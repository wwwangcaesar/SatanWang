package about.nocare.casaer.satanwang.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.HomeGetWelfareListAdapter;
import about.nocare.casaer.satanwang.bean.HomeCouponBean;
import about.nocare.casaer.satanwang.ui.home.HomeCityActivity;
import about.nocare.casaer.satanwang.ui.home.HomeMapActivity;
import about.nocare.casaer.satanwang.ui.home.appMore.AppMoreActivity;
import about.nocare.casaer.satanwang.utils.DensityUtil;
import about.nocare.casaer.satanwang.utils.FloatWindowUtils;
import about.nocare.casaer.satanwang.utils.GuideHelper;
import about.nocare.casaer.satanwang.utils.StatusBarCompat;
import about.nocare.casaer.satanwang.utils.TextShape;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * & @Description:   首页
 * & @Author:  Satan
 * & @Time:  2018/8/24 10:08
 */
public class MainActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {


    @BindView(R.id.tvOtherLan)
    TextView tvOtherLan;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.textShape)
    TextShape textShape;
    @BindView(R.id.vvSplash)
    VideoView vvSplash;
    @BindView(R.id.tvMap)
    TextView tvMap;
    private int mVideoPosition;
    private boolean mHasPaused;

    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;

    private float mStartX, mStartY;
    private long mDownTime, mUpTime;
    private View mView;
    private boolean isFloatViewNotAdded = true;
    private int mCanMoveHeight;
    private int mCanMoveWidth;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mBroadcastReceiver;
    /* 优惠券*/
    private List<HomeCouponBean> BeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ACTION_ADD_FLOATVIEW")) {
                    showFloatView(MainActivity.this);
                } else if (intent.getAction().equals("ACTION_REMOVE_FLOATVIEW")) {
                    removeFloatView(MainActivity.this);
                }
            }
        };
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_ADD_FLOATVIEW");
        filter.addAction("ACTION_REMOVE_FLOATVIEW");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, filter);
        //视频背景view
        mVideoPosition = 0;
        mHasPaused = false;
        vvSplash.setOnErrorListener(this);
        vvSplash.setOnPreparedListener(this);
        String url="android.resource://" + this.getPackageName() + "/" + R.raw.maycry;
        vvSplash.setVideoPath(url);
        //视频背景view
        initView();//悬浮框
        setDate();//填充数据
        initIsFirst();
        textShape.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                onGuild();
//                startActivity(new Intent(MainActivity.this, SearchActivity.class));

                startActivity(new Intent(MainActivity.this, AppMoreActivity.class));
//                startActivity(new Intent(MainActivity.this, HomeMapActivity.class));
            }
        });
        tvCity.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(MainActivity.this, HomeCityActivity.class));
            }
        });
        tvMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(MainActivity.this, HomeMapActivity.class));
            }
        });
    }

    /**
     * 悬浮框小A
     */
    private void initView() {
        wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mView = LayoutInflater.from(this).inflate(R.layout.kefu, null);

        wmParams = new WindowManager.LayoutParams(
                DensityUtil.dp2px(this, 60),
                DensityUtil.dp2px(this, 60),
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        );
        wmParams.gravity = Gravity.NO_GRAVITY;
        if (Build.VERSION.SDK_INT > 24) {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mCanMoveWidth = getResources().getDisplayMetrics().widthPixels / 2 - DensityUtil.dip2px(MainActivity.this, 26);
        mCanMoveHeight = (getResources().getDisplayMetrics().heightPixels - StatusBarCompat.getStatusBarHeight(this)) / 2 - DensityUtil.dip2px(MainActivity.this, 26);
        wmParams.x = mCanMoveWidth;
        wmParams.y = mCanMoveHeight - 250;//更改小A高度位置
        final ImageView ivDuo = (ImageView) mView.findViewById(R.id.ivDuo);
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ToastUtil.showToastBottomShort("点击悬浮窗");
            }
        });
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToastBottomShort("长按悬浮窗");

                return false;
            }
        });
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 当前值以屏幕左上角为原点
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指按下
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        mDownTime = System.currentTimeMillis();
                        ivDuo.setImageResource(R.drawable.duo_b);
                        break;

                    case MotionEvent.ACTION_MOVE://手指按下移动中
                        wmParams.x += event.getRawX() - mStartX;
                        wmParams.y += event.getRawY() - mStartY;
                        if (wmParams.y < -mCanMoveHeight) {
                            wmParams.y = -mCanMoveHeight;
                        }
                        if (wmParams.y > mCanMoveHeight) {
                            wmParams.y = mCanMoveHeight;
                        }
                        wm.updateViewLayout(mView, wmParams);
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        ivDuo.setImageResource(R.drawable.duo_b);
                        break;
                    case MotionEvent.ACTION_UP://抬起手指
                        wmParams.x = mCanMoveWidth;
                        mStartX = wmParams.x;
                        if (getResources().getDisplayMetrics().widthPixels / 2 < event.getRawX()) {//如果平移 的宽度大于二分之一屏幕，在右半边屏幕
                            ivDuo.setImageResource(R.drawable.duo_a);
                            wm.updateViewLayout(mView, wmParams);
                        } else {
                            ivDuo.setImageResource(R.drawable.duo_a);
                            wmParams.x = -mCanMoveWidth;
                            wm.updateViewLayout(mView, wmParams);
                        }
                        mUpTime = System.currentTimeMillis();
                        return mUpTime - mDownTime > 200;
                }
                // 消耗触摸事件
                return false;
            }
        });

    }

    /**
     * 判断是否是第一次运行，是否显示向导页面
     */
    private void initIsFirst() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfirst", true);
        final SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            onGuild();
            editor.putBoolean("isfirst", false);
            editor.commit();
        } else {
            //弹出广告弹框
            showTicketDialog();
        }
    }

    /**
     * 向导引导
     *
     * @param
     */
    private void onGuild() {
        final GuideHelper guideHelper = new GuideHelper(MainActivity.this);
        View test = guideHelper.inflate(R.layout.custom_view_show);
        guideHelper.addPage(new GuideHelper.TipData(test, Gravity.CENTER));

        GuideHelper.TipData tipData1 = new GuideHelper.TipData(R.drawable.tip1, Gravity.RIGHT | Gravity.BOTTOM, tvOtherLan);
        tipData1.setLocation(0, -DensityUtil.dipToPix(MainActivity.this, 30));
        guideHelper.addPage(tipData1);//侧边栏

        GuideHelper.TipData tipData2 = new GuideHelper.TipData(R.drawable.tip2, tvCity);
        guideHelper.addPage(tipData2);//切换城市

        GuideHelper.TipData tipData3 = new GuideHelper.TipData(R.drawable.tip3, textShape);

        View test1 = guideHelper.inflate(R.layout.suspension_guild_layout);
        GuideHelper.TipData tipData_1 = new GuideHelper.TipData(test1, Gravity.CENTER_HORIZONTAL);
        tipData_1.setLocation(0, -DensityUtil.dipToPix(MainActivity.this, 105));
        guideHelper.addPage(tipData_1);//悬浮框

        GuideHelper.TipData tipData4 = new GuideHelper.TipData(R.drawable.next, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        tipData4.setLocation(0, -DensityUtil.dipToPix(MainActivity.this, 100));//中间文字

        tipData4.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                guideHelper.nextPage();
            }
        });
        guideHelper.addPage(false, tipData3, tipData4);

        guideHelper.addPage(tipData1, tipData2, tipData3, tipData_1);

        //add custom view
        View testView = guideHelper.inflate(R.layout.custom_view_with_close);
        testView.findViewById(R.id.guide_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                guideHelper.dismiss();
            }
        });
        GuideHelper.TipData tipDataCustom = new GuideHelper.TipData(testView, Gravity.CENTER);
        guideHelper.addPage(false, tipDataCustom);
        guideHelper.show(false);
    }

    /**
     * 弹出广告弹框
     */
    private void showTicketDialog() {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_notice_image, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.dialog);
        mDialogBuilder.setView(view);
        final Dialog alert = mDialogBuilder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        ImageView cancel = (ImageView) view.findViewById(R.id.iv_cancel);
        ListView list = (ListView) view.findViewById(R.id.list);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_3);

        Window dialogWindow = alert.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
        }

        dialogTitle.setText("恭喜您获得" + BeanList.size() + "张优惠券");
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (TextUtils.isEmpty(AppSession.getPassword())) {//先要绑定MAinApplication，否则SP不好使
                    ToastUtil.showToastBottomShort("点击");
                } else {

                }
                alert.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                alert.dismiss();
                alert.cancel();
