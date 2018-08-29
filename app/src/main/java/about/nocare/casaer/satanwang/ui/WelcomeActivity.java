package about.nocare.casaer.satanwang.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lovcreate.core.base.BaseActivity;
import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.Random;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.utils.CountDownView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 欢迎页
 *
 * @author Satan Wang
 *         created at 2018/7/6 15:38
 */
@Xml(layouts = {R.layout.activity_welcome})
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.countdwonview)
    CountDownView countdwonview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this,R.layout.activity_welcome);
        ButterKnife.bind(this);

        //随机图片背景设置
        int array[] = {R.drawable.cry1, R.drawable.cry2, R.drawable.cry3, R.drawable.cry4,
                R.drawable.cry5};
        Random rnd = new Random();
        int index = rnd.nextInt(5);
        Drawable cur = getResources().getDrawable(array[index]);
        ivPic.setImageDrawable(cur);
        countdwonview.setCountdownTime(4 * 1000);
        countdwonview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countdwonview.timer != null) {
                    if (countdwonview.animator != null) {
                        countdwonview.animator.cancel();
                    }
                    countdwonview.timer.cancel();
                }
            }
        });
        countdwonview.startCountDownTime(new CountDownView.OnCountdownFinishListener() {
            @Override
            public void countdownFinished() {
                //动画结束后的操作
                date();
            }
        });
    }

    /**
     * 判断是否第一次安装
     */
    private void date() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        final SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    //第一次进入跳转
                    Intent in = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(in);
                    finish();
                    editor.putBoolean("isfer", false);
                    editor.commit();
                    return false;
                }
            }).sendEmptyMessageDelayed(0, 1000);//延迟2秒跳转
        } else {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    /**
     *  线程 启动
     */
    //    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            Message message = new Message();
//            message.what = 1;
//            mHandler.sendMessage(message);
//        }
//    });
//        thread.start();
//    public Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//
//                    break;
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
}
