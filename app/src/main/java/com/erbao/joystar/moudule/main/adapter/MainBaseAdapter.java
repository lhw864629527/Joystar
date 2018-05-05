package com.erbao.joystar.moudule.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Android on 2017/4/6.
 */

public class MainBaseAdapter extends BaseAdapter {
    private Context context;
    private String type;
    private List<Map<String, Object>> data_list;
    private int selectedItem = -1;




    public MainBaseAdapter(Context context, String type, List<Map<String, Object>> data_list) {
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
        if (type.equals("HomePageFragment")) {
//            item = convertView.inflate(context, R.layout.home_gridview_item, null);

        } else if (type.equals("")) {
        } else if (type.equals("")) {


        }


        return item;

    }

}
