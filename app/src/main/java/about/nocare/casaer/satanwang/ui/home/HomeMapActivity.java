package about.nocare.casaer.satanwang.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.lovcreate.amap.service.LocationService;
import com.lovcreate.amap.util.MapUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.constant.MapBean;
import butterknife.BindView;
import butterknife.ButterKnife;

import static about.nocare.casaer.satanwang.constant.AppConstant.DEFAULT_CITY;
import static about.nocare.casaer.satanwang.constant.AppConstant.DEFAULT_CITY_CODE;
import static about.nocare.casaer.satanwang.constant.AppConstant.DEFAULT_LAT_LNG;

/**
 * 地图
 *
 * @author Satan Wang
 *         created at 2018/7/12 10:14
 */
public class HomeMapActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, LocationSource {
    MapView mapView;
    ImageView backImageView;
    @BindView(R.id.isEnglish)
    CheckBox isEnglish;
    @BindView(R.id.isChinese)
    CheckBox isChinese;
    @BindView(R.id.tv_msg_time)
    TextView tvMsgTime;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.ll_msg)
    LinearLayout llMsg;
    @BindView(R.id.iv_point)
    ImageView ivPoint;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tvAdress)
    TextView tvAdress;

    /**
     * 高德地图所需变量定义
     */
    private AMap aMap;                                                  // 定义AMap 地图对象的操作方法与接口。
    private OnLocationChangedListener mListener;         // 位置改变的监听接口。
    private UiSettings mUiSettings;                                     // 设置用户界面的一个AMap，地图ui控制器
    private AMapLocation amapLocation;                                  // 定位信息类。定位完成后的位置信息。

    /**
     * 其他自定义地图变量
     */
    private boolean isFirstLoc = true;                  // 是否首次定位

    /**
     * 常量定义
     */
    public static final float MAP_ZOOM = 15;            // 地图缩放等级

    private List<Marker> markers = new ArrayList<>();

    private DistanceSearch distanceSearch;

    /**
     * 定位点外圈
     */
    private Circle ac;
    private Circle c;
    private long start;
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private final Interpolator interpolator1 = new LinearInterpolator();

    private MyLocationStyle myLocationStyle;
    private Marker marker;


    private int islanguage = 1;
    /**
     * 周边检索
     */
    // MapView中央对于的屏幕坐标
    private LatLonPoint mCenterLatLonPoint = null;
    private GeocodeSearch geocoderSearch;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initDistanceSearch();//距离测量

    }

    /**
     * 初始化地图
     */
    private void initView(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapView);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        // 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        // 初始化地图控制器对象, 添加相应配置

        initMap();

        initEvent();

    }

    /**
     * 重新加载地图控件
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setLocationSource(locationSource);// 通过aMap对象设置定位数据源的监听
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置(设置为true表示启动显示定位蓝点)
        aMap.setOnMapLoadedListener(onMapLoadedListener);//地图加载监听
        aMap.setOnMarkerClickListener(onMarkerClickListener);//点标记点击事件
        aMap.setOnCameraChangeListener(this);//地图上的中心点移动监听,用于Geocode搜索周围地点
        /*  MAP_TYPE_NAVI  导航地图
            MAP_TYPE_NIGHT夜景地图
            MAP_TYPE_NORMAL白昼地图（即普通地图）
            MAP_TYPE_SATELLITE卫星图*/

        /*这个属性党务了click事件切换英文底图导致第一次切换不好使*/
