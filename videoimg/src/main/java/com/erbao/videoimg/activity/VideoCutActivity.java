package com.erbao.videoimg.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.erbao.videoimg.R;
import com.erbao.videoimg.adapter.BaseAdapterBitmap;
import com.erbao.videoimg.cut_utils.VideoClipper;
import com.erbao.videoimg.interfaces.OnProgressVideoListener;
import com.erbao.videoimg.interfaces.OnRangeSeekBarListener;
import com.erbao.videoimg.interfaces.OnTrimVideoListener;
import com.erbao.videoimg.utils.TrimVideoUtil;
import com.erbao.videoimg.views.LoadingDialog;
import com.erbao.videoimg.views.RangeSeekBarView;
import com.erbao.videoimg.views.Thumb;
import com.erbao.videoimg.views.VideoThumbHorizontalListView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import iknow.android.utils.DeviceUtil;
import iknow.android.utils.UnitConverter;
import iknow.android.utils.callback.SingleCallback;
import iknow.android.utils.thread.BackgroundExecutor;
import iknow.android.utils.thread.UiThreadExecutor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideoCutActivity extends Activity implements  OnTrimVideoListener {


    private RelativeLayout layout;
    private TextView videoShootTip;
    private VideoThumbHorizontalListView videoThumbListview;


    /**
     * 计算公式:
     * PixRangeMax = (视频总长 * SCREEN_WIDTH) / 视频最长的裁剪时间(15s)
     * 视频总长/PixRangeMax = 当前视频的时间/游标当前所在位置
     */
    private static boolean isDebugMode = false;

    //    private static final String TAG = getSimpleName();
    private static final String TAG = "VideoCut2Activity";
    private static final int margin = UnitConverter.dpToPx(6);
    private static final int SCREEN_WIDTH = (DeviceUtil.getDeviceWidth() - margin * 2);
    private static final int SCREEN_WIDTH_FULL = DeviceUtil.getDeviceWidth();
    private static final int SHOW_PROGRESS = 2;


    private SeekBar mSeekBarView;
    private RangeSeekBarView mRangeSeekBarView;
    private RelativeLayout mLinearVideo;
    private VideoView mVideoView;
    private ImageView mPlayView;


    private String mFinalPath;

    private long mMaxDuration = 15 * 1000;
    private OnProgressVideoListener mListeners;
    BaseAdapterBitmap baseAdapterBitmap;
    private OnTrimVideoListener mOnTrimVideoListener;
    private int mDuration = 0;
    private long mTimeVideo = 0;
    private long mStartPosition = 0, mEndPosition = 0;
    private Uri mSrc;

    private long pixelRangeMax;
    private int currentPixMax;  //用于处理红色进度条
    private int mScrolledOffset;
    private float leftThumbValue, rightThumbValue;
    private boolean isFromRestore = false;
    List<Bitmap> bitmaps = new ArrayList<>();
    private ImageView videoCutBack;
    private TextView videoCutCmple;
    private final MessageHandler mMessageHandler = new MessageHandler(VideoCutActivity.this);
    String savepath;
    LoadingDialog Loaddialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Loaddialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("path", savepath);
                    setResult(1, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_cut);


         Loaddialog=new LoadingDialog(this);
        Loaddialog.setTips(getResources().getString(R.string.Shearing_process));

        mLinearVideo = (RelativeLayout) findViewById(R.id.layout_surface_view);
        mVideoView = (VideoView) findViewById(R.id.video_loader);
        layout = (RelativeLayout) findViewById(R.id.layout);
        videoShootTip = (TextView) findViewById(R.id.video_shoot_tip);
        videoThumbListview = (VideoThumbHorizontalListView) findViewById(R.id.video_thumb_listview);
        mSeekBarView = (SeekBar) findViewById(R.id.handlerTop);
        mRangeSeekBarView = (RangeSeekBarView) findViewById(R.id.timeLineBar);

        mPlayView = (ImageView) findViewById(R.id.icon_video_play);


        videoCutBack = (ImageView) findViewById(R.id.video_cut_back);
        videoCutCmple = (TextView) findViewById(R.id.video_cut_cmple);
        videoCutCmple.getBackground().setAlpha(80);


        mOnTrimVideoListener = this;


        setUpSeekBar();


        savepath = getIntent().getStringExtra("savepath");


        File file = new File(savepath);
        File files = new File(String.valueOf(file.getParentFile()));
        if (!files.exists()) {
            files.mkdirs();
        }

        String path = getIntent().getStringExtra("path");
        mSrc = Uri.parse(path);
        mVideoView.setVideoPath(path);
        mVideoView.requestFocus();


        bitmaps = new ArrayList<>();
        baseAdapterBitmap = new BaseAdapterBitmap(this, "VideoCut2Activity", bitmaps);
        videoThumbListview.setAdapter(baseAdapterBitmap);
        TrimVideoUtil.backgroundShootVideoThumb(this, Uri.parse(path), new SingleCallback<ArrayList<Bitmap>, Integer>() {
            @Override
            public void onSingleCallback(final ArrayList<Bitmap> bitmap, final Integer interval) {
                UiThreadExecutor.runTask("", new Runnable() {
                    @Override
                    public void run() {
                        bitmaps.addAll(bitmap);
                        baseAdapterBitmap.notifyDataSetChanged();
                    }
                }, 0L);

            }
        });


        videoThumbListview.setOnScrollStateChangedListener(new VideoThumbHorizontalListView.OnScrollStateChangedListener() {
            @Override
            public void onScrollStateChanged(ScrollState scrollState, int scrolledOffset) {
                if (videoThumbListview.getCurrentX() == 0) {
                    return;
                }

                switch (scrollState) {

                    case SCROLL_STATE_FLING:
                    case SCROLL_STATE_IDLE:
                    case SCROLL_STATE_TOUCH_SCROLL:

                        if (scrolledOffset < 0) {
                            mScrolledOffset = mScrolledOffset - Math.abs(scrolledOffset);
                            if (mScrolledOffset <= 0)
                                mScrolledOffset = 0;
                        } else {
                            if (PixToTime(mScrolledOffset + SCREEN_WIDTH) <= mDuration)//根据时间来判断还是否可以向左滚动
                                mScrolledOffset = mScrolledOffset + scrolledOffset;
                        }
                        onVideoReset();
                        onSeekThumbs(0, mScrolledOffset + leftThumbValue);
                        onSeekThumbs(1, mScrolledOffset + rightThumbValue);
                        mRangeSeekBarView.invalidate();
                        break;

                }


            }
        });


        mListeners = new OnProgressVideoListener() {
            @Override
            public void updateProgress(int time, int max, float scale) {
                updateVideoProgress(time);
            }
        };
        mRangeSeekBarView.addOnRangeSeekBarListener(new OnRangeSeekBarListener() {
            @Override
            public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {
            }

            @Override
            public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
                if (index == 0) {
                    leftThumbValue = value;
                } else {
                    rightThumbValue = value;
                }

                onSeekThumbs(index, value + Math.abs(mScrolledOffset));
            }

            @Override
            public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {
                if (mSeekBarView.getVisibility() == View.VISIBLE)
                    mSeekBarView.setVisibility(GONE);
            }

            @Override
            public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
                onStopSeekThumbs();
            }
        });

        mSeekBarView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                onPlayerIndicatorSeekStart();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onPlayerIndicatorSeekStop(seekBar);
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });

        mPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mMessageHandler.removeMessages(SHOW_PROGRESS);
                } else {
                    mVideoView.start();
                    mSeekBarView.setVisibility(View.VISIBLE);
                    mMessageHandler.sendEmptyMessage(SHOW_PROGRESS);
                }

                setPlayPauseViewIcon(mVideoView.isPlaying());
            }
        });


        //----------------点击事件------------------------

        videoCutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
                finish();
            }
        });
        videoCutCmple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEndPosition / 1000 - mStartPosition / 1000 < TrimVideoUtil.MIN_TIME_FRAME) {
                    Toast.makeText(VideoCutActivity.this, getResources().getText(R.string.video_5seconds), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("TaG", "====输入地址====mSrc====" + mSrc.getPath());
                    Log.e("TaG", "====开始时间====mStartPosition====" + mStartPosition);
                    Log.e("TaG", "====结束时间====mEndPosition====" + mEndPosition);

                    mVideoView.pause();

                    Log.e("TaG", "========开始处理。。。。。====");


                    Loaddialog.show();

                    VideoClipper clipper = new VideoClipper();
                    clipper.setInputVideoPath(mSrc.getPath());

                    try {
                        String outputPath = savepath;
                        clipper.setOutputVideoPath(outputPath);
                        clipper.clipVideo(mStartPosition * 1000, mEndPosition * 1000);
                        clipper.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
                            @Override
                            public void onFinish() {
                                Log.e("TaG", "========处理完成====");
                                handler.sendEmptyMessage(0);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });




    }

    private void setUpSeekBar() {
        mSeekBarView.setEnabled(false);
        mSeekBarView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return false;
                }

                return true;
            }
        });

    }




    private void initSeekBarPosition() {
        seekTo(mStartPosition);
        //时间与屏幕的刻度永远保持一致
        pixelRangeMax = (mDuration * SCREEN_WIDTH) / mMaxDuration;
        mRangeSeekBarView.initThumbForRangeSeekBar(mDuration, pixelRangeMax);

        //大于15秒的时候,游标处于0-15秒
        if (mDuration >= mMaxDuration) {
            mEndPosition = mMaxDuration;
            mTimeVideo = mMaxDuration;
        } else {//小于15秒,游标处于0-mDuration
            mEndPosition = mDuration;
            mTimeVideo = mDuration;
        }

        setUpProgressBarMarginsAndWidth(margin, SCREEN_WIDTH_FULL - (int) TimeToPix(mEndPosition) - margin);//Fucking seekBar,Waste a lot of my time

        mRangeSeekBarView.setThumbValue(0, 0);
        mRangeSeekBarView.setThumbValue(1, TimeToPix(mEndPosition));
        mVideoView.pause();
        setProgressBarMax();
        setProgressBarPosition(mStartPosition);
        mRangeSeekBarView.initMaxWidth();
        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);

        /**记录两个游标对应屏幕的初始位置,这个两个值只会在视频长度可以滚动的时候有效*/
        leftThumbValue = 0;
        rightThumbValue = mDuration <= mMaxDuration ? TimeToPix(mDuration) : TimeToPix(mMaxDuration);
    }

    private void setUpProgressBarMarginsAndWidth(int left, int right) {
        if (left == 0)
            left = margin;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSeekBarView.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        mSeekBarView.setLayoutParams(lp);
        currentPixMax = SCREEN_WIDTH_FULL - left - right;
        mSeekBarView.getLayoutParams().width = currentPixMax;
    }

    private void initSeekBarFromRestore() {

        seekTo(mStartPosition);
        setUpProgressBarMarginsAndWidth((int) leftThumbValue, (int) (SCREEN_WIDTH_FULL - rightThumbValue - margin));//设置seekar的左偏移量

        setProgressBarMax();
        setProgressBarPosition(mStartPosition);
        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);

        leftThumbValue = 0;
        rightThumbValue = mDuration <= mMaxDuration ? TimeToPix(mDuration) : TimeToPix(mMaxDuration);
    }

    private void onCancelClicked() {
        mOnTrimVideoListener.onCancel();
    }

    private void onPlayerIndicatorSeekStart() {
        mMessageHandler.removeMessages(SHOW_PROGRESS);
        mVideoView.pause();
        notifyProgressUpdate();
    }


    private void onPlayerIndicatorSeekStop(SeekBar seekBar) {
        mVideoView.pause();
    }


    private void onVideoPrepared(MediaPlayer mp) {

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = mLinearVideo.getWidth();
        int screenHeight = mLinearVideo.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);

        mDuration = (mVideoView.getDuration() / 1000) * 1000;
        if (!getRestoreState())
            initSeekBarPosition();
        else {
            setRestoreState(false);
            initSeekBarFromRestore();
        }
    }

    /**
     * 屏幕长度转化成视频的长度
     */
    private long PixToTime(float value) {
        if (pixelRangeMax == 0)
            return 0;
        return (long) ((mDuration * value) / pixelRangeMax);
    }

    /**
     * 视频长度转化成屏幕的长度
     */
    private long TimeToPix(long value) {
        return (pixelRangeMax * value) / mDuration;
    }

    private void seekTo(long msec) {
        mVideoView.seekTo((int) msec);
    }


    private boolean getRestoreState() {
        return isFromRestore;
    }

    public void setRestoreState(boolean fromRestore) {
        isFromRestore = fromRestore;
    }


    private void notifyProgressUpdate() {
        if (mDuration == 0) return;

        int position = mVideoView.getCurrentPosition();
        if (isDebugMode) Log.i("Jason", "updateVideoProgress position = " + position);
        mListeners.updateProgress(position, 0, 0);
    }


    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                mStartPosition = PixToTime(value);
                setProgressBarPosition(mStartPosition);
                break;
            }
            case Thumb.RIGHT: {
                mEndPosition = PixToTime(value);
                if (mEndPosition > mDuration)//实现归位
                    mEndPosition = mDuration;
                break;
            }
        }
        setProgressBarMax();

        mRangeSeekBarView.setStartEndTime(mStartPosition, mEndPosition);
        seekTo(mStartPosition);
        mTimeVideo = mEndPosition - mStartPosition;

        setUpProgressBarMarginsAndWidth((int) leftThumbValue, (int) (SCREEN_WIDTH_FULL - rightThumbValue - margin));//设置seekar的左偏移量
    }

    private void onStopSeekThumbs() {
        mMessageHandler.removeMessages(SHOW_PROGRESS);
        setProgressBarPosition(mStartPosition);
        onVideoReset();
    }

    private void onVideoCompleted() {
        seekTo(mStartPosition);
        setPlayPauseViewIcon(false);
    }

    private void onVideoReset() {
        mVideoView.pause();
        setPlayPauseViewIcon(false);
    }

    public void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            mMessageHandler.removeMessages(SHOW_PROGRESS);
            mVideoView.pause();
            seekTo(mStartPosition);//复位
            setPlayPauseViewIcon(false);
        }
    }

    private void setProgressBarPosition(long time) {
        mSeekBarView.setProgress((int) (time - mStartPosition));
    }

    private void setProgressBarMax() {
        mSeekBarView.setMax((int) (mEndPosition - mStartPosition));
    }


    /**
     * Cancel trim thread execut action when finish
     */
    public void destroy() {
        BackgroundExecutor.cancelAll("", true);
        UiThreadExecutor.cancelAll("");
    }


    private void updateVideoProgress(int time) {
        if (mVideoView == null) {
            return;
        }
        if (isDebugMode) Log.i("Jason", "updateVideoProgress time = " + time);
        if (time >= mEndPosition) {
            mMessageHandler.removeMessages(SHOW_PROGRESS);
            mVideoView.pause();
            seekTo(mStartPosition);
            setPlayPauseViewIcon(false);
            return;
        }

        if (mSeekBarView != null) {
            setProgressBarPosition(time);
        }
    }


    @Override
    public void onStartTrim() {

    }

    @Override
    public void onFinishTrim(Uri uri) {
        Looper.prepare();
        finish();
    }

    @Override
    public void onCancel() {
        destroy();
        finish();
    }


    private static class MessageHandler extends Handler {


        private final WeakReference<VideoCutActivity> mView;

        MessageHandler(VideoCutActivity view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoCutActivity view = mView.get();
            if (view == null || view.mVideoView == null) {
                return;
            }

            view.notifyProgressUpdate();
            if (view.mVideoView.isPlaying()) {
                sendEmptyMessageDelayed(0, 10);
            }
        }
    }


    private void setPlayPauseViewIcon(boolean isPlaying) {
        mPlayView.setImageResource(isPlaying ? R.mipmap.icon_video_pause_black : R.mipmap.icon_video_play_black);
    }

}
