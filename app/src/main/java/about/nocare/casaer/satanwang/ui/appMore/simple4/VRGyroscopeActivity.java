package about.nocare.casaer.satanwang.ui.appMore.simple4;

import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.squareup.picasso.Picasso;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.ui.chat.ChatRobotActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrgyroscope);
        ButterKnife.bind(this);
        initView();
        headImage.post(new Runnable() {
            @Override public void run() {
                //获取控件大小，作为拉伸参数
                int width = headImage.getWidth();
                int height = headImage.getHeight();

                Picasso.with(VRGyroscopeActivity.this)
                        .load(PIC3_URL)
                        .transform(new GyroscopeTransFormation(width, height))
                        .into(headImage);
            }
        });
        headImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CoverActivity.startActivityWithAnimation(VRGyroscopeActivity.this, PIC3_URL,
                        headImage);
            }
        });
    }

    /**
     *  初始化视图
     */
    private void initView() {
        StatusBarUtil.setColor(VRGyroscopeActivity.this, getResources().getColor(R.color.TulingToolBar));
        setTitleText("VR看房");
        setTitleTextColor(R.color.white);
        setLeftIcon(R.mipmap.ic_nav_back_white);
        setToolbarBackground(R.color.TulingToolBar);
    }

    @Override protected void onResume() {
        super.onResume();
        GyroscopeManager.getInstance().register(this);
    }

    @Override protected void onPause() {
        super.onPause();
        GyroscopeManager.getInstance().unregister(this);
    }
}
