package com.lovcreate.amap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.lovcreate.amap.service.LocationService;
import com.lovcreate.amap.util.MapUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.util.DimenUtils;

import java.util.List;


/**
 * Created by Bright on 2017/7/25 0025
 * 地图, 实现地图展示以及定位
 * <p>
 * 注意 调用此界面必须需要传入三个extra：
 * title：标题
 * setRightBtn：是否设置标题右侧的确定按钮
 * defaultHint：搜索栏默认显示的文字
 */
public class MapActivity extends BaseActivity
        implements LocationSource, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener,
                   AMap.OnMapLoadedListener, Inputtips.InputtipsListener, TextView.OnEditorActionListener {

    /**
     * 视图注入
     */
    private MapView mapView;
    private EditText addressEt;
    private ImageView location;
    private LinearLayout searchLl;

    /**
     * 高德地图所需变量定义
     */
    private AMap aMap;                                  // 定义AMap 地图对象的操作方法与接口。
    private OnLocationChangedListener mListener;        // 位置改变的监听接口。
    private UiSettings mUiSettings;                     // 设置用户界面的一个AMap，地图ui控制器
    private AMapLocation amapLocation;                  // 定位信息类。定位完成后的位置信息。
    private LatLonPoint mCenterLatLonPoint = null;      // MapView中央对于的屏幕坐标
    private GeocodeSearch geocodeSearch;                // 地理编码与逆地理编码类

    /**
     * 其他自定义地图变量
     */
    private boolean isFirstLoc = true;                  // 是否首次定位
    private double lat;                                 // 纬度
    private double lng;                                 // 经度

    /**
     * 常量定义
     */
    public static final float MAP_ZOOM = 14;            // 地图缩放等级

    private String address;                             //地址名称
    private LatLonPoint latLonPoint;                    //地址经纬度
    private String city;                                //当前定位点城市名称
    private String poiId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mapView = (MapView) findViewById(R.id.map_view);
        addressEt = (EditText) findViewById(R.id.et_task_address);
        location = (ImageView) findViewById(R.id.iv_location);
        searchLl = (LinearLayout) findViewById(R.id.search_ll);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstLoc = true;
                Toast.makeText(MapActivity.this, "正在定位中...", Toast.LENGTH_SHORT).show();
                startService(new Intent(MapActivity.this, LocationService.class));
            }
        });

        initView(savedInstanceState);
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
        // 解绑广播
        if (mItemViewListClickReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mItemViewListClickReceiver);
        }
        stopService(new Intent(MapActivity.this, LocationService.class));
    }


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
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        // 设置中心点
                        aMap.animateCamera(CameraUpdateFactory
                                .changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                        aMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), MAP_ZOOM),
                                500, null);

                        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                        mCenterLatLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                        RegeocodeQuery query = new RegeocodeQuery(mCenterLatLonPoint, 100, GeocodeSearch.AMAP);
                        geocodeSearch.getFromLocationAsyn(query);
                        startService(new Intent(MapActivity.this, LocationService.class));
                    }
                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            }
        }
    };

    private void initView(Bundle savedInstanceState) {

        address = getIntent().getStringExtra("defaultHint");
        addressEt.setHint(address);

        addressEt.setOnEditorActionListener(this);

        // 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        /**
         * 初始化地图控制器对象, 添加相应配置
         */
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setLocationSource(this);// 通过aMap对象设置定位数据源的监听
//        aMap.setOnCameraChangeListener(this);// 设置地图状态的监听接口
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置(设置为true表示启动显示定位蓝点)
        aMap.setOnMapLoadedListener(this);//地图加载监听

        /**
         * 获取地图ui控制器
         */
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);// 是否允许显示缩放按钮
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮

        /**
         * 自定义小蓝点(当前位置)样式
         */
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);// 定位的类型，设置为定位一次，且将视角移动到地图中心点
        aMap.setMyLocationStyle(myLocationStyle);

        /**
         * 地理编码与逆地理编码
         */
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        /**
         * 注册广播
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.location");
        LocalBroadcastManager.getInstance(this).registerReceiver(mItemViewListClickReceiver, intentFilter);

    }

    /**
     * 激活位置接口。
     * 定位程序将通过将此接口将主线程广播定位信息，直到用户关闭此通知。
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    /**
     * 处理定位更新的接口。
     */
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 可视范围改变时回调此方法。
     * 这个方法必须在主线程中调用。
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 用户对地图做出一系列改变地图可视区域的操作（如拖动、动画滑动、缩放）完成之后回调此方法。
     * 这个方法必须在主线程中调用。
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        lat = cameraPosition.target.latitude;
        lng = cameraPosition.target.longitude;
        mCenterLatLonPoint = new LatLonPoint(lat, lng);
    }

    /**
     * 根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
     * 逆地理编码兴趣点返回结果最大返回数目为10，道路和交叉路口返回最大数目为3。
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            if (regeocodeResult.getRegeocodeAddress().getPois().size() > 0) {
                // 获取所选位置的地址信息
                if (poiId != null && !"".equals(poiId)) {
                    for (PoiItem poiItem : regeocodeResult.getRegeocodeAddress().getPois()) {
                        if (poiId.equals(poiItem.getPoiId())) {
                            addressEt.setText("");
                            address = poiItem.getTitle();
                            poiId = "";
                        }
                    }
                } else {
                    address = regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle();
                }
                latLonPoint = regeocodeResult.getRegeocodeAddress().getPois().get(0).getLatLonPoint();
                // 显示地址信息
                addressEt.setHint(address);
                city = regeocodeResult.getRegeocodeAddress().getCity();
            }
        }
    }

    /**
     * 根据给定的地理名称和查询城市，返回地理编码的结果列表。
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//        city = geocodeResult.getGeocodeAddressList().get(0).getCity();
    }

    /**
     * 地图加载时查看是否存在已选择的经纬度
     */
    @Override
    public void onMapLoaded() {
        if (getIntent().getDoubleExtra("lat", 0) != 0 && getIntent().getDoubleExtra("lng", 0) != 0) {
            LatLng latLng = new LatLng(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("lng", 0));
            latLonPoint = new LatLonPoint(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("lng", 0));
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            //放置一个点标记
            MapUtil.addMarkerOptions(aMap, latLng, getResources(), R.drawable.ic_position_10);
            if (addressEt.getHint().toString().contains("上下班地址")) {
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 100, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            }
        } else {
            startService(new Intent(MapActivity.this, LocationService.class));
        }
    }

    @Override
    public void onGetInputtips(final List<Tip> tipList, int code) {
        /**
         * 搜索返回监听
         * 1）在回调中解析 tipList，获取输入提示词的相关信息。
         * 2）tipList 数组中的对象是 Tip ，Tip 类中包含 PoiID、Adcode、District、Name 等信息。
         */
        if (code == 1000) { // 成功
            if (tipList.size() == 0) {
                Toast.makeText(MapActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                return;
            }
            mapShowSearchMenu(searchLl, tipList, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    aMap.clear();
                    latLonPoint = tipList.get(position).getPoint();
                    address = tipList.get(position).getName();
                    poiId = tipList.get(position).getPoiID();
                    if (tipList.get(position).getTypeCode().equals("999901")) {
                        Toast.makeText(MapActivity.this, "无法选择公交路线", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 选择地址后，清除焦点
                    addressEt.clearFocus();
                    addressEt.setText("");
                    LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                    //将地图移动到点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    //放置一个点标记
                    MapUtil.addMarkerOptions(aMap, latLng, getResources(), R.drawable.ic_position_10);
                    // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
                    geocodeSearch.getFromLocationAsyn(query);
                    hineSearchMenu();
                }
            });
        } else {
            Toast.makeText(MapActivity.this, "查询失败，code:" + code, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 搜索
     * 构造 InputtipsQuery 对象，通过 InputtipsQuery(java.lang.String keyword, java.lang.String city) 设置搜索条件。
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (addressEt.getText().toString().isEmpty()) {
            return false;
        }
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                // 第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
                InputtipsQuery inputquery = new InputtipsQuery(addressEt.getText().toString(), "");
                inputquery.setCityLimit(true);
                //设置搜索的类型(POI分类编码)：主要为了去除公交路线。 详见POI分类编码表 ( http://a.amap.com/lbs/static/zip/AMap_poicode.zip )
                inputquery.setType("01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|1902|1903|1904|1905|1906|1907|20|21|22|970000|990000|991");

                // 构造 Inputtips 对象，并设置监听。
                Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
                inputTips.setInputtipsListener(this);

                // 调用 PoiSearch 的 requestInputtipsAsyn() 方法发送请求。
                inputTips.requestInputtipsAsyn();
        }
        return true;
    }

    private PopupWindow popupWindowMenu;

    public void hineSearchMenu() {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            // 关闭popupWindow
            popupWindowMenu.dismiss();
        }
    }

    /**
     * 显示标题栏右侧按钮弹框
     */
    public void mapShowSearchMenu(View view, List<Tip> popupData, AdapterView.OnItemClickListener listener) {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            // 关闭popupWindow
            popupWindowMenu.dismiss();
        }
        final View popupView = getLayoutInflater().inflate(R.layout.base_tool_bar_popup_search, null);
        popupWindowMenu = new PopupWindow(popupView, ListPopupWindow.MATCH_PARENT, ListPopupWindow.MATCH_PARENT,
                true);

        // 设置弹出的popupWindow不遮挡软键盘
        popupWindowMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindowMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置点击外部让popupWindow消失
        popupWindowMenu.setFocusable(false);// 可以试试设为false的结果
        popupWindowMenu.setOutsideTouchable(true); // 点击外部消失

        // 必须设置的选项
        popupWindowMenu.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
        popupWindowMenu.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 将window视图显示在点击按钮下面
        popupWindowMenu.showAsDropDown(view, 0, DimenUtils.px2dip(this, view.getHeight()));
        ListView listView = (ListView) popupView.findViewById(R.id.pop_listView);

        listView.setAdapter(new SearchMapAdapter(this, popupData));
        listView.setOnItemClickListener(listener);

    }
}