//                AppSession.setCarIsFirst(0);
            }
        });
        HomeGetWelfareListAdapter adapter = new HomeGetWelfareListAdapter(MainActivity.this, BeanList);
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);
    }

    /**
     * 动态修改ListView的高度
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        /*获取item数量 最大可显示3个item的高度*/
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            if (i < 2) {
                totalHeight = listItem.getMeasuredHeight() * (i + 1);
            } else {
                totalHeight = listItem.getMeasuredHeight() * 3;
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 填充数据
     */
    private void setDate() {
        List<HomeCouponBean> bean = new ArrayList<>();
        for (int i = 1; i < randomByMinMax(6, 10); i++) {
            HomeCouponBean b1 = new HomeCouponBean();
            b1.setAmount("" + randomByMinMax(1, 20));//钱数
            b1.setDiscount("" + randomByMinMax(1, 9));//打折
            b1.setCouponType("" + randomByMinMax(1, 2));//类型
            b1.setPayLimit("" + randomByMinMax(50, 100));
            b1.setScope("全网");
            String from = randomByMinMax(2018, 2019) + "/" + randomByMinMax(1, 12) + "/" + randomByMinMax(1, 30);
            String to = randomByMinMax(2019, 2020) + "/" + randomByMinMax(1, 12) + "/" + randomByMinMax(1, 31);
            b1.setExpiredDateFrom(from);
            b1.setExpiredDateTo(to);
            bean.add(b1);
        }
        BeanList.addAll(bean);
    }


    public void showFloatView(Context context) {
        if (FloatWindowUtils.checkPermission(this)) {
            if (isFloatViewNotAdded) {
                wm.addView(mView, wmParams);
                isFloatViewNotAdded = false;
            }
        } else {
            FloatWindowUtils.applyPermission(this);
        }
    }

    public void removeFloatView(Context context) {
        if (!isFloatViewNotAdded) {
            wm.removeView(mView);
            isFloatViewNotAdded = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFloatView(this);
        if (mHasPaused) {
            if (vvSplash != null) {
                vvSplash.seekTo(mVideoPosition);
                vvSplash.resume();
            }
        }
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeFloatView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        if (vvSplash != null) {
            vvSplash.stopPlayback();
        }
        return;
    }

    /**
     * 视频播放view回调
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (vvSplash != null) {
            vvSplash.requestFocus();
            vvSplash.setOnCompletionListener(this);
            vvSplash.seekTo(0);
            vvSplash.start();
        }
        return;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vvSplash != null) {
            mVideoPosition = vvSplash.getCurrentPosition();
        }
        mHasPaused = true;
    }
}
