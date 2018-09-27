package about.nocare.casaer.satanwang.adapter.appmore;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import about.nocare.casaer.satanwang.R;

/**
 * Created by Administrator Satan
 * on 2018/9/27.
 * in 11:47
 * ----------->
 */

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ViewHolder> {
    private Context context;
    public MyNewsAdapter(Activity activity){
        this.context=activity;
    }
    private String[] mTitles = {
            "天高云淡 望断南飞雁",
            "不到长城非好汉 屈指行程二万",
            "六盘山上高峰 红旗漫卷西风",
            "今日长缨在手 何时缚住苍龙",
            "钟山风雨起苍黄 百万雄师过大江",
            "虎距龙盘今胜昔 天翻地覆慨而慷",
            "宜将剩勇追穷寇 不可沽名学霸王",
            "天若有情天亦老 人间正道是沧桑",
    };
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_news.setText(mTitles[position%8]);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_news;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_news = (TextView)itemView.findViewById(R.id.tv_news);
        }
    }
}