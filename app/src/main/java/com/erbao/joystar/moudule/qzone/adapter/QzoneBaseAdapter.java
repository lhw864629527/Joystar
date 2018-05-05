package com.erbao.joystar.moudule.qzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erbao.joystar.R;

import com.erbao.joystar.moudule.qzone.activity.HtmlActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneCommentsActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneNewsdataActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneSendnewsActivity;
import com.erbao.joystar.moudule.qzone.activity.Vide2Activity;
import com.erbao.joystar.moudule.qzone.activity.VideActivity;

import com.erbao.joystar.moudule.qzone.utils.ImgVideoWH;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.AutoHeightImageView;
import com.erbao.joystar.views.MyGridView;
import com.erbao.videoimg.gpufilter.basefilter.GPUImage;

import java.util.List;
import java.util.Map;


import static android.widget.GridView.STRETCH_COLUMN_WIDTH;
import static com.erbao.joystar.okhttp.HttpUrls.QzoneSendnewslist;
import static com.erbao.joystar.okhttp.HttpUrls.isLogin;

/**
 * Created by Android on 2017/4/6.
 */

public class QzoneBaseAdapter extends BaseAdapter {
    private Context context;
    private String type;
    private List<Map<String, Object>> data_list;
    private int selectedItem = -1;


    public QzoneBaseAdapter(Context context, String type, List<Map<String, Object>> data_list) {
        this.context = context;
        this.data_list = data_list;
        this.type = type;
    }

    @Override
    public int getCount() {
        // How many items are in the data set represented by this Adapter.(在此适配器中所代表的数据集中的条目数)
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        // Get the data item associated with the specified position in the data set.(获取数据集中与指定索引对应的数据项)
        return data_list.get(position);
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public long getItemId(int position) {
        // Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // Get a View that displays the data at the specified position in the data set.
        View item = null;
        if (type.equals("QzoneActivity")) {
            item = convertView.inflate(context, R.layout.activity_qzone_item, null);

            ImageView qzoneItemGuanggaoimg = (ImageView) item.findViewById(R.id.qzone_item_guanggaoimg);//广告图
            TextView qzoneItemContext = (TextView) item.findViewById(R.id.qzone_item_context);//文字内容
            TextView qzoneItemLikes = (TextView) item.findViewById(R.id.qzone_item_likes);//点赞数
            TextView qzoneItemComment = (TextView) item.findViewById(R.id.qzone_item_comment);//评论
            TextView qzoneItemShared = (TextView) item.findViewById(R.id.qzone_item_shared);//分享数
            TextView qzoneItemPlatforms = (TextView) item.findViewById(R.id.qzone_item_platforms);//平台
            TextView qzoneItemClicks = (TextView) item.findViewById(R.id.qzone_item_clicks);//点击数
            ImageView qzoneItemShare = (ImageView) item.findViewById(R.id.qzone_item_share);//点击分享

//            qzoneItemGuanggaoimg.setVisibility(View.GONE);
            qzoneItemContext.setText(data_list.get(position).get("dynamic_text") + "");
            qzoneItemLikes.setText(data_list.get(position).get("like_counts") + "");
            qzoneItemComment.setText(data_list.get(position).get("review_counts") + "");
            qzoneItemShared.setText("0");
            qzoneItemPlatforms.setText("0");
            qzoneItemClicks.setText("0");

            List<Map<String, Object>> mapList = (List<Map<String, Object>>) data_list.get(position).get("data");
            final MyGridView qzoneItemGridview = (MyGridView) item.findViewById(R.id.qzone_item_gridview);
            qzoneItemGridview.getBackground().setAlpha(50);
            int ad = mapList.size();
            if (ad == 1) {
                qzoneItemGridview.setNumColumns(1);
            } else if (ad == 2) {
                qzoneItemGridview.setNumColumns(2);
            } else if (ad == 3) {
                qzoneItemGridview.setNumColumns(3);
            } else if (ad > 3) {
                qzoneItemGridview.setNumColumns(3);
            }

            QzoneBaseAdapter qzoneBaseAdapter = new QzoneBaseAdapter(context, "activity_qzone_item", mapList);
            qzoneItemGridview.setAdapter(qzoneBaseAdapter);
            qzoneItemGridview.setTag(position);
            qzoneItemGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!isLogin) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        context.startActivity(new Intent(context, QzoneNewsdataActivity.class).putExtra("index", qzoneItemGridview.getTag() + ""));
                    }
                }
            });


