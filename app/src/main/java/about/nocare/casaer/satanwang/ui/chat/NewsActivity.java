package about.nocare.casaer.satanwang.ui.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.jaeger.library.StatusBarUtil;
import com.lovcreate.core.base.BaseActivity;

import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.chat.NewsAdapter;
import about.nocare.casaer.satanwang.bean.chat.MessageEntity;
import about.nocare.casaer.satanwang.bean.chat.NewsEntity;
import about.nocare.casaer.satanwang.control.NavigateManager;
import about.nocare.casaer.satanwang.utils.chat.DisplayUtil;
import about.nocare.casaer.satanwang.widget.chat.XListView;
import about.nocare.casaer.satanwang.widget.chat.swipe.SwipeMenu;
import about.nocare.casaer.satanwang.widget.chat.swipe.SwipeMenuCreator;
import about.nocare.casaer.satanwang.widget.chat.swipe.SwipeMenuItem;
import about.nocare.casaer.satanwang.widget.chat.swipe.SwipeMenuListView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2015/2/5.
 */
public class NewsActivity extends BaseActivity implements XListView.IXListViewListener {

    @BindView(R.id.xlv_listView)
    XListView xlvListView;

    private List<NewsEntity> newsList;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        initActionBar();
        initData();
        initView();
    }

    private void initActionBar() {
        StatusBarUtil.setColor(NewsActivity.this, getResources().getColor(R.color.TulingToolBar));
        setTitleText("新闻");
        setTitleTextColor(R.color.white);
        setLeftIcon(R.mipmap.ic_nav_back_white);
        setToolbarBackground(R.color.TulingToolBar);
    }

    private void initData() {
        MessageEntity messageEntity = (MessageEntity) getIntent().getSerializableExtra("messageEntity");
        if (messageEntity != null && messageEntity.getList() != null && messageEntity.getList().size() > 0) {
            newsList = messageEntity.getList();
        } else {
            finish();
        }
    }

    private void initView() {
        initXlistView();
        newsAdapter = new NewsAdapter(this, newsList);
        xlvListView.setAdapter(newsAdapter);
        setSwipeMenuCreator();
        initSwipeMenuItemClickListener();
    }

    private void initXlistView() {
        xlvListView.setXListViewListener(this);
        xlvListView.setPullRefreshEnable(true);
        xlvListView.setPullLoadEnable(true);
        xlvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavigateManager.gotoDetailActivity(NewsActivity.this, newsList.get(position - 1).getDetailurl());
            }
        });
    }

    private void setSwipeMenuCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                openItem.setWidth(DisplayUtil.dip2px(NewsActivity.this, 90));
                openItem.setTitle("删除");
                openItem.setTitleSize(16);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        xlvListView.setMenuCreator(creator);
    }

    private void initSwipeMenuItemClickListener() {
        xlvListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        deleteNewsListItem(position);
                        break;
                }
                return true;
            }
        });
    }

    private void deleteNewsListItem(int position) {
        NewsEntity entity = newsList.get(position);
        newsList.remove(entity);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xlvListView.stopRefresh();
                xlvListView.setRefreshTime("刚刚");
                newsAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xlvListView.stopLoadMore();
                newsAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
