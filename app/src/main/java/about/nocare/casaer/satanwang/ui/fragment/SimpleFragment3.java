package about.nocare.casaer.satanwang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.appmore.MyAdapter;
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
        MyAdapter adapter = new MyAdapter(getActivity());
        List<String> list = new ArrayList<String>();
        list.add("抖音视频\n效果");
        list.add("腾讯新闻\n视频效果");
        list.add("不同模式\n播放视频");
        list.add("拍摄小视\n频");
        adapter.setList(list);
        dragGridView.setAdapter(adapter);
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
