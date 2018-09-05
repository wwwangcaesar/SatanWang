package about.nocare.casaer.satanwang.ui.chat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DigitalDialogInput;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.baidu.MyRecognizer;
import com.lovcreate.core.baidu.listener.ChainRecogListener;
import com.lovcreate.core.baidu.listener.IRecogListener;
import com.lovcreate.core.baidu.listener.MessageStatusRecogListener;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.StringUtil;
import com.lovcreate.core.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.chat.ChatMessageAdapter;
import about.nocare.casaer.satanwang.bean.chat.MessageEntity;
import about.nocare.casaer.satanwang.bean.voice.BaiDuVoiceBeen;
import about.nocare.casaer.satanwang.constant.chat.TulingParams;
import about.nocare.casaer.satanwang.control.RetrofitApi;
import about.nocare.casaer.satanwang.listener.AutoCheck;
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
public class ChatRobotActivity extends BaseActivity implements EventListener {

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
    /*聊天适配器*/
    private List<MessageEntity> msgList = new ArrayList<>();
    private ChatMessageAdapter msgAdapter;
    /*百度语音识别*/
    private EventManager asr;
    /*百度语音识别对话框*/
    private DigitalDialogInput input;
    private ChainRecogListener chainRecogListener;
    protected boolean running = false;
    private MyRecognizer myRecognizer;
    protected Handler handler;

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
        initPermission();
        initView();
        /*百度语音精简版识别*/

        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法


        /*百度语音识别对话框*/
        //handler 用于实时返回数据，检测每秒的语音输入情况，同事返回相应的处理结果
        // 如果想看某些具体运行过程可以在这里设置
        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

//                ToastUtil.showToastBottomShort(msg.obj.toString()+"+++handler");

            }

        };
        // DEMO集成步骤 1.1 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(handler);
        // DEMO集成步骤 1.2 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例
        myRecognizer = new MyRecognizer(this, listener);
        chainRecogListener = new ChainRecogListener();
        // DigitalDialogInput 输入 ，MessageStatusRecogListener可替换为用户自己业务逻辑的listener
        chainRecogListener.addListener(new MessageStatusRecogListener(handler));
        myRecognizer.setEventListener(chainRecogListener); // 替换掉原来的listener

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
        msgAdapter = new ChatMessageAdapter(this, msgList, packageManager);
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
                    hideSoftInput();
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
        btnPressToSpeak.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) ChatRobotActivity.this.getSystemService(ChatRobotActivity.this.VIBRATOR_SERVICE);
                vibrator.vibrate(30);
                start();
//                mystart();
                return false;
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

    //启动百度语音弹框
    private void start() {
        // 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // BaiduASRDigitalDialog的输入参数
        input = new DigitalDialogInput(myRecognizer, chainRecogListener, params);
        BaiduASRDigitalDialog.setInput(input); // 传递input信息，在BaiduASRDialog中读取,
        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);

        // 修改对话框样式
        // intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG);

        running = true;
        startActivityForResult(intent, 2);
    }

    //启动百度语音TTs
    private void mystart() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {

                        ; // 可以用下面一行替代，在logcat中查看代码
                    }
                }
            }
        }, false)).checkAsr(params);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
    }

    // 百度语音的接口回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        BaiDuVoiceBeen dataBean = new Gson().fromJson(params, BaiDuVoiceBeen.class);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getBest_result())) {
                MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_RIGHT, TimeUtil.getCurrentTimeMillis(), dataBean.getBest_result());
                msgList.add(entity);
                msgAdapter.notifyDataSetChanged();
                requestApiByRetrofit_RxJava(dataBean.getBest_result());
            }
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        running = false;
        if (requestCode == 2) {
            String message = "对话框的识别结果：";
            if (resultCode == RESULT_OK) {
                ArrayList results = data.getStringArrayListExtra("results");
                if (results != null && results.size() > 0) {
                    // results.get(0) 即是返回的最佳结果
                    MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_RIGHT, TimeUtil.getCurrentTimeMillis(), results.get(0).toString());
                    msgList.add(entity);
                    msgAdapter.notifyDataSetChanged();
                    requestApiByRetrofit_RxJava(results.get(0).toString());
                }
            } else {
                ToastUtil.showToastBottomShort("识别失败，请使用普通话");
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i("ActivityMiniRecog", "On pause");
        if (!running) {
            myRecognizer.release();
        }
    }

}
