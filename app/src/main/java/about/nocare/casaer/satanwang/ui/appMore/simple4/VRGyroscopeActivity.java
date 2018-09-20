package about.nocare.casaer.satanwang.ui.appMore.simple4;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.squareup.picasso.Picasso;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.appMore.simple4.MyAR.ImageTargets.ImageTargets;
import about.nocare.casaer.satanwang.widget.appmore.ar.ExpandableTextView;
import about.nocare.casaer.satanwang.widget.appmore.ar.RoundImageView;
import about.nocare.casaer.satanwang.widget.appmore.ar.WhewView;
import about.nocare.casaer.satanwang.widget.vr.gyroscope.GyroscopeImageView;
import about.nocare.casaer.satanwang.widget.vr.gyroscope.GyroscopeManager;
import about.nocare.casaer.satanwang.widget.vr.gyroscope.GyroscopeTransFormation;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * & @Description:  VR看房
 * & @Author:  Satan
 * & @Time:  2018/9/18 14:33
 */
public class VRGyroscopeActivity extends BaseActivity {

    @BindView(R.id.head_image)
    GyroscopeImageView headImage;

    private static final String PIC3_URL =
            "http://wx2.sinaimg.cn/large/6e9ad2bdly1fnih8uqgkuj2140140b2b.jpg";
    //圆形头像配合波纹绘制
    @BindView(R.id.wv)
    WhewView wv;
    @BindView(R.id.my_photo)
    RoundImageView myPhoto;

    private static final int Nou = 1;

    // 声明一个SoundPool
    private SoundPool sp;
    // 定义一个整型用load（）；来设置suondIDf
    private int music;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
                sp.play(music, 1, 1, 0, 0, 1);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrgyroscope);
        ButterKnife.bind(this);
        initView();
        initEvnt();
        initMusic();
        headImage.post(new Runnable() {
            @Override
            public void run() {
                //获取控件大小，作为拉伸参数
                int width = headImage.getWidth();
                int height = headImage.getHeight();

                Picasso.with(VRGyroscopeActivity.this)
                        .load(PIC3_URL)
                        .transform(new GyroscopeTransFormation(width, height))
                        .into(headImage);
            }
        });


    }

    /**
     * 初始化视图
     */
    private void initView() {
        StatusBarUtil.setColor(VRGyroscopeActivity.this, getResources().getColor(R.color.TulingToolBar));
        setTitleText("VR看房");
        setTitleTextColor(R.color.white);
        setLeftIcon(R.mipmap.ic_nav_back_white);
        setToolbarBackground(R.color.TulingToolBar);
    }

    /**
     *  初始化点击事件
     */
    private void initEvnt(){
        headImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CoverActivity.startActivityWithAnimation(VRGyroscopeActivity.this, PIC3_URL,
                        headImage);
            }
        });
        myPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_one_layout, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(VRGyroscopeActivity.this, com.lovcreate.core.R.style.dialog);
                mDialogBuilder.setView(view);
                mDialogBuilder.setCancelable(true);
                AlertDialog alert = mDialogBuilder.create();
                Window dialogWindow = alert.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                alert.setCanceledOnTouchOutside(true);
                alert.setCancelable(true);
                ExpandableTextView readText=(ExpandableTextView)view.findViewById(R.id.readText);
                TextView sure=(TextView)view.findViewById(R.id.sure);
                String text="通过对AR的了解，以及之前项目的总结。实现简单自定义AR呈现的功能，本功能基于Vuforia提供的SDK，并阅读相关API，集成制作出来。详情戳 https://developer.vuforia.com/ \n注意：\n AR实现需要扫描特定图片才会显示出来（类似支付宝扫福字，才会出现福字效果，并提供自定义图片等（展示支持图片，动画有待研究））";
                readText.setContent(text);
                readText.setLinkClickListener(new ExpandableTextView.OnLinkClickListener() {
                    @Override
                    public void onLinkClickListener(ExpandableTextView.LinkType type, String content) {
                        Uri uri = Uri.parse("https://developer.vuforia.com/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                sure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        Intent i = new Intent(VRGyroscopeActivity.this,ImageTargets.class);
                        startActivity(i);
                        alert.dismiss();
                    }
                });
                alert.show();
            }
        });

    }
    private void initMusic(){
        // 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music = sp.load(this, R.raw.qqmusic, 1);
        wv.start();
        handler.sendEmptyMessage(Nou);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GyroscopeManager.getInstance().register(this);
        if(!wv.isStarting()){
            wv.start();
            handler.sendEmptyMessage(Nou);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GyroscopeManager.getInstance().unregister(this);
        if(wv.isStarting()){
            //如果动画正在运行就停止，否则就继续执行
            wv.stop();
            //结束进程
            handler.removeMessages(Nou);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(Nou);
    }
}
