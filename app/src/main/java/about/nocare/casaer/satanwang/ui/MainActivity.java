package about.nocare.casaer.satanwang.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.brioal.swipemenu.view.SwipeMenu;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.util.ToastUtil;
import com.lovcreate.core.widget.BottomDialog;
import com.lovcreate.core.widget.CircularImage;
import com.sum.slike.SuperLikeLayout;
import com.yalantis.ucrop.UCrop;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yancy.gallerypick.utils.FileUtils;
import com.yancy.gallerypick.utils.UCropUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.HomeGetWelfareListAdapter;
import about.nocare.casaer.satanwang.bean.HomeCouponBean;
import about.nocare.casaer.satanwang.ui.chat.ChatRobotActivity;
import about.nocare.casaer.satanwang.ui.home.HomeCityActivity;
import about.nocare.casaer.satanwang.ui.home.HomeMapActivity;
import about.nocare.casaer.satanwang.ui.home.appMore.AppMoreActivity;
import about.nocare.casaer.satanwang.ui.login.LoginActivity;
import about.nocare.casaer.satanwang.utils.DensityUtil;
import about.nocare.casaer.satanwang.utils.FloatWindowUtils;
import about.nocare.casaer.satanwang.utils.GuideHelper;
import about.nocare.casaer.satanwang.utils.PicassoImageLoader;
import about.nocare.casaer.satanwang.utils.StatusBarCompat;
import about.nocare.casaer.satanwang.utils.TextShape;
import about.nocare.casaer.satanwang.utils.fabulous.BitmapProviderFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * & @Description:   首页
 * & @Author:  Satan
 * & @Time:  2018/8/24 10:08
 */
public class MainActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {


    @BindView(R.id.tvOtherLan)
    CircularImage tvOtherLan;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.textShape)
    TextShape textShape;
    @BindView(R.id.vvSplash)
    VideoView vvSplash;
    @BindView(R.id.tvMap)
    TextView tvMap;
    @BindView(R.id.main_swipemenu)
    SwipeMenu mainSwipemenu;
    @BindView(R.id.tvbeer)
    TextView tvbeer;
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
    /* 拍照弹框*/
    private BottomDialog dialog;
    private GalleryConfig galleryConfig;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    /* 解决悬浮框点击拖动问题*/
    private boolean isclick;
    private long startTime = 0;
    private long endTime = 0;
    /* 点赞动画效果*/
    SuperLikeLayout superLikeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        superLikeLayout = (SuperLikeLayout)findViewById(R.id.super_like_layout);
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
        String url = "android.resource://" + this.getPackageName() + "/" + R.raw.maycry;
        vvSplash.setVideoPath(url);
        //视频背景view
        initView();//悬浮框
        setDate();//填充数据
        initIsFirst();
        /* 点赞动画效果*/
        setFabulous();
        tvbeer.setText(R.string.gushi);
        mainSwipemenu.setReverseChangedBlur(MainActivity.this, R.drawable.cry7, R.color.transparent);
        changeStyleCode();//更改侧滑菜单风格，第一个参数，反向动态，第二个参数，视差移动，第三个缩放动画，第4个透明动画
        textShape.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                onGuild();
