## 使用方法

- 图片列表使用视图：
    ```
    <com.lovcreate.core.widget.imagewatcher.MessagePicturesLayout/>
    ```
- 在图片列表视图初始化后，使用以下方法设置数据以及回调函数(两个必须都有)
    ```
        commentImagePicturesLayout.set();
        commentImagePicturesLayout.setCallback(mCallback);
    ```
    - 如果图片列表在Adapter中，那么CallBack回调函数需要传递才能使用
    
    
- 在Activity的Create生命周期中初始化ImageWatcher
    ```
        vImageWatcher = ImageWatcher.Helper.with(this) // 一般来讲， ImageWatcher 需要占据全屏的位置
                        .setTranslucentStatus(!isTranslucentStatus ? Utils.calcStatusBarHeight(this) : 0) // 如果是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                        .setErrorImageRes(R.drawable.error_picture) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                        .setOnPictureLongPressListener(this) // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                        .setLoader(new ImageWatcher.Loader() {
                            @Override
                            public void load(Context context, String url, final ImageWatcher.LoadCallback lc) {
                                Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
        
                                    @Override
                                    public void onLoadStarted(Drawable placeholder) {
                                        lc.onLoadStarted(placeholder);
                                    }
        
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                        lc.onResourceReady(bitmap);
                                    }
                                });
                            }
                        })
                        .create();
    ```

