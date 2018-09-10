package about.nocare.casaer.satanwang.ui.login;

import android.animation.Animator;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovcreate.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.bean.login.CircleBean;
import about.nocare.casaer.satanwang.utils.chat.DisplayUtil;
import about.nocare.casaer.satanwang.utils.login.WaveViewByBezier;
import about.nocare.casaer.satanwang.utils.login.WaveViewBySinCos;
import about.nocare.casaer.satanwang.widget.login.AnimationButton;
import about.nocare.casaer.satanwang.widget.login.BubbleView;
import about.nocare.casaer.satanwang.widget.login.DragBallView;
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
    FrameLayout rlLogin;
    @BindView(R.id.wave_sincos)
    WaveViewBySinCos waveSincos;
    @BindView(R.id.vflag)
    View vflag;
    @BindView(R.id.bt)
    AnimationButton bt;
    @BindView(R.id.ivFeel)
    ImageView ivFeel;
    @BindView(R.id.drag_ball_view)
    DragBallView dragBallView;
    private List<CircleBean> circleBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initPoint();//设置bean数据
        initView();
        initEnvt();//点击事件
    }

    /**
     * 初始化控件
     */
    private void initView() {
        circleView.setCircleBeen(circleBeanList);
        //开启动画
        circleView.setCenterImg(centerTv);
        circleView.openAnimation();
        dragBallView.setMsgCount(100);
    }

    /**
     * 初始化点击事件
     */
    private void initEnvt() {
        circleView.setOnBubbleAnimationListener(new BubbleView.OnBubbleAnimationListener() {
            @Override
            public void onCompletedAnimationListener() {
                centerTv.setVisibility(View.GONE);
                bt.setVisibility(View.VISIBLE);
                dragBallView.setVisibility(View.VISIBLE);
                /*下方波浪动画*/
                onFadeClick(rlLogin, waveViewByBezier, Gravity.LEFT);//从左侧划入
                onFadeClick(rlLogin, waveSincos, Gravity.LEFT);//从左侧划入
                waveViewByBezier.startAnimation();
            }
        });
        bt.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {
                bt.start();
            }

            @Override
            public void animationFinish() {
                bt.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(rlLogin,
                        new ChangeBounds().setDuration(4000).addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                waveViewByBezier.setVisibility(View.GONE);
                                dragBallView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        }));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) waveViewByBezier.getLayoutParams();
                params.gravity = true ? Gravity.CENTER_VERTICAL : Gravity.BOTTOM;
                waveViewByBezier.setLayoutParams(params);


                TransitionManager.beginDelayedTransition(rlLogin,
                        new ChangeBounds().setDuration(4000).addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                waveSincos.setVisibility(View.GONE);
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        }));
                FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) waveSincos.getLayoutParams();
                params1.gravity = true ? Gravity.CENTER_VERTICAL : Gravity.TOP;//第一个Gravity是要运动到的位置，第二个参数是原来的位置
                waveSincos.setLayoutParams(params1);
                Exposeanimation();
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

    /**
     * 揭露动画图片
     */
    private void Exposeanimation() {
        final int centerX = 0;
        final int centerY = 0;
        final float radius = (float) Math.hypot(ivFeel.getWidth(), ivFeel.getHeight());

        if (ivFeel.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(ivFeel, centerX, centerY, radius, 0);
                animator.setDuration(6000);
                animator.start();
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(ivFeel, centerX, centerY, 0, radius);
                animator.setDuration(6500);
                ivFeel.setVisibility(View.VISIBLE);
                animator.start();
            }
        }
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