//        aMap.setMapType(MAP_TYPE_NAVI);//设置底图类型（包括卫星图、白昼地图（即最常见的黄白色地图）、夜景地图、导航地图、路况图层。）
        /*这个属性党务了click事件切换英文底图导致第一次切换不好使*/


        // 获取地图ui控制器
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);// 是否允许显示缩放按钮
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮

        /**
         * 自定义小蓝点(当前位置)样式
         */
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(com.lovcreate.amap.R.drawable.gps_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);// 定位的类型，设置为定位一次，且将视角移动到地图中心点
        aMap.setMyLocationStyle(myLocationStyle);
        if (AppSession.getIsFlag()){
            if (AppSession.getChooseCityName().equals("北京")){
                aMap.animateCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(MapBean.bejing.latitude, MapBean.bejing.longitude)));
                aMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(MapBean.bejing.latitude, MapBean.bejing.longitude), MAP_ZOOM),
                        500, null);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            }else if (AppSession.getChooseCityName().equals("上海")){
                aMap.animateCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(MapBean.shanghai.latitude, MapBean.shanghai.longitude)));
                aMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(MapBean.shanghai.latitude, MapBean.shanghai.longitude), MAP_ZOOM),
                        500, null);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            }
            else if (AppSession.getChooseCityName().equals("广州")){
                aMap.animateCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(MapBean.guangzhou.latitude, MapBean.guangzhou.longitude)));
                aMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(MapBean.guangzhou.latitude, MapBean.guangzhou.longitude), MAP_ZOOM),
                        500, null);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            }
            else if (AppSession.getChooseCityName().equals("深圳")){
                aMap.animateCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(MapBean.shenzhen.latitude, MapBean.shenzhen.longitude)));
                aMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(MapBean.shenzhen.latitude, MapBean.shenzhen.longitude), MAP_ZOOM),
                        500, null);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            }
            else if (AppSession.getChooseCityName().equals("杭州")){
                aMap.animateCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(MapBean.hangzhou.latitude, MapBean.hangzhou.longitude)));
                aMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(MapBean.hangzhou.latitude, MapBean.hangzhou.longitude), MAP_ZOOM),
                        500, null);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            }
        }else {
            // 注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.location");
            LocalBroadcastManager.getInstance(this).registerReceiver(mItemViewListClickReceiver, intentFilter);
        }

        tvMsgTime.setText("提示");
        tvMsg.setText("周围地点");

        //Geocode 搜索监听添加
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 设置定位
     */
    LocationSource locationSource = new LocationSource() {
        /**
         * 激活定位。
         * 定位程序将通过将此接口将主线程广播定位信息，直到用户关闭此通知。
         */
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
        }

        /**
         * 停止定位
         */
        @Override
        public void deactivate() {
            mListener = null;
        }
    };

    /**
     * 地图加载监听
     */
    AMap.OnMapLoadedListener onMapLoadedListener = new AMap.OnMapLoadedListener() {
        @Override
        public void onMapLoaded() {
            //定位服务已在map 依赖包中注册
            startService(new Intent(HomeMapActivity.this, LocationService.class));
        }
    };

    /**
     * 点标记点击事件
     */
    AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            for (Marker m : markers) {
                if (m.getId().equals(marker.getId())) {
                    Intent intent = new Intent(HomeMapActivity.this, SearchActivity.class);
                    intent.putExtra("stationId", m.getTitle());
                    startActivity(intent);
                }
            }
            return true;
        }
    };
    /**
     * 监听地图定位
     */
    BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            amapLocation = intent.getParcelableExtra("location");
                if (mListener != null && amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0 && aMap != null) {
                        amapLocation.setAccuracy(0);
                        mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                        // 设置中心点
                        if (isFirstLoc) {
                            aMap.animateCamera(CameraUpdateFactory
                                    .changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                            aMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), MAP_ZOOM),
                                    500, null);
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

                        }
                    } else {
                        String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                        Log.e("AMapErr", errText);

                        // 定位失败
                        amapLocation.setCityCode(DEFAULT_CITY_CODE);
                        amapLocation.setLatitude(DEFAULT_LAT_LNG.latitude);
                        amapLocation.setLongitude(DEFAULT_LAT_LNG.longitude);
                        amapLocation.setCity(DEFAULT_CITY);
                    }
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        // 获取车检站列表
                        setMoreMarker();
