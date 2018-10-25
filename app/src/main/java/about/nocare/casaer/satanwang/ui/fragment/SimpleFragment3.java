package about.nocare.casaer.satanwang.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lovcreate.core.base.OnItemClickListener;
import com.lovcreate.core.util.ToastUtil;
import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.appmore.MyAdapter;
import about.nocare.casaer.satanwang.ui.appMore.simple3.VideoRecordActivity;
import about.nocare.casaer.satanwang.ui.appMore.simple3.ViewPagerLayoutManagerActivity;
import about.nocare.casaer.satanwang.ui.home.SearchActivity;
import about.nocare.casaer.satanwang.widget.appmore.video.DragableGridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Jaeger on 16/8/11.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class SimpleFragment3 extends Fragment {
    @BindView(R.id.drag_grid_view)
    DragableGridView dragGridView;
    Unbinder unbinder;
    private TextView mTvTitle;
    private View mFakeStatusBar;
    private MyAdapter adapter;
    private List<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_simple3, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        adapter = new MyAdapter(getActivity());
        list = new ArrayList<String>();
        list.add("抖音视频\n效果");
        list.add("腾讯新闻\n视频效果");
        list.add("不同模式\n播放视频");
        list.add("拍摄小视\n频");
        adapter.setList(list);
        dragGridView.setAdapter(adapter);
        dragGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            protected void onItemNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getAdapter().getItem(position).toString().equals("抖音视频\n效果")){
                    startActivity(new Intent(getActivity(), ViewPagerLayoutManagerActivity.class));
                }else if (parent.getAdapter().getItem(position).toString().equals("腾讯新闻\n视频效果")){
                    ToastUtil.showToastBottomShort("腾讯新闻");
                }else if (parent.getAdapter().getItem(position).toString().equals("不同模式\n播放视频")){
                    ToastUtil.showToastBottomShort("不同模式");
                }else if (parent.getAdapter().getItem(position).toString().equals("拍摄小视\n频")){
                        Intent intent=new Intent(getActivity(), VideoRecordActivity.class);
                        startActivity(intent);
//                        ToastUtil.showToastBottomShort("拍摄小视");
                }

            }
        });
    }

    public void setTvTitleBackgroundColor(@ColorInt int color) {
        mTvTitle.setBackgroundColor(color);
        mFakeStatusBar.setBackgroundColor(color);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
