package com.lovcreate.amap.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Bright on 2017/8/3 0003
 * 封装一些地图公共方法
 */
public class MapUtil {

    /**
     * 绘制一个点标记
     *
     * @param aMap   地图对象
     * @param latLng 点标记所在经纬度
     * @param res    绘制自定义标记的所需参数
     * @param iconId 自定义标记图片
     */
    public static Marker addMarkerOptions(AMap aMap, LatLng latLng, Resources res, int iconId) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        //markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

        //markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(res, iconId)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        //markerOption.setFlat(true);//设置marker平贴地图效果
        return aMap.addMarker(markerOption);
    }

    /**
     * 绘制一个可点击点标记
     *
     * @param aMap   地图对象
     * @param latLng 点标记所在经纬度
     */
    public static Marker addMarkerOptions(AMap aMap, LatLng latLng, View view) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
//        markerOption.title(title).snippet(snippet);

        //markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromView(view));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        //markerOption.setFlat(true);//设置marker平贴地图效果
        return aMap.addMarker(markerOption);
    }

    /**
     * 绘制一个可点击点标记
     *
     * @param aMap   地图对象
     * @param latLng 点标记所在经纬度
     */
    public static Marker addMarkerOptions(AMap aMap, LatLng latLng, Bitmap bitmap) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
//        markerOption.title(title).snippet(snippet);

        //markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        //markerOption.setFlat(true);//设置marker平贴地图效果
        return aMap.addMarker(markerOption);
    }

    /**
     * 绘制轨迹
     *
     * @param aMap       地图对象
     * @param latLngList 经纬度列表
     * @param width      轨迹宽度
     * @param context    上下文
     * @param color      轨迹线颜色
     */
    public static void addPolyline(AMap aMap, List<LatLng> latLngList, float width, Context context, int color) {
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngList).width(width).color(ContextCompat.getColor(context, color)));
    }

    /**
     * 绘制轨迹
     *
     * @param aMap       地图对象
     * @param latLngList 经纬度列表
     * @param width      轨迹宽度
     * @param resource   轨迹图片
     */
    public static void addPolyline(AMap aMap, List<LatLng> latLngList, float width, int resource) {
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngList).width(width).setCustomTexture(BitmapDescriptorFactory.fromResource(resource)));
    }

    /**
     * 定位
     */
    public static void getLocalMessage(Context context, AMapLocationListener aMapLocationListener) {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        AMapLocationClient mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位监听
        mlocationClient.setLocationListener(aMapLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

}