//                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                startActivity(new Intent(MainActivity.this, AppMoreActivity.class));
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

        //侧滑菜单点击
        tvOtherLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(AppSession.getHeadUrl())) {
                    showCarmeaDialog();
                } else {
                    if (mainSwipemenu.isMenuShowing()) {
                        mainSwipemenu.hideMenu();
                    } else {
                        mainSwipemenu.showMenu();
                    }
                }
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
                Intent intent=new Intent(MainActivity.this,ChatRobotActivity.class);
                startActivity(intent);
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
                        isclick = false;//当按下的时候设置isclick为false
                        startTime = System.currentTimeMillis();
                        ivDuo.setImageResource(R.drawable.duo_b);
                        break;

                    case MotionEvent.ACTION_MOVE://手指按下移动中
                        isclick = true;//当按钮被移动的时候设置isclick为true
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
                        endTime = System.currentTimeMillis();
                        if ((endTime - startTime) > 0.1 * 1000L) {
                            isclick = true;
                        } else {
                            isclick = false;
                        }
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
                return isclick;
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    alert.dismiss();
                    alert.cancel();
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

    /**
     * 侧滑菜单相关
     * //更新风格代码
     */
    public void changeStyleCode() {
        int mStyleCode = 3 * 1000 + 2 * 100 + 2 * 10 + 2;
        mainSwipemenu.setStyleCode(mStyleCode);
    }

    @Override
    public void onBackPressed() {
        if (mainSwipemenu.isMenuShowing()) {
            mainSwipemenu.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    /*侧滑菜单结束*/

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
        if (TextUtils.isEmpty(AppSession.getHeadUrl())){
            tvOtherLan.setImageResource(R.mipmap.ic_user_default_big);
        }else {
            Uri uri = Uri.fromFile(new File(AppSession.getHeadUrl()));
            tvOtherLan.setImageURI(uri);
        }
        if (TextUtils.isEmpty(AppSession.getChooseCityName())) {
            tvCity.setText("城市");
        } else {
            tvCity.setText(AppSession.getChooseCityName());
        }
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

    /**
     * 拍照弹框
     */
    private void showCarmeaDialog() {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.demo_choose_pic_dialog, null);
        TextView cameraTextView = (TextView) dialogView.findViewById(R.id.cameraTextView);

        TextView photoTextView = (TextView) dialogView.findViewById(R.id.photoTextView);
        TextView cancelTextView = (TextView) dialogView.findViewById(R.id.cancelTextView);
        dialog = new BottomDialog(MainActivity.this);
        dialog.setView(dialogView);
        dialog.show();

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new PicassoImageLoader())          // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)             // 监听接口（必填）
                .provider("about.nocare.casaer.satanwang.fileprovider")     // provider (必填)   todo 这里复制的时候出现错误注意，详情介绍看朗创bee
                .multiSelect(true, 1)         // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .crop(true,true)              // 快捷开启裁剪功能：单选模式、拍照、多选模式只选一张时开启
                .isShowCamera(false)                            // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")                  // 图片存放路径
                .build();

        cameraTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                galleryConfig.getBuilder().isOpenCamera(true).build();
                showCameraAction();
                dialog.dismiss();
            }
        });
        photoTextView.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onNoDoubleClick(View v) {
                                                 galleryConfig.getBuilder().isOpenCamera(false).build();
                                                 initPermissions();
                                                 dialog.dismiss();
                                             }
                                         }
        );
        cancelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("initPermissions", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("initPermissions", "拒绝过了");
                ToastUtil.showToastBottomLong("请在 设置-应用管理 中开启此应用的储存授权。");
            } else {
                Log.i("initPermissions", "进行授权");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i("initPermissions", "不需要授权 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(List<String> photoList) {
            Uri uri = Uri.fromFile(new File(photoList.get(0)));
            AppSession.setHeadUrl(photoList.get(0));
            tvOtherLan.setImageURI(uri);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onError() {
        }
    };

    private Context mContext = null;
    private static final int REQUEST_CAMERA = 100;   // 设置拍摄照片的 REQUEST_CODE
    private File cameraTempFile;
    private File cropTempFile;
    private ArrayList<String> resultPhoto = new ArrayList<>();
    /**
     * 打开相机
     * 这里要注意，如果要使用以下方法打开相机，必须又两点需要配置：
     * 1. 在"AndroidManifest.xml"中的application标签中加入以下配置：
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * // 前面是包名，后面固定的fileprovider，这里对应new GalleryConfig时的.provider("com.example.demo.fileprovider")
     * android:authorities="com.example.demo.fileprovider"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/filepaths" />
     * </provider>
     * 2. 在res包中需要添加一个包：xml，并在里面新建文件：filepaths.xml，内容如下：
     * <?xml version="1.0" encoding="utf-8"?>
     * <resources>
     * <paths>
     * <external-path name="external" path="" />
     * <files-path name="files" path="" />
     * <cache-path name="cache" path="" />
     * </paths>
     * </resources>
     */
    private void showCameraAction() {
        mContext = MainActivity.this;
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            cameraTempFile = FileUtils.createTmpFile(MainActivity.this, galleryConfig.getFilePath());
            Uri imageUri = FileProvider.getUriForFile(mContext, galleryConfig.getProvider(), cameraTempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mContext.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(mContext, com.yancy.gallerypick.R.string.gallery_msg_no_camera, Toast.LENGTH_SHORT).show();
            galleryConfig.getIHandlerCallBack().onError();
        }
    }

    /**
     *  点赞动画效果
     */
    private void setFabulous(){

        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
        findViewById(R.id.like_btn).setOnClickListener(new View.OnClickListener() {
            long duration = 2000;
            long lastClickTime;
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - lastClickTime > duration){ // 防抖
                    v.setSelected(!v.isSelected());
                }
                lastClickTime = System.currentTimeMillis();
                if(v.isSelected()){
                    int x = (int)(v.getX() + v.getWidth() / 2);
                    int y = (int)(v.getY() + v.getHeight() / 2);
                    superLikeLayout.launch(x, y);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (cameraTempFile != null) {
                    if (galleryConfig.isCrop()) {
                        resultPhoto.clear();
                        cropTempFile = FileUtils.getCorpFile(galleryConfig.getFilePath());
                        UCropUtils.start(MainActivity.this, cameraTempFile, cropTempFile,
                                galleryConfig.getAspectRatioX(), galleryConfig.getAspectRatioY(),
                                galleryConfig.getMaxWidth(), galleryConfig.getMaxHeight(), galleryConfig.isFreeCrop());
                        return;
                    }
                    resultPhoto.add(cameraTempFile.getAbsolutePath());

                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilePath(mContext) + galleryConfig.getFilePath()));
                    intent.setData(uri);
                    sendBroadcast(intent);

                    iHandlerCallBack.onSuccess(resultPhoto);
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultPhoto.clear();
            resultPhoto.add(cropTempFile.getAbsolutePath());
            iHandlerCallBack.onSuccess(resultPhoto);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            galleryConfig.getIHandlerCallBack().onError();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
