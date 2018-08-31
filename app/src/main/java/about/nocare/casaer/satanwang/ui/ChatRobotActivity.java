package about.nocare.casaer.satanwang.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;

/**
 & @Description:  聊天机器人
 & @Author:  Satan
 & @Time:  2018/8/30 17:23
 */
public class ChatRobotActivity extends BaseActivity {

    PackageManager packageManager ;
    /*获取所有应用的包名*/
    private List<ResolveInfo> apps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_robot);
        StatusBarUtil.setColor(ChatRobotActivity.this,R.color.TulingToolBar);
        setTitleText("小A");
        setTitleTextColor(R.color.white);
        setLeftIcon(R.mipmap.ic_nav_back_white);
        setToolbarBackground(R.color.TulingToolBar);
        packageManager = this.getPackageManager();

        /*启动系统辅助功能*/
//        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(intent);
        /*打开美团*/
//        Intent intent = packageManager.getLaunchIntentForPackage("com.sankuai.meituan");//"jp.co.johospace.jorte"就是我们获得要启动应用的包名
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);com.lion.market
//        startActivity(intent);
        /*打开虫虫助手*/
//        Intent intent = packageManager.getLaunchIntentForPackage("com.lion.market");//"jp.co.johospace.jorte"就是我们获得要启动应用的包名
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        /*获取所有应用的包名*/
        loadApps();
    }
    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = getPackageManager().queryIntentActivities(intent, 0);
        //for循环遍历ResolveInfo对象获取包名和类名
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo info = apps.get(i);
            String packageName = info.activityInfo.packageName;
            CharSequence cls = info.activityInfo.name;
            CharSequence name = info.activityInfo.loadLabel(getPackageManager());
            Log.e("ddddddd",name+"----"+packageName+"----"+cls);
        }
    }
}
