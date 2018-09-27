package about.nocare.casaer.satanwang.adapter.appmore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import about.nocare.casaer.satanwang.R;

/**
 * Created by zhangyulong on 16/9/22.
 */
public class MyAdapter extends BaseDragableAdapter<String> {
    public MyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_grid_view , null);
            viewHolder.mItemTv = (TextView) view.findViewById(R.id.item_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        if (mHidePosition == i) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        viewHolder.mItemTv.setText(mList.get(i));
        return view;
    }

    class ViewHolder{
        TextView mItemTv;
    }
}
