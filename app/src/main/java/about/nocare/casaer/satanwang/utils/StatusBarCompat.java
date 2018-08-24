package about.nocare.casaer.satanwang.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import about.nocare.casaer.satanwang.R;

/**
 *    全app 存在的悬浮框
 * @author Satan Wang
 * created at 2018/7/6 10:51
 */
public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    public static void translucentStatusBar(@NonNull Activity activity, boolean hideStatusBarBackground) {
        if(Build.VERSION.SDK_INT >= 21) {
            translucentStatusBar21(activity, hideStatusBarBackground);
        } else if(Build.VERSION.SDK_INT >= 19) {
            translucentStatusBar19(activity);
        }

    }
    public static void translucentStatusBar(@NonNull Activity activity) {
        translucentStatusBar(activity, false);
    }

    private static void translucentStatusBar21(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(hideStatusBarBackground) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.VISIBLE);
        }

        ViewGroup mContentView = (ViewGroup)window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if(mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

    }

    private static void translucentStatusBar19(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup)window.getDecorView();
        View fakeView = mDecorView.findViewWithTag("statusBarView");
        if(fakeView != null) {
            mDecorView.removeView(fakeView);
        }

    }

    private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if(mContentChild != null) {
            if("marginAdded".equals(mContentChild.getTag())) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mContentChild.getLayoutParams();
                lp.topMargin -= statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag((Object)null);
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
            View statusBarView = contentView.getChildAt(0);
            //改变颜色时避免重复添加statusBarView
            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity))
            {
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }

    public static void compat(Activity activity)
    {
        compat(activity, INVALID_VAL);
    }


    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    /**

     * Flag只有在使用了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS

     * 并且没有使用 FLAG_TRANSLUCENT_STATUS的时候才有效，也就是只有在状态栏全透明的时候才有效。

     * @param activity

     * @param bDark

     */

    public static void setStatusBarMode(Activity activity, boolean bDark) {

        //6.0以上

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Window window = activity.getWindow();
            View decorView = window.getDecorView();

            if (decorView != null) {

                int vis = decorView.getSystemUiVisibility();

                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.setStatusBarColor(activity.getResources().getColor(R.color.white));

                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));

                }

                decorView.setSystemUiVisibility(vis);

            }



        }

    }
}
