package com.lovcreate.core.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lovcreate.core.R;
import com.lovcreate.core.util.DimenUtils;
import com.lovcreate.core.util.Logcat;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 作者：yuanYe创建于2016/12/20
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
public abstract class BaseFragment extends Fragment {

    private Activity bfActivity;//Fragment所属的Activity

    /**
     * 右侧的菜单list
     */
    private PopupWindow popupWindowMenu;
    private final static String TAG = "BaseFragment";
    protected View view;

    protected ImmersionBar mImmersionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isImmersionBarEnabled())
            initImmersionBar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bfActivity = getActivity();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onCreate...");
    }

    /**
     * 子类onCreateView后,初始化ButterKnife控件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == view) {
            view = getView();
        }
        //初始化ButterKnife
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onStart() {
        super.onStart();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onStart...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onStop...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onDestroyView...");
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(getActivity())
                .statusBarColor(R.color.transparentBackground)// 如果使用了fitsSystemWindows，必须设置背景颜色
                .statusBarDarkFont(true);// 设置状态栏文字颜色为暗色
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    ///////////////////////////////////// 标题栏 ///////////////////////////////////////////////

    /**
     * 设置标题栏背景色
     *
     * @param backgroundColor R.color.xxx
     * @return this
     */
    public void setToolbarBackground(int backgroundColor) {
        FrameLayout toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbarLayout);
        if (toolbarLayout != null) {
            toolbarLayout.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));
        }
    }

    /**
     * 隐藏标题下方横线
     */
    public void hideToolbarBottomLine() {
        View toolbarBottomLine = view.findViewById(R.id.toolbarBottomLine);
        if (toolbarBottomLine != null) {
            toolbarBottomLine.setVisibility(View.GONE);
        }
    }

    protected void setLeftText(String leftText) {
        TextView baseLeftTextView = (TextView) view.findViewById(R.id.baseLeftTextView);
        if (leftText != null && !leftText.isEmpty()) {
            baseLeftTextView.setText(leftText);
        }
    }

    protected void setLeftIcon(int leftIcon) {
        TextView baseLeftTextView = (TextView) view.findViewById(R.id.baseLeftTextView);
        if (leftIcon != 0) {
            Drawable drawable = ContextCompat.getDrawable(bfActivity, leftIcon);
            // 设置边界
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            // 画在左边
            baseLeftTextView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    protected void setLeftOnClickFinish() {
        RelativeLayout baseLeftToolbar = (RelativeLayout) view.findViewById(R.id.baseLeftToolbar);
        baseLeftToolbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                getActivity().finish();
            }
        });
    }

    protected void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        RelativeLayout baseLeftToolbar = (RelativeLayout) view.findViewById(R.id.baseLeftToolbar);
        baseLeftToolbar.setOnClickListener(leftOnClickListener);
    }

    protected void setLeftTextColor(int leftTextColor) {
        TextView baseLeftTextView = (TextView) view.findViewById(R.id.baseLeftTextView);
        if (leftTextColor != 0) {
            baseLeftTextView.setTextColor(leftTextColor);
        }
    }

    protected void setLeftToolbarPadding(int left, int top, int right, int bottom) {
        RelativeLayout baseLeftToolbar = (RelativeLayout) view.findViewById(R.id.baseLeftToolbar);
        baseLeftToolbar.setPadding(left, top, right, bottom);
    }

    protected void setLeftTextSize(int leftTextSize) {
        TextView baseLeftTextView = (TextView) view.findViewById(R.id.baseLeftTextView);
        if (leftTextSize != 0) {
            baseLeftTextView.setTextSize(leftTextSize);
        }
    }

    protected void setTitleText(String titleText) {
        TextView baseTitle = (TextView) view.findViewById(R.id.baseTitle);
        if (titleText != null && !titleText.isEmpty()) {
            baseTitle.setText(titleText);
        }
    }

    protected void setTitleTextColor(int titleTextColor) {
        TextView baseTitle = (TextView) view.findViewById(R.id.baseTitle);
        if (titleTextColor != 0) {
            baseTitle.setTextColor(ContextCompat.getColor(getActivity(), titleTextColor));
        }
    }

    protected void setTitleTextSize(int titleTextSize) {
        TextView baseTitle = (TextView) view.findViewById(R.id.baseTitle);
        if (titleTextSize != 0) {
            baseTitle.setTextSize(titleTextSize);
        }
    }

    protected void setRightText(String rightText) {
        TextView baseRightTextView = (TextView) view.findViewById(R.id.baseRightTextView);
        if (rightText != null && !rightText.isEmpty()) {
            baseRightTextView.setText(rightText);
        }
    }

    protected void setRightIcon(int rightIcon) {
        TextView baseRightTextView = (TextView) view.findViewById(R.id.baseRightTextView);
        if (rightIcon != 0) {
            Drawable drawable = ContextCompat.getDrawable(bfActivity, rightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            baseRightTextView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    protected void setRightOnClickListener(OnClickListener rightOnClickListener) {
        RelativeLayout baseRightToolbar = (RelativeLayout) view.findViewById(R.id.baseRightToolbar);
        if (rightOnClickListener != null) {
            baseRightToolbar.setOnClickListener(rightOnClickListener);
        }
    }

    protected void setRightTextColor(int rightTextColor) {
        TextView baseRightTextView = (TextView) view.findViewById(R.id.baseRightTextView);
        if (rightTextColor != 0) {
            baseRightTextView.setTextColor(rightTextColor);
        }
    }

    protected void setRightTextSize(int rightTextSize) {
        TextView baseRightTextView = (TextView) view.findViewById(R.id.baseRightTextView);
        if (rightTextSize != 0) {
            baseRightTextView.setTextSize(rightTextSize);
        }
    }

    protected void setRightToolbarPadding(int left, int top, int right, int bottom) {
        RelativeLayout baseRightToolbar = (RelativeLayout) view.findViewById(R.id.baseRightToolbar);
        baseRightToolbar.setPadding(left, top, right, bottom);
    }

    /**
     * 隐藏标题栏右侧按钮
     */
    protected void hideRightToolbar() {
        RelativeLayout baseRightToolbar = (RelativeLayout) view.findViewById(R.id.baseRightToolbar);
        baseRightToolbar.setVisibility(View.GONE);
    }

    /**
     * 在原来的RightView的右边动态添加新的View
     *
     * @param newText
     * @param newColor
     * @param newListener
     */
    protected void addRightView(String newText, int newColor, OnClickListener newListener) {
        RelativeLayout baseRightToolbar = (RelativeLayout) view.findViewById(R.id.baseRightToolbar);

        TextView newView = new TextView(getContext());
        newView.setText(newText);
        newView.setTextColor(this.getResources().getColor(newColor));
        newView.setOnClickListener(newListener);

        RelativeLayout.LayoutParams newViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        newViewParams.addRule(RelativeLayout.END_OF, R.id.baseLeftTextView);
        newViewParams.setMarginStart(30);
        newView.setLayoutParams(newViewParams);

        newView.setGravity(Gravity.CENTER);

        baseRightToolbar.addView(newView);
    }

    /**
     * 重写OnDestroy()方法, 销毁时收起软键盘
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Logcat.i(bfActivity + "." + this.toString() + " - ==> onDestroy...");
        hideSoftInput();
    }

    /**
     * 显示标题栏右侧按钮弹框
     */
    public void showPopMenu(View view, ArrayList<String> popupData, OnItemClickListener listener) {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            // 关闭popupWindow
            popupWindowMenu.dismiss();
        } else {
            final View popupView = getActivity().getLayoutInflater().inflate(R.layout.base_tool_bar_popupwindow, null);
            popupWindowMenu = new PopupWindow(popupView, ListPopupWindow.WRAP_CONTENT, ListPopupWindow.WRAP_CONTENT,
                    true);

            // 设置弹出的popupWindow不遮挡软键盘
            popupWindowMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindowMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // 设置点击外部让popupWindow消失
            popupWindowMenu.setFocusable(true);// 可以试试设为false的结果
            popupWindowMenu.setOutsideTouchable(true); // 点击外部消失

            // 必须设置的选项
            popupWindowMenu.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
            popupWindowMenu.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
            // 将window视图显示在点击按钮下面
            popupWindowMenu.showAsDropDown(view, 0, -DimenUtils.px2dip(getContext(), view.getHeight()));
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
    private void hideSoftInput() {
        try {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext()
                    .getSystemService(INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.i(TAG, "----" + e.toString());
        }
    }
}
