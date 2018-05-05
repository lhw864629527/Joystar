package com.erbao.videoimg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erbao.videoimg.R;

import java.io.InputStream;
import java.util.List;


/**
 * Created by Android on 2017/4/6.
 */

public class BaseAdapterBitmap extends android.widget.BaseAdapter {


    private Activity context;
    private String type;
    private List<Bitmap> data_list;
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


    public BaseAdapterBitmap(Activity context, String type, List<Bitmap> data_list) {
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
        if (type.equals("VideoCut2Activity")) {
            item = convertView.inflate(context, R.layout.video_thumb_itme_layout, null);
            ImageView   thumb = (ImageView) item.findViewById(R.id.thumb);
            thumb.setImageBitmap(data_list.get(position));



        } else if (type.equals("")) {
        } else if (type.equals("")) {
        } else if (type.equals("")) {


        }


        return item;

    }

}
