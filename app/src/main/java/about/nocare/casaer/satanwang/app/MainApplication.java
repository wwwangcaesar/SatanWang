package about.nocare.casaer.satanwang.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.lovcreate.core.app.CoreApplication;
import com.mob.MobSDK;


/**
*         app 初始化
* @author Satan Wang
* created at 2018/7/11 10:06
*/
public class MainApplication extends CoreApplication {
    private static Context mContext;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 预先加载一级列表所有城市的数据
         */
//        CityListLoader.getInstance().loadCityData(getContext());
        // 注册分享功能
        //需要两个依赖包，MobTools 和 MobCommons
        MobSDK.init(this);
//        UMShareAPI.get(this);
        mContext = getApplicationContext();
    }
    public static Context getContext() {
        return mContext;
    }

}
