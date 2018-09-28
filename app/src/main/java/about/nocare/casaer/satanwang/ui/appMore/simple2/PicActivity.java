package about.nocare.casaer.satanwang.ui.appMore.simple2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnItemClickListener;
import com.lovcreate.core.util.ToastUtil;
import com.lovcreate.core.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.appmore.HorizontalListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片处理
 */
public class PicActivity extends BaseActivity {

    @BindView(R.id.horizontalListView)
    HorizontalListView recyBringinto;
    private HorizontalListAdapter adapter;

    private List<String> listBrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        //初始化横向RecyclerView
        initView();
    }

    /**
     * 初始化横向RecyclerView
     */
    private void initView() {
        recyBringinto = (HorizontalListView) findViewById(R.id.horizontalListView);
        adapter = new HorizontalListAdapter(this, getData());
        recyBringinto.setAdapter(adapter);
        recyBringinto.setOnItemClickListener(new OnItemClickListener() {
            @Override
            protected void onItemNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToastBottomShort(" "+ position);
            }
        });
    }

    private List<String> getData() {
        listBrings.add("滤镜");
        listBrings.add("人体变形");
        listBrings.add("边框");
        listBrings.add("涂鸦");
        listBrings.add("马赛克");
        listBrings.add("剪切");
        listBrings.add("添加水印");
        listBrings.add("图像增强");
        listBrings.add("旋转");
        listBrings.add("添加文字");
        return listBrings;
    }
}
