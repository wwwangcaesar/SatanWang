package about.nocare.casaer.satanwang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovcreate.core.base.OnClickListener;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.appMore.simple2.PicActivity;


/**
 * Created by Jaeger on 16/8/11.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class SimpleFragment2 extends Fragment {
    private TextView mTvTitle,tvBeautify;
    private View mFakeStatusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_simple2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvBeautify = (TextView) view.findViewById(R.id.tvBeautify);
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        tvBeautify.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(getActivity(), PicActivity.class));
            }
        });
    }

    public void setTvTitleBackgroundColor(@ColorInt int color) {
        mTvTitle.setBackgroundColor(color);
        mFakeStatusBar.setBackgroundColor(color);
    }
}
