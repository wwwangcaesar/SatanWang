package com.lovcreate.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.lovcreate.core.R;
import com.lovcreate.core.base.CoreUrl;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.util.ScreenAdaptation;
import com.lovcreate.core.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import static com.lovcreate.core.constant.CoreConstant.BUGLY_APP_ID;

/**
 * Created by Administrator on 2016/12/14.
 * <p>
 * 应用主体, 初始化okhttp
 * 在这里做一些第三方初始化
 */
public class CoreApplication extends Application {

    private static CoreApplication instance;

    private List<Activity> activityList = new ArrayList<>();

    private static String deviceId = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //针对Android 7 以上 FileUriExposedException : 置入一个不设防的VmPolicy,再用旧的方式直接把file://格式的URI发送出去   http://www.jianshu.com/p/68a4e8132fcd
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        ApplicationInfo applicationInfo = instance.getApplicationInfo();
        String processName = applicationInfo.processName;
        if (!processName.endsWith(".debug")) {
            /*
             * 测试地址：http://home.lovcreate.com:8029/
             * 客户地址：https://www.91car.club/hydra-app/
             */
            CoreUrl.baseUrl = "http://home.lovcreate.com:8029/";
//            CoreUrl.baseUrl = "https://www.91car.club/hydra-app/";
        }

        initApp();
        initCloudChannel(this);

        // 注册滑动返回
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // 初始化屏幕适配 需要传入ui设计给的大小
        if (height / width < 2) {
            new ScreenAdaptation(this, 750, 1334).register();
        }

        // 初始化Toast弹框
        ToastUtil.init(this);

        CrashReport.initCrashReport(getApplicationContext(), BUGLY_APP_ID, false);

    }

    /**
     * 初始化阿里推送
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                String deviceId = pushService.getDeviceId();
                Log.d("aliPush", "init cloudchannel success deviceId=" + deviceId);
                AppSession.setDeviceId(deviceId);
                CoreApplication.setDeviceId(deviceId);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("aliPush", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }

    private void initApp() {
        // http请求
        try {
            OkHttpUtils.initClient(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                    .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////// 单例控制 ///////////////////////////////////////////


    public static CoreApplication getInstance() {
        if (instance == null) {
            instance = new CoreApplication();
        }
        return instance;
    }


    //////////////////////////////////////// 全退功能 ///////////////////////////////////////////

    public List<Activity> getActivityList() {
        return activityList;
    }

    // add Activity
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId() {
        if ("".equals(deviceId)) {
            return AppSession.getDeviceId();
        } else {
            return deviceId;
        }
    }

    public static void setDeviceId(String deviceId) {
        CoreApplication.deviceId = deviceId;
    }

}
