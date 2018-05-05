package com.erbao.videoimg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.videoimg.R;
import com.erbao.videoimg.gpufilter.basefilter.GPUImage;
import com.erbao.videoimg.gpufilter.helper.MagicFilterFactory;
import com.erbao.videoimg.gpufilter.helper.MagicFilterType;
import com.erbao.videoimg.videimg_utils.FilterMuisc;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * Created by Android on 2017/4/6.
 */

public class BaseAdapter extends android.widget.BaseAdapter {


    private Activity context;
    private String type;
    private List<Map<String, Object>> data_list;
    private int selectedItem = -1;
    private static InputStream is;
    int bHeight = 0;
    int bwidth = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    notifyDataSetInvalidated();
                    break;
                case 1:
                    bwidth = msg.arg1;
                    bHeight = msg.arg2;
                    break;

                default:
                    break;


            }


        }
    };


    public BaseAdapter(Activity context, String type, List<Map<String, Object>> data_list) {
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
        if (type.equals("VideoMiscActivity_muisc")) {
            item = convertView.inflate(context, R.layout.videomiscactivity_muisc, null);
            ImageView img = (ImageView) item.findViewById(R.id.img);

            if (data_list.get(position).get("run").toString().equals("1")) {
                img.setImageResource(R.mipmap.music_chose);
                //动画
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation);
                LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
                animation.setInterpolator(lin);
                img.startAnimation(animation);
            }


        } else if (type.equals("VideoMiscActivity_filter")) {
            item = convertView.inflate(context, R.layout.videomiscactivity_filter, null);

            ImageView bitmapfilterItemBgimg = (ImageView) item.findViewById(R.id.bitmapfilter_item_bgimg);
            ImageView bitmapfilterItemImg = (ImageView) item.findViewById(R.id.bitmapfilter_item_img);
            TextView bitmapfilterItemText = (TextView) item.findViewById(R.id.bitmapfilter_item_text);


//            map.put("filter",mapfilter.get(i).get("filter"));
//            map.put("str_en",mapfilter.get(i).get("str_en"));
//            map.put("str_cn",mapfilter.get(i).get("str_cn"));

            if (data_list.get(position).get("select").equals("1")) {
                bitmapfilterItemImg.setVisibility(View.VISIBLE);
            }
            if (FilterMuisc.isLanguage){
                bitmapfilterItemText.setText(data_list.get(position).get("str_cn").toString());
            }else {
                bitmapfilterItemText.setText(data_list.get(position).get("str_en").toString());
            }


            if (data_list.get(position).get("filter").toString().equals("NONE")){

            }else {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filter_pic);
                GPUImage gpuImage = new GPUImage(context);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(MagicFilterFactory.initFilters((MagicFilterType) data_list.get(position).get("filter")));
                bitmapfilterItemBgimg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
            }





        } else if (type.equals("VideoCameraActivity_filter")) {
            item = convertView.inflate(context, R.layout.videomiscactivity_filter, null);

            ImageView bitmapfilterItemBgimg = (ImageView) item.findViewById(R.id.bitmapfilter_item_bgimg);
            ImageView bitmapfilterItemImg = (ImageView) item.findViewById(R.id.bitmapfilter_item_img);
            TextView bitmapfilterItemText = (TextView) item.findViewById(R.id.bitmapfilter_item_text);


            if (data_list.get(position).get("select").equals("1")) {
                bitmapfilterItemImg.setVisibility(View.VISIBLE);
            }
            if (FilterMuisc.isLanguage){
                bitmapfilterItemText.setText(data_list.get(position).get("str_cn").toString());
            }else {
                bitmapfilterItemText.setText(data_list.get(position).get("str_en").toString());
            }


            if (data_list.get(position).get("filter").toString().equals("NONE")){

            }else {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filter_pic);
                GPUImage gpuImage = new GPUImage(context);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(MagicFilterFactory.initFilters((MagicFilterType) data_list.get(position).get("filter")));
                bitmapfilterItemBgimg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
            }


        } else if (type.equals("ImageFilterActivity")) {
            item = convertView.inflate(context, R.layout.videomiscactivity_filter, null);

            ImageView bitmapfilterItemBgimg = (ImageView) item.findViewById(R.id.bitmapfilter_item_bgimg);
            ImageView bitmapfilterItemImg = (ImageView) item.findViewById(R.id.bitmapfilter_item_img);
            TextView bitmapfilterItemText = (TextView) item.findViewById(R.id.bitmapfilter_item_text);


            if (data_list.get(position).get("select").equals("1")) {
                bitmapfilterItemImg.setVisibility(View.VISIBLE);
            }
            if (FilterMuisc.isLanguage){
                bitmapfilterItemText.setText(data_list.get(position).get("str_cn").toString());
            }else {
                bitmapfilterItemText.setText(data_list.get(position).get("str_en").toString());
            }


            if (data_list.get(position).get("filter").toString().equals("NONE")){

            }else {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.filter_pic);
                GPUImage gpuImage = new GPUImage(context);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(MagicFilterFactory.initFilters((MagicFilterType) data_list.get(position).get("filter")));
                bitmapfilterItemBgimg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
            }


        } else if (type.equals("")) {
        } else if (type.equals("")) {


        }


        return item;

    }

}
