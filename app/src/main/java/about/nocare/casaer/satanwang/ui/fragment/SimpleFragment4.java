package about.nocare.casaer.satanwang.ui.fragment;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
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


/**
 & @Description:   ARFragment
 & @Author:  Satan
 & @Time:  2018/9/13 15:30
 */
public class SimpleFragment4 extends Fragment {
    private TextView mTvTitle;
    private View mFakeStatusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_simple4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(getActivity(), R.animator.anim_view_state_change_2);
        StateListAnimator stateListAnimator1 = AnimatorInflater.loadStateListAnimator(getActivity(), R.animator.anim_view_state_change_3);

        view.findViewById(R.id.tvAR).setStateListAnimator(stateListAnimator);
        view.findViewById(R.id.tvHourse).setStateListAnimator(stateListAnimator1);

        view.findViewById(R.id.tvAR).setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

            }
        });
        view.findViewById(R.id.tvHourse).setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

            }
        });

    }

    public void setTvTitleBackgroundColor(@ColorInt int color) {
        mTvTitle.setBackgroundColor(color);
        mFakeStatusBar.setBackgroundColor(color);
    }
}
