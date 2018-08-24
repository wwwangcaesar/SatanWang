package com.lovcreate.amap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lovcreate.core.constant.CoreConstant;

/**
 * Created by Administrator on 2016/12/14.
 * 定位服务
 */
public class LocationService extends Service implements AMapLocationListener {

    public static Service service;
    public AMapLocationClient mLocationClient = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        Log.e("LocationService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocationService", "onStartCommand");
        startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startLocation() {
        if (mLocationClient == null) {
            // 声明AMapLocationClient类对象
            // 初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            // 设置定位回调监听
            mLocationClient.setLocationListener(this);
            initLocation();
            // 启动定位
            mLocationClient.startLocation();
        }
        if (!mLocationClient.isStarted()) {
            mLocationClient.startLocation();
        }
    }

    private void initLocation() {
        // 初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mLocationOption.setGpsFirst(true);
        // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000L);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiScan(true);
        // 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        // 是否开启缓存机制
        mLocationOption.setLocationCacheEnable(true);
        // 可选，设置是否使用传感器。默认是false
        mLocationOption.setSensorEnable(true);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 可在其中解析amapLocation获取相应内容。
                Log.e("LocationService", "poiName : " + aMapLocation.getPoiName());
//                Toast.makeText(getApplicationContext(),"纬度:"+aMapLocation.getLatitude()+"经度:"+aMapLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                // 发送广播
                Intent intent = new Intent("android.intent.action.location");
                intent.putExtra("location", aMapLocation);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } else {
                // 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Intent intent = new Intent("android.intent.action.location");
                intent.putExtra("location", aMapLocation);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                startLocation();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();// 停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();// 销毁定位客户端，同时销毁本地定位服务。
        }
        mLocationClient = null;
    }

}