//广告图片
            qzoneItemGuanggaoimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, HtmlActivity.class));
                }
            });

            qzoneItemShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(context, context.getResources().getString(R.string.Staytunedfor));
                }
            });

        } else if (type.equals("activity_qzone_item")) {
            item = convertView.inflate(context, R.layout.activity_qzone_item_item, null);
//            AutoHeightImageView img = (AutoHeightImageView) item.findViewById(R.id.img);
            ImageView img = (ImageView) item.findViewById(R.id.img);
            LogUtils.e("==========data_list111========" + data_list);

            if (data_list.size() == 1) {
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                img.measure(w, h);
                int height = img.getMeasuredHeight();
                int width = img.getMeasuredWidth();

                //   设置控件的高度：
                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) img.getLayoutParams(); //取控件textView当前的布局参数
//                    linearParams.height = wh[1];// 控件的高强制设成20
                linearParams.width = width;// 控件的宽强制设成30
                img.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

            }


            if (data_list.get(position).get("gif_img").toString().length() > 1) {
                GlideImageLoader.show(context, data_list.get(position).get("gif_img").toString(), img);
            } else {
                GlideImageLoader.show(context, data_list.get(position).get("img_url").toString(), img);
            }


        } else if (type.equals("QzoneSendnewsActivity")) {
            item = convertView.inflate(context, R.layout.qzonesendnews_item, null);
            ImageView qzonesendnewsItemImg = (ImageView) item.findViewById(R.id.qzonesendnews_item_img);
            final EditText qzonesendnewsItemEdt = (EditText) item.findViewById(R.id.qzonesendnews_item_edt);
            final ImageView qzonesendnewsItemDete = (ImageView) item.findViewById(R.id.qzonesendnews_item_dete);

            qzonesendnewsItemImg.setImageBitmap(BitmapFactory.decodeFile(data_list.get(position).get("img") + ""));
            qzonesendnewsItemEdt.setText(data_list.get(position).get("text") + "");
            qzonesendnewsItemEdt.setTag(position);
            qzonesendnewsItemEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    int index = Integer.parseInt(qzonesendnewsItemEdt.getTag() + "");
                    QzoneSendnewslist.get(index).put("text", qzonesendnewsItemEdt.getText().toString());

                    LogUtils.e("====================" + qzonesendnewsItemEdt.getText().toString());

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            qzonesendnewsItemDete.setTag(position);
            qzonesendnewsItemDete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pst = Integer.parseInt(qzonesendnewsItemDete.getTag() + "");
                    QzoneSendnewsActivity.setdata(pst);
                }
            });

        } else if (type.equals("QzoneNewsdataActivity")) {
            item = convertView.inflate(context, R.layout.qzonenewsdata_item, null);

            TextView qzonenewsdataItemContxet = (TextView) item.findViewById(R.id.qzonenewsdata_item_contxet);//内容
            TextView qzonenewsdataItemTime = (TextView) item.findViewById(R.id.qzonenewsdata_item_time);//时间
            final ImageView qzonenewsdataItemImg = (ImageView) item.findViewById(R.id.qzonenewsdata_item_img);//图片
            TextView qzonenewsdataItemText = (TextView) item.findViewById(R.id.qzonenewsdata_item_text);//点赞数和时间
            final TextView qzonenewsdataItemPinglun = (TextView) item.findViewById(R.id.qzonenewsdata_item_pinglun);//评论
            final ImageView qzonenewsdataItemDianzang = (ImageView) item.findViewById(R.id.qzonenewsdata_item_dianzang);//点赞

            if (position != 0) {
                qzonenewsdataItemContxet.setVisibility(View.GONE);
                qzonenewsdataItemTime.setVisibility(View.GONE);
            }
            qzonenewsdataItemPinglun.setTag(position);
            qzonenewsdataItemPinglun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin) {
                        int pos = Integer.parseInt(qzonenewsdataItemPinglun.getTag() + "");
                        if (data_list.get(pos).get("gif_img").toString().length() > 5) {
//                            context.startActivity(new Intent(context, VideActivity.class)
//                                    .putExtra("superUserId", data_list.get(pos).get("user_id").toString())
//                                    .putExtra("imgId", data_list.get(pos).get("img_id").toString())
//                                    .putExtra("img_url", data_list.get(pos).get("img_url").toString())
//                            );


                            context.startActivity(new Intent(context, QzoneCommentsActivity.class)
                                    .putExtra("superUserId", data_list.get(pos).get("user_id").toString())
                                    .putExtra("imgId", data_list.get(pos).get("img_id").toString())
                                    .putExtra("img_url", data_list.get(pos).get("gif_img").toString())
                            );


                        } else {
                            context.startActivity(new Intent(context, QzoneCommentsActivity.class)
                                    .putExtra("superUserId", data_list.get(pos).get("user_id").toString())
                                    .putExtra("imgId", data_list.get(pos).get("img_id").toString())
                                    .putExtra("img_url", data_list.get(pos).get("img_url").toString())
                            );
                        }

                    } else {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }

                }
            });


