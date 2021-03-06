package about.nocare.casaer.satanwang.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.appmore.MyNewsAdapter;
import about.nocare.casaer.satanwang.utils.fragment.BannerLayoutManager;
import about.nocare.casaer.satanwang.widget.appmore.image.MyBanner;


/**
 * Created by Jaeger on 16/8/11.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class ImageFragment extends Fragment {
    private RecyclerView mRecycler_2;//消息轮播
    private MyBanner banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         /*轮播图*/
        banner=(MyBanner) view.findViewById(R.id.banner);
        List<Integer> list=new ArrayList<>();
        list.add(R.drawable.bg_monkey_king);
        list.add(R.drawable.cry1);
        list.add(R.drawable.cry2);
        list.add(R.drawable.cry3);
        list.add(R.drawable.cry4);

        setBanner(list);
        banner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//指示器样式加上标题
        banner.setBannerAnimation(ZoomOutTranformer.class);//动画效果
        banner.start();

        /*消息轮播*/
        mRecycler_2 = (RecyclerView)view.findViewById(R.id.recycler2);
        MyNewsAdapter myNewsAdapter = new MyNewsAdapter(getActivity());
        BannerLayoutManager bannerNewsLayoutManager = new BannerLayoutManager(getActivity(),mRecycler_2,8, OrientationHelper.VERTICAL);
        bannerNewsLayoutManager.setTimeSmooth(400f);
        mRecycler_2.setLayoutManager(bannerNewsLayoutManager);
        mRecycler_2.setAdapter(myNewsAdapter);
    }
    /**
     * 轮播图设置图片
     */
    private void setBanner(final List<Integer> list) {
        /*
         * 这里的泛型可以是String，可以是Integer
         * 加载网络图片时使用String
         * 加载本地图片时使用Integer
         */
        String[] tips = getResources().getStringArray(R.array.title);
        List list1 = Arrays.asList(tips);
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setBannerTitles(list1)
                .setDelayTime(4000)
                .start();
    }
    /**
     * 轮播图加载工具
     */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, final ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.ic_launcher_round) // 加载中样式
                    .error(R.mipmap.ic_launcher) // 失败样式
                    .into(imageView);
        }
    }

}
