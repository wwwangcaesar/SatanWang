package about.nocare.casaer.satanwang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.guide.xiamiMusic.ExtendedViewPager;
import about.nocare.casaer.satanwang.ui.guide.xiamiMusic.VideoItemFragment;
import about.nocare.casaer.satanwang.ui.guide.xianyuApp.Guide2Activity;
import about.nocare.casaer.satanwang.utils.CircleIndicator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.pager)
    ExtendedViewPager pager;
    @BindView(R.id.view_pager_indicator)
    CircleIndicator viewPagerIndicator;
    @BindView(R.id.tvChange)
    TextView tvChange;
    private ViewPagerAdapter mVpAdapter;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        ButterKnife.bind(this);

        mVpAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mVpAdapter);
        pager.setOffscreenPageLimit(4);
        pager.setOnPageChangeListener(this);

        viewPagerIndicator = (CircleIndicator) findViewById(R.id.view_pager_indicator);
        viewPagerIndicator.setViewPager(pager);

    }

    @OnClick(R.id.tvChange)
    public void onViewClicked() {
        startActivity(new Intent(GuideActivity.this, Guide2Activity.class));
        finish();
    }

    /**
     * 虾米音乐
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final int[] videoRes;
        private final int[] slogonImgRes;

        public ViewPagerAdapter(FragmentManager paramFragmentManager) {
            super(paramFragmentManager);
            this.videoRes = new int[]{R.raw.splash_1, R.raw.splash_2, R.raw.splash_3, R.raw.splash_4};
            this.slogonImgRes = new int[]{R.drawable.slogan_1, R.drawable.slogan_2, R.drawable.slogan_3, R.drawable.slogan_4};
        }

        public int getCount() {
            return this.videoRes.length;
        }

        public Fragment getItem(int paramInt) {
            VideoItemFragment videoItemFragment = new VideoItemFragment();
            Bundle localBundle = new Bundle();
            localBundle.putInt("position", paramInt);
            localBundle.putInt("videoRes", this.videoRes[paramInt]);
            localBundle.putInt("imgRes", this.slogonImgRes[paramInt]);
            videoItemFragment.setArguments(localBundle);
            if (paramInt < getCount()) {
                return videoItemFragment;
            }
            throw new RuntimeException("Position out of range. Adapter has " + getCount() + " items");
        }
    }

    public void next(int positon) {
        int i = this.pager.getCurrentItem();
        if (positon == i) {
            positon += 1;

            this.pager.setCurrentItem(positon, true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((VideoItemFragment) (mVpAdapter.getItem(position))).play();
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
                if (pager.getCurrentItem() == pager.getAdapter()
                        .getCount() - 1 && !flag) {
                    TranslateAnimation mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                            , -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAnim.setDuration(300);
                    tvChange.startAnimation(mShowAnim);
                    tvChange.setVisibility(View.VISIBLE);
                } else {
                    tvChange.setVisibility(View.GONE);
                }
                flag = true;
                break;
        }
    }

//    实现切换部分：
//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message me) {
//            switch (me.what) {
//                case 1:
//                    ChuShi(new VideoItemFragment());//加载碎片1
//                    break;
//                case 2:
//                    ChuShi(new GuideFragment());//加载碎片2
//                    break;
//            }
//        }
//    };
//    被调用的方法:

//    public void Qiehuan(int a) {
//        Message message = new Message();
//        message.what = a;
//        handler.sendMessage(message);
//    }


//    加载碎片（Fragment）部分:
//    private void ChuShi(Fragment fragment) {//创建待添加的碎片实例
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        /*获取FragmentManager,在活动中可以直接通过调用getSupportFragmentManager()方法的到*/
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                /*
//                开启一个事务，通过调用beginTransaction()方法开启。
//                 */
//        transaction.replace(R.id.pager, fragment);//向容器添加或替换碎片一般使用replace实现
//        transaction.commit();//提交事物，调用commit方法来完成/
//    }
}