//------------------------设置控件的宽高-----------------
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            qzonenewsdataItemImg.measure(w, h);
            int height = qzonenewsdataItemImg.getMeasuredHeight();
            int width = qzonenewsdataItemImg.getMeasuredWidth();

            //   设置控件的高度：
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) qzonenewsdataItemImg.getLayoutParams(); //取控件textView当前的布局参数
//                    linearParams.height = wh[1];// 控件的高强制设成20
            linearParams.width = width;// 控件的宽强制设成30
            qzonenewsdataItemImg.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

//------------------------设置控件的宽高-----------------


            if (data_list.get(position).get("gif_img").toString().length() > 5) {
                LogUtils.e("=====gif_img======" + data_list.get(position).get("gif_img"));
                GlideImageLoader.show(context, data_list.get(position).get("gif_img").toString(), qzonenewsdataItemImg);
            } else {
                GlideImageLoader.show(context, data_list.get(position).get("img_url").toString(), qzonenewsdataItemImg);
            }


            qzonenewsdataItemContxet.setText(data_list.get(position).get("text") + "");
            qzonenewsdataItemTime.setText(data_list.get(position).get("crated_time") + "");
            qzonenewsdataItemText.setText(data_list.get(position).get("review_count") + context.getResources().getString(R.string.commentsd) + "  |  " +
                    data_list.get(position).get("like_count") + context.getResources().getString(R.string.thumb_up));

            if (data_list.get(position).get("status").equals("1")) {
                qzonenewsdataItemDianzang.setImageResource(R.mipmap.heart_view);
            }
            qzonenewsdataItemDianzang.setTag(position);
            qzonenewsdataItemDianzang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLogin) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        int pos = Integer.parseInt(qzonenewsdataItemDianzang.getTag() + "");
                        QzoneNewsdataActivity.setdata(pos, data_list.get(pos).get("user_id").toString(), data_list.get(pos).get("img_id").toString());
                    }

                }
            });


            qzonenewsdataItemImg.setTag(position);
            qzonenewsdataItemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(qzonenewsdataItemImg.getTag() + "");
                    if (data_list.get(pos).get("gif_img").toString().length() > 5) {
//                        context.startActivity(new Intent(context, VideActivity.class)
//                                .putExtra("superUserId", data_list.get(pos).get("user_id").toString())
//                                .putExtra("imgId", data_list.get(pos).get("img_id").toString())
//                                .putExtra("img_url", data_list.get(pos).get("img_url").toString())
//                        );


                        context.startActivity(new Intent(context, Vide2Activity.class)
                                .putExtra("superUserId", data_list.get(pos).get("user_id").toString())
                                .putExtra("imgId", data_list.get(pos).get("img_id").toString())
                                .putExtra("img_url", data_list.get(pos).get("img_url").toString())
                        );
                    }
                }
            });






        } else if (type.equals("QzoneCommentsActivity")) {
            item = convertView.inflate(context, R.layout.qzonecomments_item, null);
            ImageView commentImg = (ImageView) item.findViewById(R.id.comment_img);
            TextView commentName = (TextView) item.findViewById(R.id.comment_name);
            TextView commentTime = (TextView) item.findViewById(R.id.comment_time);
            TextView commentText = (TextView) item.findViewById(R.id.comment_text);
            LinearLayout commentLin = (LinearLayout) item.findViewById(R.id.comment_lin);

            LinearLayout commentLins = (LinearLayout) item.findViewById(R.id.comment_lins);
            TextView commentTimes = (TextView) item.findViewById(R.id.comment_times);
            TextView commentNames = (TextView) item.findViewById(R.id.comment_names);
            TextView commentTexts = (TextView) item.findViewById(R.id.comment_texts);
            ImageView commentImgs = (ImageView) item.findViewById(R.id.comment_imgs);
            LinearLayout commentLinsd = (LinearLayout) item.findViewById(R.id.comment_linsd);
            commentLinsd.getBackground().setAlpha(80);

//            if (data_list.get(position).get("isMe_status").equals("0")){
//                commentLin.setVisibility(View.VISIBLE);
//                commentLins.setVisibility(View.GONE);
//                GlideImageLoader.showCircle(context, data_list.get(position).get("user_photo").toString(), commentImg, R.mipmap.circle_bg_img);
//                commentName.setText(data_list.get(position).get("user_nickName").toString());
//                commentTime.setText(data_list.get(position).get("review_time").toString());
//                commentText.setText(data_list.get(position).get("user_review").toString());
//            }else {
//                commentLin.setVisibility(View.GONE);
//                commentLins.setVisibility(View.VISIBLE);
//                GlideImageLoader.showCircle(context, data_list.get(position).get("user_photo").toString(), commentImgs, R.mipmap.circle_bg_img);
//                commentNames.setText(data_list.get(position).get("user_nickName").toString());
//                commentTimes.setText(data_list.get(position).get("review_time").toString());
//                commentTexts.setText(data_list.get(position).get("user_review").toString());
//            }


            if (data_list.get(position).get("isMe_status").equals("0")) {
                commentImg.setVisibility(View.INVISIBLE);
            } else {
                commentImg.setVisibility(View.VISIBLE);
            }
            commentLin.setVisibility(View.VISIBLE);
            commentLins.setVisibility(View.GONE);
            GlideImageLoader.showCircle(context, data_list.get(position).get("user_photo").toString(), commentImg, R.mipmap.circle_bg_img);
            commentName.setText(data_list.get(position).get("user_nickName").toString());
            commentTime.setText(data_list.get(position).get("review_time").toString());
            commentText.setText(data_list.get(position).get("user_review").toString());


        } else if (type.equals("BitmapFilterActivity")) {
            item = convertView.inflate(context, R.layout.bitmapfilter_item, null);
            LinearLayout bitmapfilterItemLin = (LinearLayout) item.findViewById(R.id.bitmapfilter_item_lin);
            ImageView bitmapfilterItemImg = (ImageView) item.findViewById(R.id.bitmapfilter_item_img);
            TextView bitmapfilterItemText = (TextView) item.findViewById(R.id.bitmapfilter_item_text);
            ImageView bitmapfilterItemBgimg = (ImageView) item.findViewById(R.id.bitmapfilter_item_bgimg);
            bitmapfilterItemText.setText(data_list.get(position).get("text").toString());

            if (position == 0) {

            } else {
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filter_pic);
//                GPUImage gpuImage = new GPUImage(context);
//                gpuImage.setImage(bitmap);
//                gpuImage.setFilter(BitmapFilterActivity.filters.get(position));
//                bitmapfilterItemBgimg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
            }


        } else if (type.equals("VideoFilterActivity")) {

            item = convertView.inflate(context, R.layout.bitmapfilter_item, null);
            LinearLayout bitmapfilterItemLin = (LinearLayout) item.findViewById(R.id.bitmapfilter_item_lin);
            ImageView bitmapfilterItemImg = (ImageView) item.findViewById(R.id.bitmapfilter_item_img);
            TextView bitmapfilterItemText = (TextView) item.findViewById(R.id.bitmapfilter_item_text);
            ImageView bitmapfilterItemBgimg = (ImageView) item.findViewById(R.id.bitmapfilter_item_bgimg);
            bitmapfilterItemText.setText(data_list.get(position).get("text").toString());

            if (position == 0) {

            } else {
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filter_pic);
//                GPUImage gpuImage = new GPUImage(context);
//                gpuImage.setImage(bitmap);
//                gpuImage.setFilter(VideoFilterActivity.filters.get(position));
//                bitmapfilterItemBgimg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
            }


        } else if (type.equals("")) {
        } else if (type.equals("")) {


        }


        return item;

    }

}
