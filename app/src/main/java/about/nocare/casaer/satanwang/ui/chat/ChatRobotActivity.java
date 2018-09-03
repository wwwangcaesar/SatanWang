package about.nocare.casaer.satanwang.ui.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.chat.ChatMessageAdapter;
import about.nocare.casaer.satanwang.bean.chat.MessageEntity;
import about.nocare.casaer.satanwang.constant.chat.TulingParams;
import about.nocare.casaer.satanwang.control.RetrofitApi;
import about.nocare.casaer.satanwang.utils.chat.TimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * & @Description:  聊天机器人
 * & @Author:  Satan
 * & @Time:  2018/8/30 17:23
 */
public class ChatRobotActivity extends BaseActivity {

    PackageManager packageManager;
    @BindView(R.id.lv_message)
    ListView lvMessage;
    @BindView(R.id.iv_send_msg)
    ImageView ivSendMsg;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.ll_msg)
    LinearLayout llMsg;
    @BindView(R.id.rl_msg)
    RelativeLayout rlMsg;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;

    private boolean speak = true;
    /*获取所有应用的包名*/
    private List<ResolveInfo> apps = new ArrayList<>();

    private List<MessageEntity> msgList = new ArrayList<>();
    private ChatMessageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_robot);
        ButterKnife.bind(this);
        packageManager = this.getPackageManager();

//        new FirUpdater(this, "3c57fb226edf7facf821501e4eba08d2", "5704953c00fc74127000000a").checkVersion();

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
        /*获取手机应用的所有包名*/
        loadApps();
        initView();
        initData();
        initEventListen();//监听，点击事件
    }

    /**
     * 获取手机应用的所有包名
     */
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
            Log.e("ddddddd", name + "----" + packageName + "----" + cls);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        StatusBarUtil.setColor(ChatRobotActivity.this, getResources().getColor(R.color.TulingToolBar));
        setTitleText("小A");
        setTitleTextColor(R.color.white);
        setLeftIcon(R.mipmap.ic_nav_back_white);
        setToolbarBackground(R.color.TulingToolBar);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (msgList.size() == 0) {
            MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_LEFT, TimeUtil.getCurrentTimeMillis());
            entity.setText("你好！我是小A！\n祝你每天开心\n有什么要吩咐的么？");
            msgList.add(entity);
        }
        msgAdapter = new ChatMessageAdapter(this, msgList,packageManager);
        lvMessage.setAdapter(msgAdapter);
        lvMessage.setSelection(msgAdapter.getCount());
    }
    /**
     * 监听键盘回车键
     */
    private void initEventListen() {
        tvSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                sendMessage();
            }
        });
        //滑动listview的时候隐藏键盘
        lvMessage.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                hideSoftInput();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        etMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //处理事件
                    sendMessage();
                }
                return false;
            }
        });
        ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speak) {
                    ivSendMsg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_keyboard_chat));
                    btnPressToSpeak.setVisibility(View.VISIBLE);
                    etMsg.setVisibility(View.GONE);
                    tvSend.setVisibility(View.GONE);
                    speak = false;
                } else {
                    ivSendMsg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_voice_chat));
                    btnPressToSpeak.setVisibility(View.GONE);
                    etMsg.setVisibility(View.VISIBLE);
                    tvSend.setVisibility(View.VISIBLE);
                    speak = true;
                }
            }
        });
        btnPressToSpeak.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

            }
        });
    }

    // 给Turing发送问题
    public void sendMessage() {
        String msg = etMsg.getText().toString().trim();

        if (!TextUtils.isEmpty(msg)) {
            MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_RIGHT, TimeUtil.getCurrentTimeMillis(), msg);
            msgList.add(entity);
            msgAdapter.notifyDataSetChanged();
            etMsg.setText("");

            // 仅使用 Retrofit 请求接口
//            requestApiByRetrofit(msg);

            // 使用 Retrofit 和 RxJava 请求接口
            requestApiByRetrofit_RxJava(msg);
        }
    }
    // 请求图灵API接口，获得问答信息
    private void requestApiByRetrofit_RxJava(String info) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TulingParams.TULING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RetrofitApi api = retrofit.create(RetrofitApi.class);
        api.getTuringInfoByRxJava(TulingParams.TULING_KEY, info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseMessage, Throwable::printStackTrace);
    }

    // 处理获得到的问答信息
    private void handleResponseMessage(MessageEntity entity) {
        if (entity == null) return;

        entity.setTime(TimeUtil.getCurrentTimeMillis());
        entity.setType(ChatMessageAdapter.TYPE_LEFT);

        switch (entity.getCode()) {
            case TulingParams.TulingCode.URL:
                entity.setText(entity.getText() + "，点击网址查看：" + entity.getUrl());
                break;
            case TulingParams.TulingCode.NEWS:
                entity.setText(entity.getText() + "，点击查看");
                break;
            case TulingParams.TulingCode.TEXT:
                if (entity.getText().indexOf("日历") != -1) {
                    entity.setText(entity.getText() + "~进入日历~");
                } else if (entity.getText().indexOf("微信") != -1) {
                    entity.setText(entity.getText() + "~打开微信~");
                } else {
                    entity.setText(entity.getText());
                }
                break;
            case TulingParams.TulingCode.VIDEO:
                entity.setText(entity.getText() + "，点击网址查看：" + entity.getUrl());
                break;


        }

        msgList.add(entity);
        msgAdapter.notifyDataSetChanged();
    }
}
