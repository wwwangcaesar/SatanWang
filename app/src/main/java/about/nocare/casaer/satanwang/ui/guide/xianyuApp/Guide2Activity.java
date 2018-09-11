package about.nocare.casaer.satanwang.ui.guide.xianyuApp;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.MainActivity;
import about.nocare.casaer.satanwang.ui.guide.xiamiMusic.ExtendedViewPager;
import about.nocare.casaer.satanwang.utils.CircleIndicator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 标题栏去不掉的原因
 * AppComentActivity换成FragmentActivity
 */
public class Guide2Activity extends FragmentActivity {
    public static final int PAGE_COUNT = 4;
    GuideAdapter mAdapter;
    @BindView(R.id.pager1)
    ExtendedViewPager pager1;
    @BindView(R.id.view_pager_indicator1)
    CircleIndicator viewPagerIndicator1;
    @BindView(R.id.tvChange1)
    TextView tvChange1;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide2_activity);
        ButterKnife.bind(this);
        mAdapter = new GuideAdapter(getSupportFragmentManager());
        pager1.setAdapter(mAdapter);
        viewPagerIndicator1.setViewPager(pager1);
        pager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
//                flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (pager1.getCurrentItem() == pager1.getAdapter()
                                .getCount() - 1 && !flag) {

//                            TranslateAnimation mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
//                                    Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
//                                    ,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
//                            mShowAnim.setDuration(300);
//                            tvChange.startAnimation(mShowAnim );
//                            startShakeByViewAnim(tvChange, 0.9f, 1.1f, 10f, 1000);
                            startShakeByPropertyAnim(tvChange1, 0.9f, 1.1f, 10f, 1000);
                            tvChange1.setVisibility(View.VISIBLE);
                        } else {
                            tvChange1.setVisibility(View.GONE);
                        }
                        flag = true;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tvChange1)
    public void onViewClicked() {
        startActivity(new Intent(Guide2Activity.this, MainActivity.class));
        finish();
    }

    static class GuideAdapter extends FragmentPagerAdapter {

        public GuideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

    /**
     * 从小变大  摇晃
     *
     * @param view
     * @param scaleSmall
     * @param scaleLarge
     * @param shakeDegrees
     * @param duration
     */
    private void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(30);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }

    /**
     * 不是放大的动画，是晃动的
     *
     * @param view
     * @param scaleSmall
     * @param scaleLarge
     * @param shakeDegrees
     * @param duration
     */
    public static void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

}
