package com.lovcreate.core.widget.imagewatcher;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovcreate.core.R;
import com.lovcreate.core.util.image.GlideCacheUtil;
import com.lovcreate.core.util.image.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 可查看大图的图片列表
 * <p>
 * 在以下基础上做修改
 * https://github.com/iielse/DemoProjects/tree/master/P02_ImageWatcher
 * 至尊流畅;daLao专用;/斜眼笑
 */
public class MessagePicturesLayout extends FrameLayout implements View.OnClickListener {

    public static final int MAX_DISPLAY_COUNT = 9;
    private final LayoutParams lpChildImage = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    /**
     * 图片列表中 图片的间距
     */
    private int mSpace = 32;
    /**
     * 图片列表中图片的宽度
     */
    private int mImageSize = 172;
    private final int mSingleMaxSize;
    private List<RoundImageView> iPictureList = new ArrayList<>();
    private List<ImageView> mVisiblePictureList = new ArrayList<>();
    private final TextView tOverflowCount;

    private Callback mCallback;
    private boolean isInit;
    private List<String> mDataList;
    private List<String> mThumbDataList;
    private boolean isMatch = false;

    public MessagePicturesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // 必须清理缓存否则第二次进入不显示图片
        GlideCacheUtil.getInstance().clearCacheMemory();
        GlideCacheUtil.getInstance().cleanCatchDisk();
        GlideCacheUtil.getInstance().clearCacheDiskSelf();
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        mSingleMaxSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 216, mDisplayMetrics) + 0.5f);

        for (int i = 0; i < MAX_DISPLAY_COUNT; i++) {
            RoundImageView squareImageView = new RoundImageView(context);
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setVisibility(View.GONE);
            squareImageView.setOnClickListener(this);
            addView(squareImageView);
            iPictureList.add(squareImageView);
        }

        tOverflowCount = new TextView(context);
        tOverflowCount.setTextColor(0xFFFFFFFF);
        tOverflowCount.setTextSize(24);
        tOverflowCount.setGravity(Gravity.CENTER);
        tOverflowCount.setBackgroundColor(0x66000000);
        tOverflowCount.setVisibility(View.GONE);
        addView(tOverflowCount);
    }

    /**
     * 设置数据
     *
     * @param urlThumbList 小图列表
     * @param urlList      大图列表
     * @param imageSize    图片尺寸
     * @param space        图片间距
     */
    public void set(List<String> urlThumbList, List<String> urlList, int imageSize, int space) {
        mThumbDataList = urlThumbList;
        mDataList = urlList;
        mImageSize = imageSize;
        mSpace = space;
        if (isInit) {
            notifyDataChanged();
        }
    }

    /**
     * 设置数据(默认图片尺寸)
     *
     * @param urlThumbList 小图列表
     * @param urlList      大图列表
     * @param isMatch      列表宽度是否match parent
     */
    public void set(List<String> urlThumbList, List<String> urlList, boolean isMatch) {
        mThumbDataList = urlThumbList;
        mDataList = urlList;
        this.isMatch = isMatch;
        if (isInit) {
            notifyDataChanged();
        }
    }

    private void notifyDataChanged() {
//        GlideCacheUtil.getInstance().clearCacheDiskSelf();
        final List<String> thumbList = mThumbDataList;
        final int urlListSize = thumbList != null ? mThumbDataList.size() : 0;

        if (thumbList == null || thumbList.size() < 1) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }

        if (thumbList.size() > mDataList.size()) {
            throw new IllegalArgumentException("dataList.size(" + mDataList.size() + ") > thumbDataList.size(" + thumbList.size() + ")");
        }

        int column = 3;
        if (urlListSize == 1) {
            column = 1;
        } else if (urlListSize == 4) {
            column = 2;
        }
        int row = 0;
        if (urlListSize > 6) {
            row = 3;
        } else if (urlListSize > 3) {
            row = 2;
        } else if (urlListSize > 0) {
            row = 1;
        }

        if (isMatch) {
            mImageSize = urlListSize == 1 ? mSingleMaxSize :
                    (int) ((getWidth() * 1f - mSpace * (column - 1)) / column);
            lpChildImage.width = mImageSize;
            lpChildImage.height = lpChildImage.width;
        } else {
            DisplayMetrics mDisplayMetrics = this.getResources().getDisplayMetrics();
            if (mDisplayMetrics.widthPixels <= 480) {
                mImageSize = 84;
                mSpace = 16;
            }
            lpChildImage.width = mImageSize;
            lpChildImage.height = mImageSize;
        }

        tOverflowCount.setVisibility(urlListSize > MAX_DISPLAY_COUNT ? View.VISIBLE : View.GONE);
        tOverflowCount.setText("+ " + (urlListSize - MAX_DISPLAY_COUNT));
        tOverflowCount.setLayoutParams(lpChildImage);

        mVisiblePictureList.clear();
        for (int i = 0; i < iPictureList.size(); i++) {
            final RoundImageView iPicture = iPictureList.get(i);
            if (i < urlListSize) {
                iPicture.setVisibility(View.VISIBLE);
                mVisiblePictureList.add(iPicture);
                iPicture.setLayoutParams(lpChildImage);
                iPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iPicture.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                Glide.with(getContext()).load(thumbList.get(i)).into(iPicture);
//                Picasso.with(getContext()).load(thumbList.get(i)).into(iPicture);
                iPicture.setTranslationX((i % column) * (mImageSize + mSpace));
                iPicture.setTranslationY((i / column) * (mImageSize + mSpace));
            } else {
                iPicture.setVisibility(View.GONE);
            }

            if (i == MAX_DISPLAY_COUNT - 1) {
                tOverflowCount.setTranslationX((i % column) * (mImageSize + mSpace));
                tOverflowCount.setTranslationY((i / column) * (mImageSize + mSpace));
            }
        }
        getLayoutParams().height = mImageSize * row + mSpace * (row - 1);
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onThumbPictureClick((ImageView) v, mVisiblePictureList, mDataList);
        }
    }

    public interface Callback {
        void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isInit = true;
        notifyDataChanged();
    }
}
