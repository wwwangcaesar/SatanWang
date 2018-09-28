package about.nocare.casaer.satanwang.adapter.appmore;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lovcreate.core.base.OnClickListener;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

import about.nocare.casaer.satanwang.R;

/**
 * Created by Administrator Satan
 * on 2018/9/28.
 * in 13:32
 * ----------->
 * 注意事项
 */

public class HorizontalListAdapter extends SuperAdapter<String> {

    private Context context;
    private Callback callback;
    private int selectIndex = -1;//声明默认选中的项

    public HorizontalListAdapter(Context context, List<String> list) {
        super(context, list, R.layout.recycleview_bringinto);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, String item) {
        if (item == null) {
            return;
        }

        final RadioButton radioButton = holder.findViewById(R.id.tv_toolname);
        radioButton.setText(item);
        radioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                selectIndex = layoutPosition;
                if(callback!=null){
                    callback.onCheckChanged();
                }
                notifyDataSetHasChanged();
            }
        });
        if (selectIndex == layoutPosition) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
    }

    public void setDefSelect(int position) {
        this.selectIndex = position;
    }

    //获取列表项信息
    public String getSelectItem() {
        if (getData().isEmpty())
            return "";
        return getData().get(selectIndex);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCheckChanged();
    }
}
