package about.nocare.casaer.satanwang.utils;

import android.app.Activity;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

import about.nocare.casaer.satanwang.R;

/**
 * PicassoImageLoader
 * Created by Yancy on 2016/10/31.
 */
public class PicassoImageLoader implements ImageLoader {

    private final static String TAG = "PicassoImageLoader";

    @Override
    public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {

        Picasso.with(context)
                .load("file://" + path)
                .resize(width, height)
                .placeholder(R.mipmap.gallery_pick_photo)
                .error(R.mipmap.gallery_pick_photo)
                .into(galleryImageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
/*
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃
 *     ┃　　　┃
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *        神兽保佑
 *        代码无BUG!
 */