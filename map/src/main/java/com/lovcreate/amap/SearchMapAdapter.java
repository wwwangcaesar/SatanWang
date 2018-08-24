package com.lovcreate.amap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;

import java.util.List;

/**
 * Created by Lmy on 2017/7/17.
 * email 1434117404@qq.com
 */

public class SearchMapAdapter extends BaseAdapter {
    private List<Tip> list;
    private Context context;

    public SearchMapAdapter(Context context, List<Tip> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.base_tool_bar_popup_search_item, parent, false);
            holder.tv_item_content1 = (TextView) convertView.findViewById(R.id.tv_item_content1);
            holder.tv_item_content2 = (TextView) convertView.findViewById(R.id.tv_item_content2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            resetViewHolder(holder);
        }
        Tip bean = list.get(position);

        holder.tv_item_content1.setText(bean.getName());
        holder.tv_item_content2.setText(bean.getAddress());

        return convertView;
    }

    private void resetViewHolder(ViewHolder holder) {
        holder.tv_item_content1.setText(null);
        holder.tv_item_content2.setText(null);
    }

    class ViewHolder {
        public TextView tv_item_content1;
        public TextView tv_item_content2;
    }

}
