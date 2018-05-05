//package com.erbao.joystar.moudule.qzone.videoview;
//
///**
// * Created by baidu on 2017/2/10.
// */
//
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.media.MediaPlayer;
//import android.opengl.GLSurfaceView;
//import android.view.Surface;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
//import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
//
//public class VideoSurfaceView extends GLSurfaceView {
//    private static final String TAG = "VideoSurfaceView";
//    private static final int SLEEP_TIME_MS = 1000;
//    GPUImageRenderer mRenderer;
//    private MediaPlayer mMediaPlayer = null;
//    public VideoSurfaceView(Context context, MediaPlayer mp) {
//        super(context);
//        setEGLContextClientVersion(2);
//        mMediaPlayer = mp;
//        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
//        filterGroup.addFilter(new GPUImageExtTexFilter());
//        filterGroup.addFilter(new GPUImageFilter());
//        mRenderer = new GPUImageRenderer(filterGroup);
//
//        // following function: setUpSurafceTexture(MediaPlayer) is created by baidu
//        mRenderer.setUpSurfaceTexture(mMediaPlayer);
//        setRenderer(mRenderer);
//    }
//
//    public void setFilter(GPUImageFilter filter) {
//        mRenderer.setFilter(filter);
//    }
//
//    public void setSourceSize(int imageWidth, int imageHeight) {
//        mRenderer.setSourceSize(imageWidth, imageHeight);
//    }
////    @Override
////    public void onResume() {
////        queueEvent(new Runnable(){
////            public void run() {
////                mRenderer.setMediaPlayer(mMediaPlayer);
////            }});
////        super.onResume();
////    }
////    public void startTest() throws Exception {
////        Thread.sleep(SLEEP_TIME_MS);
////        mMediaPlayer.start();
////        Thread.sleep(SLEEP_TIME_MS * 5);
////        mMediaPlayer.setSurface(null);
////        while (mMediaPlayer.isPlaying()) {
////            Thread.sleep(SLEEP_TIME_MS);
////        }
////    }
//    /**
//     * A GLSurfaceView implementation that wraps TextureRender.  Used to render frames from a
//     * video decoder to a View.
//     */
//    private static class VideoRender
//            implements Renderer, SurfaceTexture.OnFrameAvailableListener {
//        private static String TAG = "VideoRender";
//        private TextureRender mTextureRender;
//        private SurfaceTexture mSurfaceTexture;
//        private boolean updateSurface = false;
//        private MediaPlayer mMediaPlayer;
//        public VideoRender(Context context) {
//            mTextureRender = new TextureRender();
//        }
//        public void setMediaPlayer(MediaPlayer player) {
//            mMediaPlayer = player;
//        }
//        public void onDrawFrame(GL10 glUnused) {
//            synchronized(this) {
//                if (updateSurface) {
//                    mSurfaceTexture.updateTexImage();
//                    updateSurface = false;
//                }
//            }
//            mTextureRender.drawFrame(mSurfaceTexture);
//        }
//        public void onSurfaceChanged(GL10 glUnused, int width, int height) {
//        }
//        public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
//            mTextureRender.surfaceCreated();
//            /*
//             * Create the SurfaceTexture that will feed this textureID,
//             * and pass it to the MediaPlayer
//             */
//            mSurfaceTexture = new SurfaceTexture(mTextureRender.getTextureId());
//            mSurfaceTexture.setOnFrameAvailableListener(this);
//            Surface surface = new Surface(mSurfaceTexture);
//            mMediaPlayer.setSurface(surface);
//            surface.release();
//            synchronized(this) {
//                updateSurface = false;
//            }
//        }
//        synchronized public void onFrameAvailable(SurfaceTexture surface) {
//            updateSurface = true;
//        }
//    }  // End of class VideoRender.
//}  // End of class VideoSurfaceView.