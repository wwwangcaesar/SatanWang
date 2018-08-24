package about.nocare.casaer.satanwang.constant;

import com.amap.api.maps.model.LatLng;

/**
 * 项目常量
 * <p>
 * Create By Bright on 2017-12-25 11:00:20
 */
public class AppConstant {

    public static final String DEVICE_TYPE = "1";// 设备标识 1:android 2:ios

    public static final String systemType = "1";//系统类型

    /**
     * 是否
     */
    public static final String TRUE = "1";
    public static final String FALSE = "0";

    /**
     * 默认经纬度 - 天安门
     */
    public static final LatLng DEFAULT_LAT_LNG = new LatLng(39.908692, 116.397477);
    public static final String DEFAULT_CITY_CODE = "010";
    public static final String DEFAULT_CITY = "北京市";
    public static final String DEFAULT_AD_CODE = "110100";

    /**
     * 排序类型 - 智能排序
     */
    public static final String SORT_AI_SORT = "AISort";
    /**
     * 排序类型 - 离我最近
     */
    public static final String SORT_NEAREST = "Nearest";
    /**
     * 排序类型 - 销量最高
     */
    public static final String SORT_TOP_SELLER = "TopSeller";

    /**
     * 地区筛选 - 全市
     */
    public static final String SEARCH_TYPE_CITY = "City";
    /**
     * 地区筛选 - 附近
     */
    public static final String SEARCH_TYPE_DISTANCE = "Distance";
    /**
     * 地区筛选 - 区域
     */
    public static final String SEARCH_TYPE_DISTRICT = "District";
    //车检订单状态
    /**
     * 待付款
     */
    public static final String UNPAY = "UnPay";
    /**
     * 待车检
     */
    public static final String WAIT = "Wait";
    /**
     * 已车检
     */
    public static final String DONE = "Done";
    /**
     * 已取消
     */
    public static final String CANCEL = "Cancel";
    /**
     * 退款中
     */
    public static final String CANCEL_WAIT = "Refund_Wait";
    /**
     * 退款成功
     */
    public static final String CANCEL_SUCCESS = "Refund_Success";
    /**
     * 退款失败
     */
    public static final String CANCEL_FAIL = "Refund_Fail";

    public static final String PRICE = "¥";

    /**
     * 支付方式
     */
    public static final String NONE = "None";

    public static final String ALIPAY = "AliPay";

    public static final String WXPAY = "WxPay";

    public static final String STATION = "Station";
    /**
     * 添加车辆
     */
    public static final String ADD_VEHICLE = "add";
    /**
     * 安检
     */
    public static final String SAFETY_INSPECT = "0";
    /**
     * 环检
     */
    public static final String EMISSIONS_INSPECT = "1";
    /**
     * 综检
     */
    public static final String COMPREHENSIVE_INSPECT = "2";

    /**
     * 非运营
     */
    public static final String NOT_COMMERIAL = "0";
    /**
     * 运营
     */
    public static final String COMMERIAL = "1";

    /**
     * 推送消息分类 0：首页活动 1：车检消息 2：系统消息 3:首页弹出的用户专属活动图片 4:首页弹出的车友会活动图片
     */
    public static final String MESSAGE_TYPE_ACTIVITY = "0";
    public static final String MESSAGE_TYPE_CHECK = "1";
    public static final String MESSAGE_TYPE_SYSTEM = "2";
    public static final String MESSAGE_TYPE_HOME_ACTIVITY = "3";
    public static final String MESSAGE_TYPE_HOME_CLUB = "4";

    public static final String PAGE_SIZE = "20";

    public static String TRADE_NO = "";

    public static String ORDER_TYPE = "";

    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String BUY_ORDER = "BUY_ORDER";

    /**
     * 是否买单成功，页面刷新使用
     */
    public static boolean BUY_ORDER_SUCCESS = false;

}
