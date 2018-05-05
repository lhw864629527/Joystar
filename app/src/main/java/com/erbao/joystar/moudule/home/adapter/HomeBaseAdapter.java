package com.erbao.joystar.moudule.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.erbao.joystar.R;
import com.erbao.joystar.moudule.home.activity.ContextCommentsActivity;
import com.erbao.joystar.moudule.home.activity.RightImgActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneCommentsActivity;
import com.erbao.joystar.moudule.qzone.activity.Vide2Activity;
import com.erbao.joystar.moudule.qzone.activity.VideActivity;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Android on 2017/4/6.
 */

public class HomeBaseAdapter extends BaseAdapter {
    private Context context;
    private String type;
    private List<Map<String, Object>> data_list;
    private int selectedItem = -1;


    public HomeBaseAdapter(Context context, String type, List<Map<String, Object>> data_list) {
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
        if (type.equals("homefragment_content")) {
            item = convertView.inflate(context, R.layout.homefragment_content_item, null);

            final ImageView homecontentBgimg = (ImageView) item.findViewById(R.id.homecontent_bgimg);
            final ImageView homecontentHandimg = (ImageView) item.findViewById(R.id.homecontent_handimg);
            TextView homecontentNickname = (TextView) item.findViewById(R.id.homecontent_nickname);
            TextView homecontentAddress = (TextView) item.findViewById(R.id.homecontent_address);
            TextView homecontentTime = (TextView) item.findViewById(R.id.homecontent_time);

            GlideImageLoader.show(context, data_list.get(position).get("user_coverimg").toString(), homecontentBgimg);
            GlideImageLoader.showCircle(context, data_list.get(position).get("user_photo").toString(), homecontentHandimg, R.mipmap.circle_bg_img);
            homecontentNickname.setText(data_list.get(position).get("user_partname").toString());
            homecontentAddress.setText(data_list.get(position).get("user_country").toString());
            homecontentTime.setText(data_list.get(position).get("dynamic_time").toString());


            homecontentBgimg.setTag(position);
            homecontentBgimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tps = Integer.parseInt(homecontentBgimg.getTag() + "");
                    context.startActivity(new Intent(context, ContextCommentsActivity.class)
                            .putExtra("userId", data_list.get(tps).get("user_id").toString())
                            .putExtra("user_coverimg", data_list.get(tps).get("user_coverimg").toString())
                    );
                }
            });

            homecontentHandimg.setTag(position);
            homecontentHandimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tps = Integer.parseInt(homecontentHandimg.getTag() + "");
                    context.startActivity(new Intent(context, QzoneActivity.class).putExtra("userId", data_list.get(tps).get("user_id").toString()));
                }
            });


        } else if (type.equals("homefragment_right")) {
            item = convertView.inflate(context, R.layout.homefragment_reght_item, null);
            final ImageView fragminRightImg = (ImageView) item.findViewById(R.id.fragmin_right_img);
            TextView fragminRightImgsize = (TextView) item.findViewById(R.id.fragmin_right_imgsize);
            TextView fragminRightTitle = (TextView) item.findViewById(R.id.fragmin_right_title);
            final ImageView fragminRightPhoto = (ImageView) item.findViewById(R.id.fragmin_right_photo);
            TextView fragminRightName = (TextView) item.findViewById(R.id.fragmin_right_name);
            TextView fragminRightCounty = (TextView) item.findViewById(R.id.fragmin_right_county);
            TextView fragminRightTime = (TextView) item.findViewById(R.id.fragmin_right_time);

            GlideImageLoader.show(context, data_list.get(position).get("user_coverimg").toString(), fragminRightImg);
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) data_list.get(position).get("data");
            fragminRightImgsize.setText("+" + mapList.size() + context.getResources().getString(R.string.picture));
            fragminRightTitle.setText(data_list.get(position).get("user_tag") + "");
            GlideImageLoader.showCircle(context, data_list.get(position).get("user_photo").toString(), fragminRightPhoto, R.mipmap.circle_bg_img);
            fragminRightName.setText(data_list.get(position).get("user_partname") + "");
            fragminRightCounty.setText(data_list.get(position).get("user_country") + "");
            fragminRightTime.setText(data_list.get(position).get("dynamic_time") + "");


            fragminRightImg.setTag(position);
            fragminRightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tps = Integer.parseInt(fragminRightImg.getTag() + "");
                    List<Map<String, Object>> mapList = (List<Map<String, Object>>) data_list.get(tps).get("data");
                    if (mapList.get(0).get("img_url").toString().contains(".mp4")) {
                        LogUtils.e("==========mp4====");
//                        context.startActivity(new Intent(context, VideActivity.class)
//                                .putExtra("superUserId", data_list.get(tps).get("user_id").toString())
//                                .putExtra("imgId", mapList.get(0).get("img_id").toString())
//                                .putExtra("img_url",mapList.get(0).get("img_url").toString())
//                        );
                        context.startActivity(new Intent(context, Vide2Activity.class)
                                .putExtra("superUserId", data_list.get(tps).get("user_id").toString())
                                .putExtra("imgId", mapList.get(0).get("img_id").toString())
                                .putExtra("img_url", mapList.get(0).get("img_url").toString())
                        );

                    } else {
                        context.startActivity(new Intent(context, RightImgActivity.class)
                                .putExtra("img_url", mapList.get(0).get("img_url").toString())
                                .putExtra("dataArray", mapList.get(0).get("dataArray").toString())
                        );
                        LogUtils.e("==========img====");
                    }


                }
            });

            fragminRightPhoto.setTag(position);
            fragminRightPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tps = Integer.parseInt(fragminRightPhoto.getTag() + "");
                    context.startActivity(new Intent(context, QzoneActivity.class).putExtra("userId", data_list.get(tps).get("user_id").toString()));

                }
            });


        } else if (type.equals("ContextCommentsActivity")) {
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


        } else if (type.equals("RightImgActivity")) {
            item = convertView.inflate(context, R.layout.rightimgactivity_item, null);
            ImageView img = (ImageView) item.findViewById(R.id.img);
            GlideImageLoader.show(context,data_list.get(position).get("img_url").toString(),img);




        } else if (type.equals("")) {
        } else if (type.equals("")) {
        } else if (type.equals("")) {


        }


        return item;

    }

}
