package about.nocare.casaer.satanwang.adapter.appmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyulong on 16/9/22.
 */
public abstract class BaseDragableAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    //初始化需隐藏的位置信息
    protected int mHidePosition = AdapterView.INVALID_POSITION;

    public BaseDragableAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mList = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? 0 : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public void setList(List<T> list) {
        if (this.mList != null) this.mList.clear(); // 避免脏数据
        if (list == null) {
            return;
        }
        this.mList = list;
    }

    public void setList(T[] list){
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public List<T> getList() {
        return this.mList;
    }

    public void addAll(List<T> list) {
        if (list == null || list.size() == 0) return;
        this.mList.addAll(list);
    }

    public void addAll(int location, List<T> list) {
        if (list == null || list.size() == 0) return;
        this.mList.addAll(location, list);
    }

    public void clean() {
        if (getCount() == 0) return;

        mList.clear();
    }

    public void hideView(int position){
        mHidePosition = position;
        notifyDataSetChanged();
    }

    public void showHideView(){
        //重置hideposition
        mHidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    /**
     * 交换节点
     * @param draggedPos 拖拽的起始节点
     * @param currentPos 拖拽的当前节点
     */
    public void swapView(int draggedPos , int currentPos) {
        //从前往后拖
        if(draggedPos < currentPos){
            //将被拖拽的节点移动至当前节点
            mList.add(currentPos + 1 , mList.get(draggedPos));
            //删除拖拽前的节点
            mList.remove(draggedPos);
        }
        //从后往前拖
        else if (draggedPos > currentPos) {
            //将被拖拽的节点移动至当前节点
            mList.add(currentPos , mList.get(draggedPos));
            //删除拖拽前的节点
            mList.remove(draggedPos + 1);
        }
        mHidePosition = currentPos;
        notifyDataSetChanged();
    }
}