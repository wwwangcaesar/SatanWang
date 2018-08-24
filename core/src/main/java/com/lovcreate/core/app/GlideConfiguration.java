package com.lovcreate.core.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import static com.lovcreate.core.constant.CoreConstant.GLIDE_CACHE_DIR;
import static com.lovcreate.core.constant.CoreConstant.GLIDE_CACHE_SIZE;


/**
 * Glide配置
 * Created by Albert.Ma on 2017/10/9 0009.
 */

public class GlideConfiguration implements GlideModule {

    // 需要在AndroidManifest.xml中声明
    // <meta-data
    //    android:name="com.yaphetzhao.glidecatchsimple.glide.GlideConfiguration"
    //    android:value="GlideModule" />

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, GLIDE_CACHE_DIR, GLIDE_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
