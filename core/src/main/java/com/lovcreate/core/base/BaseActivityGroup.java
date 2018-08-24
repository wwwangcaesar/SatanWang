package com.lovcreate.core.base;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovcreate.core.R;
import com.lovcreate.core.app.CoreApplication;
import com.lovcreate.core.util.Logcat;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 用于SuperWebView的基础类
 * Created by Albert.Ma on 2017/10/30 0030.
 */

public class BaseActivityGroup extends ActivityGroup {

    private PermissionListener mlistener;

    /**
     * 右侧的菜单list
     */
    private PopupWindow popupWindowMenu;
    private final static String TAG = "BaseActivityGroup";
    protected CoreApplication coreApplication;
    protected Context baseContext;//上下文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logcat.i(this.toString() + " - ==> onCreate...");
        baseContext = this;
        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        }
        // activity管理
        coreApplication = (CoreApplication) getApplication();
        coreApplication.getActivityList().add(this);
    }

    /**
     * 重写setContentView(int)方法, 添加初始化ButterKnife
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 设置默认标题
        setBaseToolbar();
        Logcat.i(this.toString() + " - ==> onResume...");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Logcat.i(this.toString() + " - ==> onPause...");
    }


    /**
     * finish方法添加动画效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    /**
     * 重写onStop()方法, 取消Glide加载图片请求
     */
    @Override
    protected void onStop() {
        super.onStop();
        Logcat.i(this.toString() + " - ==> onStop...");
        if (Glide.with(this) != null) {
            Glide.with(this).pauseRequests();
        }
    }

    /**
     * 重写OnDestroy()方法, 销毁时收起软键盘
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logcat.i(this.toString() + " - ==> onDestroy...");
        coreApplication.getActivityList().remove(this);
        hideSoftInput();
    }


    /**
     * 收起键盘
     */
    public void hideSoftInput() {
        try {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.i(TAG, "----" + e.toString());
        }

    }

    /**
     * 设置全局标题样式方法.放在Base的onResume方法中
     */
    public void setBaseToolbar() {
        setToolbarBackground(R.color.appBar);
        setTitleTextColor(R.color.white);
        setLeftOnClickFinish();
        setBoldTitle();
    }

    /**
     * 设置 标题加粗
     */
    public void setBoldTitle() {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return;
        }
        baseTitle.getPaint().setFakeBoldText(true);
    }

    /**
     * 设置标题文字颜色
     *
     * @param titleTextColor R.color.xxx
     * @return this
     */
    public void setTitleTextColor(int titleTextColor) {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return;
        }
        if (titleTextColor != 0) {
            baseTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), titleTextColor));
        }
    }

    /**
     * 设置标题栏背景色
     *
     * @param backgroundColor R.color.xxx
     * @return this
     */
    public void setToolbarBackground(int backgroundColor) {
        FrameLayout toolbarLayout = (FrameLayout) findViewById(R.id.toolbarLayout);
        if (toolbarLayout != null) {
            toolbarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), backgroundColor));
        }
    }

    /**
     * 设置左侧图标
     *
     * @param leftIcon R.mipmap.xxx
     * @return this
     */
    public void setLeftIcon(int leftIcon) {
        TextView baseLeftTextView = (TextView) findViewById(R.id.baseLeftTextView);
        if (null == baseLeftTextView) {
            return;
        }
        if (leftIcon != 0) {
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), leftIcon);
            // 设置边界
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            // 画在左边
            baseLeftTextView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /**
     * 设置左侧点击返回
     *
     * @return this
     */
    public void setLeftOnClickFinish() {
        RelativeLayout baseLeftToolbar = (RelativeLayout) findViewById(R.id.baseLeftToolbar);
        if (null == baseLeftToolbar) {
            return;
        }
        baseLeftToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置标题文字
     *
     * @return this
     */
    public void setTitleText(String titleText) {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return;
        }
        if (titleText != null && !titleText.isEmpty()) {
            baseTitle.setText(titleText);
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void hineSearchMenu() {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            // 关闭popupWindow
            popupWindowMenu.dismiss();
        }
    }

    /**
     * 显示标题栏右侧按钮弹框
     */
    public void showSearchMenu(View view, List<String> popupData, AdapterView.OnItemClickListener listener) {
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
        popupWindowMenu.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent));
        popupWindowMenu.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 将window视图显示在点击按钮下面
        popupWindowMenu.showAsDropDown(view, 0, 0);
        ListView listView = (ListView) popupView.findViewById(R.id.pop_listView);
        listView.setAdapter(new MoreBaseAdapter<String>(popupData, R.layout.base_tool_bar_popup_search_item) {
            @Override
            public void bindView(ViewHolder holder, String obj) {
                holder.setText(R.id.tv_item_content, obj);
            }
        });
        listView.setOnItemClickListener(listener);

    }

    /** ------------------------------------  动态获取权限 http://www.jianshu.com/p/c6cb758cbb43 ---------------------------------------- **/

    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        this.mlistener = listener;

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else {  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {  //用户拒绝权限为空
                        mlistener.onGranted();
                    } else {  //不为空
                        //拒绝授权
                        mlistener.onDenied(deniedPermissions);
                        //授权部分
                        mlistener.onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }
}
