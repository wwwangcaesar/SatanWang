package about.nocare.casaer.satanwang.ui.appMore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.Random;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.fragment.ImageFragment;
import about.nocare.casaer.satanwang.ui.fragment.SimpleFragment2;
import about.nocare.casaer.satanwang.ui.fragment.SimpleFragment3;
import about.nocare.casaer.satanwang.ui.fragment.SimpleFragment4;

public class AppMoreActivity extends BaseActivity {
    private ViewPager mVpHome;
    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_more);
        setStatusBar();
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_favorite, "Satan"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "图片"))
                .addItem(new BottomNavigationItem(R.drawable.ic_grade, "视频"))
                .addItem(new BottomNavigationItem(R.drawable.ic_group_work, "AR"))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mVpHome.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        mFragmentList.add(new ImageFragment());
        mFragmentList.add(new SimpleFragment2());
        mFragmentList.add(new SimpleFragment3());
        mFragmentList.add(new SimpleFragment4());

        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
                Random random = new Random();
                int color = 0xff000000 | random.nextInt(0xffffff);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        if (mFragmentList.get(position) instanceof SimpleFragment2) {
                            ((SimpleFragment2) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                    case 2:
                        if (mFragmentList.get(position) instanceof SimpleFragment3) {
                            ((SimpleFragment3) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                    case 3:
                        if (mFragmentList.get(position) instanceof SimpleFragment4) {
                            ((SimpleFragment4) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(AppMoreActivity.this, null);
    }
}
