package com.lovcreate.core.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.gyf.barlibrary.ImmersionBar;
import com.lovcreate.core.R;
import com.lovcreate.core.app.CoreApplication;
import com.lovcreate.core.util.DimenUtils;
import com.lovcreate.core.util.Logcat;
import com.lovcreate.core.util.StringUtil;
import com.lovcreate.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.ButterKnife;

/**
 * 作者：yuanYe创建于2016/10/12
 * QQ：962851730
 * <p>
 * 修改：Bright Lee
 * 修改内容：重新封装标题设置方法
 * <p>
 * 使用标题栏方法：
 * 1.在布局文件中引入标题栏:<include layout="@layout/base_tool_bar"/>
 * 2.使用setXXX方法可以对标题栏信息进行设置:例如：setTitleText("注册");
 * <p>
 * 其他方法：
 * 1.收起软键盘：hideSoftInput();
 * 2.隐藏标题栏右侧视图(动态需求)：hideRightMenu();
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PermissionListener mlistener;
    public static boolean isActive = true;//推送状态判断
    public static final int REQUEST_LOGIN_CODE = 500;
    protected Context mContext;
    /**
     * 右侧的菜单list
     */
    private PopupWindow popupWindowMenu;
    private final static String TAG = "BaseActivity";
    protected CoreApplication coreApplication;
    protected Context baseContext;//上下文

    protected static List<Activity> activityList = new ArrayList<>();
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        // 状态栏沉浸，4.4+生效 <<<<<<<<<<<<<<<<<

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        // 状态栏颜色更改，在activtiy中也一样调用，下面3句话就可以
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.app_main_color);//状态背景色，可传drawable资源

        // 状态栏沉浸，4.4+生效 >>>>>>>>>>>>>>>>>

        // activity管理
        coreApplication = (CoreApplication) getApplication();
        coreApplication.getActivityList().add(this);
        mContext = this;

        mImmersionBar = ImmersionBar.with(this)
                .statusBarColor(R.color.transparentBackground)// 如果使用了fitsSystemWindows，必须设置背景颜色
                .statusBarDarkFont(true);// 设置状态栏文字颜色为暗色
        mImmersionBar.init();   //所有子类都将继承这些相同的属性
    }

    /**
     * 页面点击监听
     * 如果需要隐藏键盘和输入框焦点，则对其进行处理
     */
   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (InputUtil.isHideInput(view, ev)) {
                hideSoftInput();
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/

    /**
     * 重写setContentView(int)方法, 添加初始化ButterKnife
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void setContentViewWithoutButterKnife(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logcat.i(this.toString() + " - ==> onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置默认标题
//        setBaseToolbar();
        Logcat.i(this.toString() + " - ==> onResume...");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Logcat.i(this.toString() + " - ==> onPause...");
    }

    /**
     *  推送状态判断
     * @return
     */
    public boolean isAppOnFreground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        String curPackageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> app = am.getRunningAppProcesses();
        if (app == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo a : app) {
            if (a.processName.equals(curPackageName) && a.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
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
        if (!isAppOnFreground()) {
            isActive = false;
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
        mContext = null;
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    ///////////////////////////////////// 标题栏 ///////////////////////////////////////////////

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
     * 隐藏标题下方横线
     */
    public void hideToolbarBottomLine() {
        View toolbarBottomLine = findViewById(R.id.toolbarBottomLine);
        if (toolbarBottomLine != null) {
            toolbarBottomLine.setVisibility(View.GONE);
        }
    }

    public BaseActivity setLeftText(String leftText) {
        TextView baseLeftTextView = (TextView) findViewById(R.id.baseLeftTextView);
        if (leftText != null && !leftText.isEmpty()) {
            baseLeftTextView.setText(leftText);
        }
        return this;
    }

    /**
     * 设置左侧图标
     *
     * @param leftIcon R.mipmap.xxx
     * @return this
     */
    public BaseActivity setLeftIcon(int leftIcon) {
        TextView baseLeftTextView = (TextView) findViewById(R.id.baseLeftTextView);
        if (null == baseLeftTextView) {
            return this;
        }
        if (leftIcon != 0) {
            Drawable drawable = ContextCompat.getDrawable(baseContext, leftIcon);
            // 设置边界
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            // 画在左边
            baseLeftTextView.setCompoundDrawables(drawable, null, null, null);
        }
        setLeftOnClickFinish();
//        setLeftToolbarPadding(40, 0, 40, 0);
        return this;
    }

    /**
     * 设置左侧点击返回
     *
     * @return this
     */
    public BaseActivity setLeftOnClickFinish() {
        RelativeLayout baseLeftToolbar = (RelativeLayout) findViewById(R.id.baseLeftToolbar);
        if (null == baseLeftToolbar) {
            return this;
        }
        baseLeftToolbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        return this;
    }

    /**
     * 自定义左侧点击事件
     *
     * @param leftOnClickListener 点击事件
     * @return this
     */
    public BaseActivity setLeftOnClickListener(OnClickListener leftOnClickListener) {
        RelativeLayout baseLeftToolbar = (RelativeLayout) findViewById(R.id.baseLeftToolbar);
        if (null == baseLeftToolbar) {
            return this;
        }
        baseLeftToolbar.setOnClickListener(leftOnClickListener);
        return this;
    }

    /**
     * 设置左侧文字颜色
     *
     * @param leftTextColor R.color.xxx
     * @return this
     */
    public BaseActivity setLeftTextColor(int leftTextColor) {
        TextView baseLeftTextView = (TextView) findViewById(R.id.baseLeftTextView);
        if (null == baseLeftTextView) {
            return this;
        }
        if (leftTextColor != 0) {
            baseLeftTextView.setTextColor(leftTextColor);
        }
        return this;
    }

    /**
     * 设置左侧布局的内边距
     *
     * @return this
     */
    public BaseActivity setLeftToolbarPadding(int left, int top, int right, int bottom) {
        RelativeLayout baseLeftToolbar = (RelativeLayout) findViewById(R.id.baseLeftToolbar);
        if (null == baseLeftToolbar) {
            return this;
        }
        baseLeftToolbar.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置左侧文字大小
     *
     * @return this
     */
    public BaseActivity setLeftTextSize(int leftTextSize) {
        TextView baseLeftTextView = (TextView) findViewById(R.id.baseLeftTextView);
        if (null == baseLeftTextView) {
            return this;
        }
        if (leftTextSize != 0) {
            baseLeftTextView.setTextSize(leftTextSize);
        }
        return this;
    }

    /**
     * 设置标题文字
     *
     * @return this
     */
    public BaseActivity setTitleText(String titleText) {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return this;
        }
        if (titleText != null && !titleText.isEmpty()) {
            baseTitle.setText(titleText);
        }
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param titleTextColor R.color.xxx
     * @return this
     */
    public BaseActivity setTitleTextColor(int titleTextColor) {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return this;
        }
        if (titleTextColor != 0) {
            baseTitle.setTextColor(ContextCompat.getColor(this, titleTextColor));
        }
        return this;
    }

    /**
     * 设置标题文字大小
     *
     * @return this
     */
    public BaseActivity setTitleTextSize(int titleTextSize) {
        TextView baseTitle = (TextView) findViewById(R.id.baseTitle);
        if (null == baseTitle) {
            return this;
        }
        if (titleTextSize != 0) {
            baseTitle.setTextSize(titleTextSize);
        }
        return this;
    }

    /**
     * 设置右侧文字
     *
     * @return this
     */
    public BaseActivity setRightText(String rightText) {
        TextView baseRightTextView = (TextView) findViewById(R.id.baseRightTextView);
        if (null == baseRightTextView) {
            return this;
        }
        if (rightText != null && !rightText.isEmpty()) {
            baseRightTextView.setText(rightText);
        }
        return this;
    }

    /**
     * 设置右侧图标
     *
     * @param rightIcon R.mipmap.xxx
     * @return this
     */
    public BaseActivity setRightIcon(int rightIcon) {
        TextView baseRightTextView = (TextView) findViewById(R.id.baseRightTextView);
        if (null == baseRightTextView) {
            return this;
        }
        if (rightIcon != 0) {
            Drawable drawable = ContextCompat.getDrawable(baseContext, rightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            baseRightTextView.setCompoundDrawables(drawable, null, null, null);
        }
        return this;
    }

    /**
     * 自定义右侧点击事件
     *
     * @param rightOnClickListener 点击事件
     * @return this
     */
    public BaseActivity setRightOnClickListener(OnClickListener rightOnClickListener) {
        RelativeLayout baseRightToolbar = (RelativeLayout) findViewById(R.id.baseRightToolbar);
        if (null == baseRightToolbar) {
            return this;
        }
        if (rightOnClickListener != null) {
            baseRightToolbar.setOnClickListener(rightOnClickListener);
        }
        return this;
    }

    /**
     * 右侧文字颜色
     *
     * @param rightTextColor R.color.xxx
     * @return this
     */
    public BaseActivity setRightTextColor(int rightTextColor) {
        TextView baseRightTextView = (TextView) findViewById(R.id.baseRightTextView);
        if (null == baseRightTextView) {
            return this;
        }
        if (rightTextColor != 0) {
            baseRightTextView.setTextColor(ContextCompat.getColor(this, rightTextColor));
        }
        return this;
    }

    /**
     * 设置右侧文字大小
     *
     * @return this
     */
    public BaseActivity setRightTextSize(int rightTextSize) {
        TextView baseRightTextView = (TextView) findViewById(R.id.baseRightTextView);
        if (null == baseRightTextView) {
            return this;
        }
        if (rightTextSize != 0) {
            baseRightTextView.setTextSize(rightTextSize);
        }
        return this;
    }

    /**
     * 设置右侧内边距
     *
     * @return this
     */
    public BaseActivity setRightToolbarPadding(int left, int top, int right, int bottom) {
        RelativeLayout baseRightToolbar = (RelativeLayout) findViewById(R.id.baseRightToolbar);
        if (null == baseRightToolbar) {
            return this;
        }
        baseRightToolbar.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 隐藏右侧按钮
     */
    public void hideRightToolbar() {
        RelativeLayout baseRightToolbar = (RelativeLayout) findViewById(R.id.baseRightToolbar);
        baseRightToolbar.setVisibility(View.GONE);
    }

    /**
     * 显示标题栏右侧按钮弹框
     */
    public void showPopMenu(View view, List<String> popupData, OnItemClickListener listener) {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            // 关闭popupWindow
            popupWindowMenu.dismiss();
        } else {
            final View popupView = getLayoutInflater().inflate(R.layout.base_tool_bar_popupwindow, null);
            popupWindowMenu = new PopupWindow(popupView, ListPopupWindow.WRAP_CONTENT, ListPopupWindow.WRAP_CONTENT,
                    true);

            // 设置弹出的popupWindow不遮挡软键盘
            popupWindowMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindowMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // 设置点击外部让popupWindow消失
            popupWindowMenu.setFocusable(true);// 可以试试设为false的结果
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
            popupWindowMenu.showAsDropDown(view, 0, -DimenUtils.px2dip(this, view.getHeight()));
            ListView listView = (ListView) popupView.findViewById(R.id.pop_listView);
            listView.setAdapter(new MoreBaseAdapter<String>(popupData, R.layout.base_tool_bar_popupwindow_item) {
                @Override
                public void bindView(ViewHolder holder, String obj) {
                    holder.setText(R.id.tv_item_content, obj);
                }
            });
            listView.setOnItemClickListener(listener);
        }
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

    /**
     * 密码输入过滤器
     */
    public InputFilter pwdInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            if (TextUtils.isEmpty(charSequence.toString().trim())) {
                return "";
            } else {
                return null;
            }
        }
    };

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String code = info.versionName;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 获取一个随机数
     * * 包含 min 和 max
     */
    public static int randomByMinMax(int min, int max) {
        return new Random().nextInt(max + 1 - min) + min;
    }
    /**
     * 获取一个随机数
     * * 包含 min 和 max
     */
    public static double randomDoublemin() {
        return new Random().nextDouble() + 0.0001;
    }
    public static double randomDoublemax() {
        return new Random().nextDouble() + 0.0009;
    }

    /**复制文字
     * @param context
     * @param value
     */
    public static void copyText(Context context, String value) {
        if (context == null || StringUtil.isNotEmpty(value, true) == false) {
            Log.e(TAG, "copyText  context == null || StringUtil.isNotEmpty(value, true) == false >> return;");
            return;
        }

        ClipData cD = ClipData.newPlainText("simple text", value);
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(cD);
        ToastUtil.showToastBottomShort( "已复制\n" + value);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        FragmentManager fm = getSupportFragmentManager();
//        int index = requestCode >> 16;
//        if (index != 0) {
//            index--;
//            if (fm.getFragments() == null || index < 0
//                    || index >= fm.getFragments().size()) {
//                Log.w(TAG, "Activity result fragment index out of range: 0x"
//                        + Integer.toHexString(requestCode));
//                return;
//            }
//            Fragment frag = fm.getFragments().get(index);
//            if (frag == null) {
//                Log.w(TAG, "Activity result no fragment exists for index: 0x"
//                        + Integer.toHexString(requestCode));
//            } else {
//                handleResult(frag, requestCode, resultCode, data);
//            }
//            return;
//        }
//
//    }
//
//    /**
//     * 递归调用，对所有子Fragement生效
//     *
//     * @param frag
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    private void handleResult(Fragment frag, int requestCode, int resultCode,
//                              Intent data) {
//        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
//        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
//        if (frags != null) {
//            for (Fragment f : frags) {
//                if (f != null)
//                    handleResult(f, requestCode, resultCode, data);
//            }
//        }
//    }
}
