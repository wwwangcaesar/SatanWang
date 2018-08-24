package about.nocare.casaer.satanwang.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import about.nocare.casaer.satanwang.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements TextWatcher, View.OnKeyListener {


    @BindView(R.id.add_friend_back)
    ImageView addFriendBack;
    @BindView(R.id.add_friend_title)
    TextView addFriendTitle;
    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.add_friend_search_view)
    EditText addFriendSearchView;
    @BindView(R.id.add_friend_search_linear_layout)
    LinearLayout addFriendSearchLinearLayout;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    private boolean isExpand = false;//搜索框默认为收缩状态
    private String historyContent = "";//保存历史搜索记录
    private TransitionSet mSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置窗体为没有标题的模式
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            shrinkSearchLayout();
            hideSoftInput();
            addFriendTitle.setVisibility(View.VISIBLE);
            isExpand = false;
            historyContent = addFriendSearchView.getText().toString();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("ACTION_ADD_FLOATVIEW"));
    }

    @OnClick({R.id.add_friend_back, R.id.search_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friend_back:
                finish();
                break;
            case R.id.search_icon:
                if (!isExpand) {
                    expandSearchLayout();
                    showSoftInput();
                    addFriendTitle.setVisibility(View.INVISIBLE);
                    isExpand = true;
                } else {
                    shrinkSearchLayout();
                    hideSoftInput();
                    addFriendTitle.setVisibility(View.VISIBLE);
                    historyContent = addFriendSearchView.getText().toString();
                    isExpand = false;
                }
                break;
        }
    }

    /**
     * 搜索栏伸展
     */
    private void expandSearchLayout() {
        addFriendSearchView.setText(historyContent);
        addFriendSearchView.setHint("搜索");
        addFriendSearchView.requestFocus();//EditText获得焦点
        addFriendSearchView.setSelection(historyContent.length());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) addFriendSearchLinearLayout.getLayoutParams();
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.setMargins(dip2px(55), dip2px(8), dip2px(12), dip2px(8));
        addFriendSearchLinearLayout.setLayoutParams(layoutParams);
        showDelayedTransition(addFriendSearchLinearLayout);
    }

    /**
     * 搜索栏收缩
     */
    private void shrinkSearchLayout() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) addFriendSearchLinearLayout.getLayoutParams();
        layoutParams.width = dip2px(0);
        layoutParams.setMargins(dip2px(12), dip2px(8), dip2px(12), dip2px(8));
        addFriendSearchLinearLayout.setLayoutParams(layoutParams);
        showDelayedTransition(addFriendSearchLinearLayout);
    }

    /**
     * 设置搜索栏显示动画
     */
    private void showDelayedTransition(ViewGroup view) {
        mSet = new AutoTransition();
        mSet.setDuration(300);
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    /**
     * 系统软键盘弹出
     */
    private void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏系统软键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 尺寸转换
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