//                    addLocationMarker(amapLocation);
                    }
                }

        }
    };

    /**
     * 设置周围范围Marker
     */
    private void setMoreMarker() {
        //  43.823192
        //  125.309576
        for (int i = 1; i < 6; i++) {
            MarkerOptions markerOption = new MarkerOptions();
            Marker marker = MapUtil.addMarkerOptions(aMap, new LatLng(amapLocation.getLatitude() - Math.random(), amapLocation.getLongitude() - Math.random()), getResources(), R.mipmap.ic_map_ziying);
            markerOption.title("satan").snippet("satan：wang");
            markerOption.draggable(true);//设置Marker可拖动
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            // SDK V4.0.0 版本起，SDK 提供了给 Marker 设置动画的方法，具体实现方法如下：
            Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 360, 0, 0, 0);
            long duration = 1000L;
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
            marker.setAnimation(animation);
            marker.startAnimation();
            markers.add(marker);
        }
        for (int i = 1; i < 4; i++) {
            MarkerOptions markerOption = new MarkerOptions();
            Marker marker = MapUtil.addMarkerOptions(aMap, new LatLng(amapLocation.getLatitude() - randomDoublemin(), amapLocation.getLongitude() - randomDoublemax()), getResources(), R.mipmap.ic_map_feiziying);
            markerOption.title("satan").snippet("satan：wang");
            markerOption.draggable(true);//设置Marker可拖动
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            // SDK V4.0.0 版本起，SDK 提供了给 Marker 设置动画的方法，具体实现方法如下：
            Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 360, 0, 0, 0);
            long duration = 1000L;
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
            marker.setAnimation(animation);
            marker.startAnimation();
            markers.add(marker);
        }
        MarkerOptions markerOption = new MarkerOptions();
        Marker marker = MapUtil.addMarkerOptions(aMap, new LatLng(amapLocation.getLatitude() + 0.0009, amapLocation.getLongitude() + 0.0009), getResources(), R.mipmap.ic_map_feiziying);
        markerOption.title("satan").snippet("satan：wang");
        markerOption.draggable(true);//设置Marker可拖动
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        // SDK V4.0.0 版本起，SDK 提供了给 Marker 设置动画的方法，具体实现方法如下：
        Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 360, 0, 0, 0);
        long duration = 1000L;
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        marker.setAnimation(animation);
        marker.startAnimation();
        markers.add(marker);

    }

    /**
     * 距离测量
     */
    private void initDistanceSearch() {
        distanceSearch = new DistanceSearch(this);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                if (i == 1000) {
                    float lu = distanceResult.getDistanceResults().get(0).getDuration();
                    float lu1 = distanceResult.getDistanceResults().get(0).getDistance();
                }
            }
        });
        LatLonPoint start = new LatLonPoint(39.90403, 116.407525);
        LatLonPoint start1 = new LatLonPoint(39.90000, 116.407525);
        LatLonPoint start2 = new LatLonPoint(38.540103, 76.978787);
        LatLonPoint start3 = new LatLonPoint(10.90000, 116.407525);
        LatLonPoint dest = new LatLonPoint(39.90455, 116.407555);

        //设置起点和终点，其中起点支持多个
        List<LatLonPoint> latLonPoints = new ArrayList<LatLonPoint>();
        latLonPoints.add(start);
        latLonPoints.add(start1);
        latLonPoints.add(start2);
        latLonPoints.add(start3);
        DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(dest);
        //设置测量方式，支持直线和驾车
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
    }


    /**
     * 定位点周围外圈扩展
     */
    private void addLocationMarker(AMapLocation aMapLocation) {
        LatLng mylocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        float accuracy = aMapLocation.getAccuracy();

        if (marker == null) {
            Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.gps_point);
            BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
            marker = aMap.addMarker(new MarkerOptions().position(mylocation).icon(des)
                    .anchor(0.1f, 0.1f));
//           myLocationStyle.myLocationIcon(des);

            ac = aMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(100, 255, 218, 185)).radius(accuracy)
                    .strokeColor(Color.argb(255, 255, 228, 185)).strokeWidth(5));
            c = aMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(70, 255, 218, 185))
                    .radius(accuracy).strokeColor(Color.argb(255, 255, 228, 185))
                    .strokeWidth(0));
        } else {
            marker.setPosition(mylocation);
            ac.setCenter(mylocation);
            ac.setRadius(accuracy);
            c.setCenter(mylocation);
            c.setRadius(accuracy);
        }
        Scalecircle(c);
    }

    private Marker addMarker(LatLng point) {
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_map_feiziying);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        Marker marker = aMap.addMarker(new MarkerOptions().position(point).icon(des)
                .anchor(0.5f, 0.5f));
        return marker;
    }


    public void Scalecircle(final Circle circle) {
        start = SystemClock.uptimeMillis();
        mTimerTask = new circleTask(circle, 1000);
        mTimer.schedule(mTimerTask, 0, 30);
    }


    /**
     * AMap.OnCameraChangeListener 的回调接口方法
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        tvMsgTime.setText("提示");
        tvMsg.setText("猜一猜");
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        double weidu = cameraPosition.target.latitude;
        double jingdu = cameraPosition.target.longitude;
        mCenterLatLonPoint = new LatLonPoint(weidu, jingdu);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(mCenterLatLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * GeocodeSearch.OnGeocodeSearchListener 的回调接口方法
     *
     * @param regeocodeResult
     * @param i
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            if (regeocodeResult.getRegeocodeAddress().getPois().size() > 0) {
                // 获取周边兴趣点
                if (islanguage == 1) {//中文
                    ServiceSettings.getInstance().setLanguage(ServiceSettings.CHINESE);//默认是中文ServiceSettings.CHINESE
                    tvMsg.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                    tvAdress.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                } else {
                    ServiceSettings.getInstance().setLanguage(ServiceSettings.ENGLISH);//默认是中文ServiceSettings.CHINESE
                    tvMsg.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                    tvAdress.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                }
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * LocationSource 的回调接口方法
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }


    private class circleTask extends TimerTask {
        private double r;
        private Circle circle;
        private long duration = 1000;

        public circleTask(Circle circle, long rate) {
            this.circle = circle;
            this.r = circle.getRadius();
            if (rate > 0) {
                this.duration = rate;
            }
        }

        @Override
        public void run() {
            try {
                long elapsed = SystemClock.uptimeMillis() - start;
                float input = (float) elapsed / duration;
//                外圈循环缩放
//                float t = interpolator.getInterpolation((float)(input-0.25));//return (float)(Math.sin(2 * mCycles * Math.PI * input))
//                double r1 = (t + 2) * r;
//                外圈放大后消失
                float t = interpolator1.getInterpolation(input);
                double r1 = (t + 1) * r;
                circle.setRadius(r1);
                if (input > 2) {
                    start = SystemClock.uptimeMillis();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        try {
            mTimer.cancel();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 解绑广播
        if (mItemViewListClickReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mItemViewListClickReceiver);
        }
        stopService(new Intent(HomeMapActivity.this, LocationService.class));
    }

    //处理点击事件
    private void initEvent() {
        //退出ac
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //切换英文底图
        isEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aMap.reloadMap();
                    aMap.setMapLanguage(AMap.ENGLISH);
                    isChinese.setClickable(false);
                    islanguage = 2;
                    tvMsg.setText("加载中");
                    tvAdress.setText("加载中");
                } else {
                    isChinese.setClickable(true);
                }
            }
        });
        //切换中文底图
        isChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aMap.reloadMap();
                    aMap.setMapLanguage(AMap.CHINESE);
                    isEnglish.setClickable(false);
                    islanguage = 1;
                    tvMsg.setText("加载中");
                    tvAdress.setText("加载中");
                } else {
                    isEnglish.setClickable(true);
                }

            }
        });
        //回到当前定位
        ivLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                isFirstLoc = true;
                aMap.clear();//清除地图上的marker
                startService(new Intent(HomeMapActivity.this, LocationService.class));
            }
        });
    }
}
