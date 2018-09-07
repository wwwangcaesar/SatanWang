package about.nocare.casaer.satanwang.ui.login;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovcreate.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.bean.login.CircleBean;
import about.nocare.casaer.satanwang.utils.chat.DisplayUtil;
import about.nocare.casaer.satanwang.utils.login.WaveViewByBezier;
import about.nocare.casaer.satanwang.widget.login.BubbleView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * & @Description:  登录  动画  页面
 * & @Author:  Satan
 * & @Time:  2018/9/7 14:28
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.circle_view)
    BubbleView circleView;
    @BindView(R.id.center_tv)
    TextView centerTv;
    @BindView(R.id.wave_bezier)
    WaveViewByBezier waveViewByBezier;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    private List<CircleBean> circleBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initPoint();
        circleView.setCircleBeen(circleBeanList);
        //开启动画
        circleView.setCenterImg(centerTv);
        circleView.openAnimation();
        circleView.setOnBubbleAnimationListener(new BubbleView.OnBubbleAnimationListener() {
            @Override
            public void onCompletedAnimationListener() {
                centerTv.setVisibility(View.GONE);
                /*下方波浪动画*/
                onFadeClick(rlLogin,waveViewByBezier, Gravity.LEFT);//从左侧划入
                waveViewByBezier.startAnimation();
            }
        });

    }

    /**
     * 设置bean数据
     */
    private void initPoint() {

        int height = DisplayUtil.getWindowHight(this);
        int width = DisplayUtil.getWindowWidth(this);

        int centerX = width / 2;
        int centerY = height / 2;


        Log.d(TAG, "initPoint: " + centerX + "----" + centerY);


        CircleBean circleBean = new CircleBean(
                new PointF((float) (-width / 5.1), (float) (height / 1.5)),
                new PointF(centerX - 30, height * 2 / 3),
                new PointF((float) (width / 2.4), (float) (height / 3.4)),
                new PointF(width / 6, centerY - 120),
                new PointF((float) (width / 7.2), -height / 128),
                (float) (width / 14.4), 60);
        CircleBean circleBean2 = new CircleBean(
                new PointF(-width / 4, (float) (height / 1.3)),
                new PointF(centerX - 20, height * 3 / 5),
                new PointF((float) (width / 2.1), (float) (height / 2.5)),
                new PointF(width / 3, centerY - 10),
                new PointF(width / 4, (float) (-height / 5.3)),
                width / 4, 60);
        CircleBean circleBean3 = new CircleBean(
                new PointF(-width / 12, (float) (height / 1.1)),
                new PointF(centerX - 100, height * 2 / 3),
                new PointF((float) (width / 3.4), height / 2),
                new PointF(0, centerY + 100),
                new PointF(0, 0),
                width / 24, 60);

        CircleBean circleBean4 = new CircleBean(
                new PointF(-width / 9, (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF((float) (width / 2.1), (float) (height / 2.3)),
                new PointF(width / 2, centerY),
                new PointF((float) (width / 1.5), (float) (-height / 5.6)),
                width / 4, 60);

        CircleBean circleBean5 = new CircleBean(
                new PointF((float) (width / 1.4), (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF(width / 2, (float) (height / 2.37)),
                new PointF(width * 10 / 13, centerY - 20),
                new PointF(width / 2, (float) (-height / 7.1)),
                width / 4, 60);
        CircleBean circleBean6 = new CircleBean(
                new PointF((float) (width / 0.8), height),
                new PointF(centerX + 20, height * 2 / 3),
                new PointF((float) (width / 1.9), (float) (height / 2.3)),
                new PointF(width * 11 / 14, centerY + 10),

                new PointF((float) (width / 1.1), (float) (-height / 6.4)),
                (float) (width / 4), 60);
        CircleBean circleBean7 = new CircleBean(
                new PointF((float) (width / 0.9), (float) (height / 1.2)),
                new PointF(centerX + 20, height * 4 / 7),
                new PointF((float) (width / 1.6), (float) (height / 1.9)),
                new PointF(width, centerY + 10),

                new PointF(width, 0),
                (float) (width / 9.6), 60);

        circleBeanList.add(circleBean);
        circleBeanList.add(circleBean2);
        circleBeanList.add(circleBean3);
        circleBeanList.add(circleBean4);
        circleBeanList.add(circleBean5);
        circleBeanList.add(circleBean6);
        circleBeanList.add(circleBean7);

    }

    @Override
    protected void onPause() {
        super.onPause();
        waveViewByBezier.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveViewByBezier.resumeAnimation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveViewByBezier.stopAnimation();
    }
}
