package com.lovcreate.core.util.image;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.lovcreate.core.util.Logcat;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Glide工具
 * 获取Glide缓存的图片地址,需要在加载图片时 .diskCacheStrategy(DiskCacheStrategy.ALL)
 * http://blog.csdn.net/u012737144/article/details/60872470
 * Created by Albert.Ma on 2017/8/18 0018.
 */
public class GetGlideCacheImageFilePathAsyncTask extends AsyncTask<String, Void, File> {

    private Context context;
    private Callback callback;
    private String filepath;//缓存图片的地址

    public GetGlideCacheImageFilePathAsyncTask(Context context) {
        this.context = context;
    }

    public GetGlideCacheImageFilePathAsyncTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected File doInBackground(String... paramses) {
        String url = paramses[0];
        try {
            FutureTarget<File> futureTarget = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            return futureTarget.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logcat.e(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Logcat.e(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        //此path就是对应文件的缓存路径
        filepath = result.getPath();
        Logcat.e("获取缓存的头像地址:" + filepath);
        //回调
        if (callback != null) {
            callback.afterGetFilePath(filepath);
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public interface Callback {
        void afterGetFilePath(String filepath);
    }
}
