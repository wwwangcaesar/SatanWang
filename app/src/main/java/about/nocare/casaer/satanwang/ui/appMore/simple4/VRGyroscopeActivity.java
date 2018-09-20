package about.nocare.casaer.satanwang.ui.appMore.simple4;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.squareup.picasso.Picasso;

import about.nocare.casaer.satanwang.R;
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
