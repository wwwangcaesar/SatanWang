package about.nocare.casaer.satanwang.ui.appMore.simple2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.ToastUtil;
import com.lovcreate.core.widget.HorizontalListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.appmore.HorizontalListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jarlen.photoedit.crop.CropImageType;
import cn.jarlen.photoedit.crop.CropImageView;
import cn.jarlen.photoedit.enhance.PhotoEnhance;
import cn.jarlen.photoedit.filters.NativeFilter;
import cn.jarlen.photoedit.mosaic.DrawMosaicView;
import cn.jarlen.photoedit.mosaic.MosaicUtil;
import cn.jarlen.photoedit.operate.ImageObject;
import cn.jarlen.photoedit.operate.OperateConstants;
import cn.jarlen.photoedit.operate.OperateUtils;
import cn.jarlen.photoedit.operate.OperateView;
import cn.jarlen.photoedit.operate.TextObject;
import cn.jarlen.photoedit.photoframe.PhotoFrame;
import cn.jarlen.photoedit.scrawl.DrawAttribute;
import cn.jarlen.photoedit.scrawl.DrawingBoardView;
import cn.jarlen.photoedit.scrawl.ScrawlTools;
import cn.jarlen.photoedit.utils.FileUtils;
import cn.jarlen.photoedit.utils.PhotoUtils;
import cn.jarlen.photoedit.utils.SelectColorPopup;
import cn.jarlen.photoedit.warp.Picwarp;
import cn.jarlen.photoedit.warp.WarpView;

/**
 * 图片处理
 */
public class PicActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.horizontalListView)
    HorizontalListView recyBringinto;
    @BindView(R.id.carme)
    TextView carme;
    @BindView(R.id.album)
    TextView album;
    @BindView(R.id.pictureShow)
    ImageView pictureShow;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.tllet)
    TextView tllet;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_ok)
    TextView btnOk;
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.llAdd)
    LinearLayout llAdd;
    @BindView(R.id.saturation)
    SeekBar saturation;
    @BindView(R.id.brightness)
    SeekBar brightness;
    @BindView(R.id.contrast)
    SeekBar contrast;
    @BindView(R.id.tone_sub_menu)
    TableLayout toneSubMenu;
    @BindView(R.id.drawView)
    DrawingBoardView drawView;
    @BindView(R.id.mosaic)
    DrawMosaicView mosaic;
    @BindView(R.id.cropmageView)
    CropImageView cropmageView;
    @BindView(R.id.waterlinear)
    LinearLayout waterlinear;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.wordlinear)
    RelativeLayout wordlinear;
    @BindView(R.id.warp_image)
    WarpView warpImage;
    private HorizontalListAdapter adapter;

    private List<String> listBrings = new ArrayList<>();

    /*特效参数相关*/
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;

    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;

    /* 边框 */
    private static final int PHOTO_FRAME_WITH_DATA = 3024;

    /* 马赛克 */
    private static final int PHOTO_MOSAIC_WITH_DATA = 3025;

    /* 涂鸦 */
    private static final int PHOTO_DRAW_WITH_DATA = 3026;

    /* 剪切 */
    private static final int PHOTO_CROP_WITH_DATA = 3027;

    /* 滤镜 */
    private static final int PHOTO_FILTER_WITH_DATA = 3028;

    /* 增强 */
    private static final int PHOTO_ENHANCE_WITH_DATA = 3029;

    /* 旋转 */
    private static final int PHOTO_REVOLVE_WITH_DATA = 3030;

    /* 图像变形 */
    private static final int PHOTO_WARP_WITH_DATA = 3031;

    /* 添加水印图片 */
    private static final int PHOTO_ADD_WATERMARK_DATA = 3032;
    /* 添加文字 */
    private static final int PHOTO_ADD_TEXT_DATA = 3033;

    /*  测试接口 */
    private static final int PHOTO_TEST_TEXT_DATA = 3034;

    private Bitmap newBitmap, oldBitmap;
    private int srcWidth, srcHeight;
    /* 照相机拍照得到的图片 */
    private File mCurrentPhotoFile;
    private String photoPath = null, tempPhotoPath, camera_path;

    private int scale = 2;
    int width = 0;

    //是否取消
    private int canal = 1;
    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";

    OperateUtils operateUtils;
    //图像增强
    private PhotoEnhance pe;
    private int pregress = 0;

    //图像变形
    private Picwarp warp = new Picwarp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        //初始化横向RecyclerView
        initView();
        initEvnt();
    }

    /**
     * 初始化横向RecyclerView
     */
    private void initView() {
        recyBringinto = (HorizontalListView) findViewById(R.id.horizontalListView);
        adapter = new HorizontalListAdapter(this, getData());
        recyBringinto.setAdapter(adapter);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        operateUtils = new OperateUtils(this);
        //图像增强
        saturation.setMax(255);
        saturation.setProgress(128);
        saturation.setOnSeekBarChangeListener(this);
        brightness.setMax(255);
        brightness.setProgress(128);
        brightness.setOnSeekBarChangeListener(this);
        contrast.setMax(255);
        contrast.setProgress(128);
        contrast.setOnSeekBarChangeListener(this);

        recyBringinto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rlTool.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.VISIBLE);
                final LayoutInflater inflater = LayoutInflater.from(PicActivity.this);
                // 获取需要被添加控件的布局  
                // 获取需要添加的布局  
                switch (position) {
                    //滤镜
                    case 0:
                        llAdd.removeAllViews();
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.filterslist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        //美化效果
                        initfilters(layout);
                        llAdd.addView(layout);

                        mosaic.setVisibility(View.GONE);
                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.VISIBLE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //人体变形
                    case 1:
                        llAdd.removeAllViews();
                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.GONE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.VISIBLE);
                        isWater = true;
                        isWords = true;
                        break;
                    //边框
                    case 2:
                        llAdd.removeAllViews();
                        RelativeLayout layout2 = (RelativeLayout) inflater.inflate(R.layout.framelist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initframe(layout2);
                        llAdd.addView(layout2);
                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.VISIBLE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //涂鸦
                    case 3:
                        llAdd.removeAllViews();
                        drawView.setVisibility(View.VISIBLE);
                        pictureShow.setVisibility(View.GONE);
                        RelativeLayout layout3 = (RelativeLayout) inflater.inflate(R.layout.graffitilist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initgraffiti(layout3);
                        llAdd.addView(layout3);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //马赛克
                    case 4:
                        llAdd.removeAllViews();
                        RelativeLayout layout4 = (RelativeLayout) inflater.inflate(R.layout.mosaiclist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initmosaic(layout4);
                        llAdd.addView(layout4);

                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.GONE);
                        mosaic.setVisibility(View.VISIBLE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //剪切
                    case 5:
                        llAdd.removeAllViews();
                        RelativeLayout layout5 = (RelativeLayout) inflater.inflate(R.layout.shearlist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initshear(layout5);
                        llAdd.addView(layout5);
                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.GONE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.VISIBLE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //添加水印
                    case 6:
                        llAdd.removeAllViews();
                        RelativeLayout layout6 = (RelativeLayout) inflater.inflate(R.layout.addwatermark, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initaddwatermark(layout6);
                        llAdd.addView(layout6);
                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.GONE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.VISIBLE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = false;
                        isWords = true;
                        break;
                    //图像增强
                    case 7:
                        llAdd.setVisibility(View.GONE);
                        recyBringinto.setVisibility(View.GONE);
                        toneSubMenu.setVisibility(View.VISIBLE);

                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.VISIBLE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //旋转
                    case 8:
                        llAdd.removeAllViews();
                        RelativeLayout layout8 = (RelativeLayout) inflater.inflate(R.layout.rotatelist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中 
                        initrotate(layout8);
                        llAdd.addView(layout8);

                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.VISIBLE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.GONE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = true;
                        break;
                    //添加文字
                    case 9:
                        llAdd.removeAllViews();
                        RelativeLayout layout9 = (RelativeLayout) inflater.inflate(R.layout.writtenwordslist, null).findViewById(R.id.filtersList);
                        // 将布局加入到当前布局中  
                        initwrittenwords(layout9);
                        llAdd.addView(layout9);

                        drawView.setVisibility(View.GONE);
                        pictureShow.setVisibility(View.GONE);
                        mosaic.setVisibility(View.GONE);
                        cropmageView.setVisibility(View.GONE);
                        waterlinear.setVisibility(View.GONE);
                        wordlinear.setVisibility(View.VISIBLE);
                        warpImage.setVisibility(View.GONE);
                        isWater = true;
                        isWords = false;
                        break;
                }
            }
        });
    }

    Bitmap resultImg = null;
    /**
     * 滤镜效果
     */
    private NativeFilter nativeFilters = new NativeFilter();

    private void initfilters(RelativeLayout layout) {
        TextView filterGray = (TextView) layout.findViewById(R.id.filterGray);
        TextView filterMosatic = (TextView) layout.findViewById(R.id.filterMosatic);
        TextView filterLOMO = (TextView) layout.findViewById(R.id.filterLOMO);
        TextView filterNostalgic = (TextView) layout.findViewById(R.id.filterNostalgic);
        TextView filterComics = (TextView) layout.findViewById(R.id.filterComics);
        TextView filterBrown = (TextView) layout.findViewById(R.id.filterBrown);
        TextView filterSketchPencil = (TextView) layout.findViewById(R.id.filterSketchPencil);
        filterGray.setOnClickListener(this);
        filterMosatic.setOnClickListener(this);
        filterLOMO.setOnClickListener(this);
        filterNostalgic.setOnClickListener(this);
        filterComics.setOnClickListener(this);
        filterBrown.setOnClickListener(this);
        filterSketchPencil.setOnClickListener(this);
    }

    /**
     * 边框效果
     */
    private PhotoFrame mImageFrame;

    private void initframe(RelativeLayout layout) {
        ImageView photoRes_one = (ImageView) layout.findViewById(R.id.photoRes_one);
        ImageView photoRes_two = (ImageView) layout.findViewById(R.id.photoRes_two);
        ImageView photoRes_three = (ImageView) layout.findViewById(R.id.photoRes_three);
        photoRes_one.setOnClickListener(this);
        photoRes_two.setOnClickListener(this);
        photoRes_three.setOnClickListener(this);
    }

    /**
     * 涂鸦效果
     */
    ScrawlTools casualWaterUtil = null;

    private void initgraffiti(RelativeLayout layout) {
        TextView graffit1 = (TextView) layout.findViewById(R.id.graffit1);
        TextView graffit2 = (TextView) layout.findViewById(R.id.graffit2);
        TextView graffit3 = (TextView) layout.findViewById(R.id.graffit3);
        TextView graffit4 = (TextView) layout.findViewById(R.id.graffit4);
        TextView graffit5 = (TextView) layout.findViewById(R.id.graffit5);
        TextView graffit6 = (TextView) layout.findViewById(R.id.graffit6);
        graffit1.setOnClickListener(this);
        graffit2.setOnClickListener(this);
        graffit3.setOnClickListener(this);
        graffit4.setOnClickListener(this);
        graffit5.setOnClickListener(this);
        graffit6.setOnClickListener(this);
    }

    private void initdraw() {

        Bitmap resizeBmp = operateUtils.compressionFiller(newBitmap, drawView);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                resizeBmp.getWidth(), resizeBmp.getHeight());
//
//        drawView.setLayoutParams(layoutParams);

        casualWaterUtil = new ScrawlTools(this, drawView, resizeBmp);

        Bitmap paintBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crayon);

        // int[] res = new int[]{
        // R.drawable.stamp0star,R.drawable.stamp1star,R.drawable.stamp2star,R.drawable.stamp3star
        // };

        casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER,
                paintBitmap, 0xffacb8bd);

        // casualWaterUtil.creatStampPainter(DrawAttribute.DrawStatus.PEN_STAMP,res,0xff00ff00);

    }

    /**
     * 马赛克效果
     */
    int size = 5;

    private void initmosaic(RelativeLayout layout) {
        TextView mosaic1 = (TextView) layout.findViewById(R.id.mosaic1);
        TextView mosaic2 = (TextView) layout.findViewById(R.id.mosaic2);
        TextView mosaic3 = (TextView) layout.findViewById(R.id.mosaic3);
        TextView mosaic4 = (TextView) layout.findViewById(R.id.mosaic4);
        TextView mosaic5 = (TextView) layout.findViewById(R.id.mosaic5);
        mosaic1.setOnClickListener(this);
        mosaic2.setOnClickListener(this);
        mosaic3.setOnClickListener(this);
        mosaic4.setOnClickListener(this);
        mosaic5.setOnClickListener(this);
    }

    /**
     * 剪切效果
     */
    private void initshear(RelativeLayout layout) {
        TextView shear1 = (TextView) layout.findViewById(R.id.shear1);
        TextView shear2 = (TextView) layout.findViewById(R.id.shear2);
        TextView shear3 = (TextView) layout.findViewById(R.id.shear3);
        TextView shear4 = (TextView) layout.findViewById(R.id.shear4);
        TextView shear5 = (TextView) layout.findViewById(R.id.shear5);
        TextView shear6 = (TextView) layout.findViewById(R.id.shear6);
        TextView shear7 = (TextView) layout.findViewById(R.id.shear7);
        TextView shear8 = (TextView) layout.findViewById(R.id.shear8);
        TextView shear9 = (TextView) layout.findViewById(R.id.shear9);
        shear1.setOnClickListener(this);
        shear2.setOnClickListener(this);
        shear3.setOnClickListener(this);
        shear4.setOnClickListener(this);
        shear5.setOnClickListener(this);
        shear6.setOnClickListener(this);
        shear7.setOnClickListener(this);
        shear8.setOnClickListener(this);
        shear9.setOnClickListener(this);
    }

    /**
     * 水印效果
     */
    private OperateView operateView, operateView1;
    private boolean isWater = true;
    private boolean isWords = true;

    private void initaddwatermark(RelativeLayout layout) {
        TextView chunvzuo = (TextView) layout.findViewById(R.id.chunvzuo);
        TextView shenhuifu = (TextView) layout.findViewById(R.id.shenhuifu);
        TextView qiugouda = (TextView) layout.findViewById(R.id.qiugouda);
        TextView guaishushu = (TextView) layout.findViewById(R.id.guaishushu);
        TextView haoxingzuo = (TextView) layout.findViewById(R.id.haoxingzuo);
        TextView wanhuaile = (TextView) layout.findViewById(R.id.wanhuaile);
        TextView xiangsi = (TextView) layout.findViewById(R.id.xiangsi);
        TextView xingzuokong = (TextView) layout.findViewById(R.id.xingzuokong);
        TextView xinnian = (TextView) layout.findViewById(R.id.xinnian);
        TextView zaoan = (TextView) layout.findViewById(R.id.zaoan);
        TextView zuile = (TextView) layout.findViewById(R.id.zuile);
        TextView jiuyaozuo = (TextView) layout.findViewById(R.id.jiuyaozuo);
        TextView zui = (TextView) layout.findViewById(R.id.zui);
        chunvzuo.setOnClickListener(this);
        shenhuifu.setOnClickListener(this);
        qiugouda.setOnClickListener(this);
        guaishushu.setOnClickListener(this);
        haoxingzuo.setOnClickListener(this);
        wanhuaile.setOnClickListener(this);
        xiangsi.setOnClickListener(this);
        xingzuokong.setOnClickListener(this);
        xinnian.setOnClickListener(this);
        zaoan.setOnClickListener(this);
        zuile.setOnClickListener(this);
        jiuyaozuo.setOnClickListener(this);
        zui.setOnClickListener(this);
    }

    /**
     * 旋转效果
     */
    Bitmap OriginalBitmap = null;

    private void initrotate(RelativeLayout layout) {
        Button rotate1 = (Button) layout.findViewById(R.id.rotate1);
        Button rotate2 = (Button) layout.findViewById(R.id.rotate2);
        Button rotate3 = (Button) layout.findViewById(R.id.rotate3);
        Button rotate4 = (Button) layout.findViewById(R.id.rotate4);
        rotate1.setOnClickListener(this);
        rotate2.setOnClickListener(this);
        rotate3.setOnClickListener(this);
        rotate4.setOnClickListener(this);
    }

    /**
     * 添加文字
     */
    private SelectColorPopup menuWindow;
    private String typeface;

    private void initwrittenwords(RelativeLayout layout) {
        Button writtenwordslist1 = (Button) layout.findViewById(R.id.writtenwordslist1);
        Button writtenwordslist2 = (Button) layout.findViewById(R.id.writtenwordslist2);
        Button writtenwordslist3 = (Button) layout.findViewById(R.id.writtenwordslist3);
        writtenwordslist1.setOnClickListener(this);
        writtenwordslist2.setOnClickListener(this);
        writtenwordslist3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (canal == 0) {
            newBitmap = oldBitmap;
        }
        srcWidth = newBitmap.getWidth();
        srcHeight = newBitmap.getHeight();
        int[] dataResult = null;
        int[] pix = new int[srcWidth * srcHeight];
        newBitmap.getPixels(pix, 0, srcWidth, 0, 0, srcWidth, srcHeight);

        switch (v.getId()) {
            case R.id.filterGray:
                dataResult = nativeFilters.gray(pix, srcWidth, srcHeight,
                        1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterMosatic:
                int mosatic = (int) (1 * 30);
                dataResult = nativeFilters.mosatic(pix, srcWidth, srcHeight,
                        mosatic);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterLOMO:
                dataResult = nativeFilters.lomo(pix, srcWidth, srcHeight,
                        1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterNostalgic:
                dataResult = nativeFilters.nostalgic(pix, srcWidth,
                        srcHeight, 1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterComics:
                dataResult = nativeFilters.comics(pix, srcWidth, srcHeight,
                        1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterBrown:
                dataResult = nativeFilters.brown(pix, srcWidth, srcHeight,
                        1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            case R.id.filterSketchPencil:
                dataResult = nativeFilters.sketchPencil(pix, srcWidth,
                        srcHeight, 1);
                resultImg = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,
                        Bitmap.Config.ARGB_8888);
                pictureShow.setImageBitmap(resultImg);
                break;
            /*滤镜效果结束*/

            case R.id.photoRes_one:
                mImageFrame.setFrameType(PhotoFrame.FRAME_SMALL);
                mImageFrame.setFrameResources(
                        R.drawable.frame_around1_left_top,
                        R.drawable.frame_around1_left,
                        R.drawable.frame_around1_left_bottom,
                        R.drawable.frame_around1_bottom,
                        R.drawable.frame_around1_right_bottom,
                        R.drawable.frame_around1_right,
                        R.drawable.frame_around1_right_top,
                        R.drawable.frame_around1_top);
                resultImg = mImageFrame.combineFrameRes();

                pictureShow.setImageBitmap(resultImg);
                pictureShow.invalidate();
                break;
            case R.id.photoRes_two:
                mImageFrame.setFrameType(PhotoFrame.FRAME_SMALL);
                mImageFrame.setFrameResources(
                        R.drawable.frame_around2_left_top,
                        R.drawable.frame_around2_left,
                        R.drawable.frame_around2_left_bottom,
                        R.drawable.frame_around2_bottom,
                        R.drawable.frame_around2_right_bottom,
                        R.drawable.frame_around2_right,
                        R.drawable.frame_around2_right_top,
                        R.drawable.frame_around2_top);
                resultImg = mImageFrame.combineFrameRes();

                pictureShow.setImageBitmap(resultImg);
                pictureShow.invalidate();
                break;
            case R.id.photoRes_three:
                mImageFrame.setFrameType(PhotoFrame.FRAME_BIG);
                mImageFrame.setFrameResources(R.drawable.frame_big1);

                resultImg = mImageFrame.combineFrameRes();

                pictureShow.setImageBitmap(resultImg);
                pictureShow.invalidate();
                break;
            /*边框效果结束*/

            case R.id.graffit1:
                Bitmap paintBitmap1 = BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.marker);
                casualWaterUtil.creatDrawPainter(
                        DrawAttribute.DrawStatus.PEN_WATER, paintBitmap1,
                        0xffadb8bd);
                break;
            case R.id.graffit2:
                Bitmap paintBitmap2 = BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.crayon);
                casualWaterUtil.creatDrawPainter(
                        DrawAttribute.DrawStatus.PEN_CRAYON, paintBitmap2,
                        0xffadb8bd);
                break;
            case R.id.graffit3:
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = 2;
                Bitmap paintBitmap3 = BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.marker, option);
                casualWaterUtil.creatDrawPainter(
                        DrawAttribute.DrawStatus.PEN_WATER, paintBitmap3,
                        0xffadb8bd);
                break;
            case R.id.graffit4:
                Bitmap paintBitmap4 = BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.marker);
                casualWaterUtil.creatDrawPainter(
                        DrawAttribute.DrawStatus.PEN_WATER, paintBitmap4,
                        0xff002200);

                break;
            case R.id.graffit5:
                int[] res = new int[]{R.drawable.stamp0star,
                        R.drawable.stamp1star, R.drawable.stamp2star,
                        R.drawable.stamp3star};

                casualWaterUtil.creatStampPainter(
                        DrawAttribute.DrawStatus.PEN_STAMP, res, 0xff00ff00);
                break;
            case R.id.graffit6:
                Bitmap paintBitmap6 = BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.eraser);

                casualWaterUtil.creatDrawPainter(
                        DrawAttribute.DrawStatus.PEN_ERASER, paintBitmap6,
                        0xffadb8bd);
                break;
            /*涂鸦效果结束*/

            case R.id.mosaic1:
                Bitmap bitmapMosaic = MosaicUtil.getMosaic(newBitmap);
                mosaic.setMosaicResource(bitmapMosaic);
                break;
            case R.id.mosaic2:
                Bitmap bitmapBlur = MosaicUtil.getBlur(newBitmap);
                mosaic.setMosaicResource(bitmapBlur);
                break;
            case R.id.mosaic3:
                Bitmap bit = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.hi4);
                bit = FileUtils.ResizeBitmap(bit, srcWidth, srcHeight);
                mosaic.setMosaicResource(bit);
                break;
            case R.id.mosaic4:
                if (size >= 30) {
                    size = 5;
                } else {
                    size += 5;
                }
                mosaic.setMosaicBrushWidth(size);
                break;
            case R.id.mosaic5:
                mosaic.setMosaicType(MosaicUtil.MosaicType.ERASER);
                break;
            /*马赛克效果结束*/

            case R.id.shear1:
                cropmageView.setFixedAspectRatio(false);
                break;
            case R.id.shear2:
                cropmageView.setFixedAspectRatio(true);
                cropmageView.setAspectRatio(10, 10);
                break;
            case R.id.shear3:
                cropmageView.setFixedAspectRatio(true);
                cropmageView.setAspectRatio(30, 20);
                break;
            case R.id.shear4:
                cropmageView.setFixedAspectRatio(true);
                cropmageView.setAspectRatio(40, 30);
                break;
            case R.id.shear5:
                cropmageView.setFixedAspectRatio(true);
                cropmageView.setAspectRatio(160, 90);
                break;
            case R.id.shear6:
                cropmageView.rotateImage(90);
                break;
            case R.id.shear7:
                cropmageView.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);
                break;
            case R.id.shear8:
                cropmageView.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);
                break;
            case R.id.shear9:
                Bitmap cropImageBitmap = cropmageView.getCroppedImage();
                Toast.makeText(
                        this,
                        "已保存到相册；剪切大小为 " + cropImageBitmap.getWidth() + " x "
                                + cropImageBitmap.getHeight(),
                        Toast.LENGTH_SHORT).show();
                FileUtils.saveBitmapToCamera(this, cropImageBitmap, "crop.jpg");
                break;
            /*剪切效果结束*/


            case R.id.chunvzuo:
                addpic(watermark[0]);
                break;
            case R.id.shenhuifu:
                addpic(watermark[1]);
                break;
            case R.id.qiugouda:
                addpic(watermark[2]);
                break;
            case R.id.guaishushu:
                addpic(watermark[3]);
                break;
            case R.id.haoxingzuo:
                addpic(watermark[4]);
                break;
            case R.id.wanhuaile:
                addpic(watermark[5]);
                break;
            case R.id.xiangsi:
                addpic(watermark[6]);
                break;
            case R.id.xingzuokong:
                addpic(watermark[7]);
                break;
            case R.id.xinnian:
                addpic(watermark[8]);
                break;
            case R.id.zaoan:
                addpic(watermark[9]);
                break;
            case R.id.zuile:
                addpic(watermark[10]);
                break;
            case R.id.jiuyaozuo:
                addpic(watermark[11]);
                break;
            case R.id.zui:
                addpic(watermark[12]);
                break;
            /*水印效果结束*/

            case R.id.rotate1:
                newBitmap = PhotoUtils.rotateImage(newBitmap, 90);
                pictureShow.setImageBitmap(newBitmap);
                OriginalBitmap = newBitmap;
                break;
            case R.id.rotate2:
                Bitmap bitt = newBitmap;
                pictureShow.setImageBitmap(bitt);
                break;
            case R.id.rotate3:
                newBitmap = PhotoUtils.reverseImage(newBitmap, 1, -1);
                pictureShow.setImageBitmap(newBitmap);
                OriginalBitmap = newBitmap;
                break;
            case R.id.rotate4:
                newBitmap = PhotoUtils.reverseImage(newBitmap, -1, 1);
                pictureShow.setImageBitmap(newBitmap);
                OriginalBitmap = newBitmap;
                break;
            /*旋转效果结束*/

            case R.id.writtenwordslist1:
                menuWindow = new SelectColorPopup(PicActivity.this,
                        PicActivity.this);
                // 显示窗口
                menuWindow.showAtLocation(
                        PicActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.writtenwordslist2:
                foutDialog();
                break;
            case R.id.writtenwordslist3:
                addfont();
                break;
            case R.id.submit:
                menuWindow.dismiss();
                break;
            /*添加文字结束*/

        }
    }

    private void initEvnt() {
        carme.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                getPictureFormCamera();
            }
        });
        album.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                getPictureFromPhoto();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                llAdd.setVisibility(View.GONE);
                rlTool.setVisibility(View.GONE);
                recyBringinto.setVisibility(View.VISIBLE);
                toneSubMenu.setVisibility(View.GONE);
                pictureShow.setImageBitmap(oldBitmap);
//                updateImageFrame(oldBitmap);
                pictureShow.setVisibility(View.VISIBLE);

                drawView.setVisibility(View.GONE);
                mosaic.setVisibility(View.GONE);
                cropmageView.setVisibility(View.GONE);
                waterlinear.setVisibility(View.GONE);
                wordlinear.setVisibility(View.GONE);
                warpImage.setVisibility(View.GONE);
                canal = 0;
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                if (resultImg == null && OriginalBitmap == null && waterlinear.getVisibility() == View.GONE && wordlinear.getVisibility() == View.GONE && pregress == 0 && warpImage.getVisibility() == View.GONE) {
                    ToastUtil.showToastBottomShort("请选择美化效果，再保存");
                } else {
                    if (drawView.getVisibility() == View.VISIBLE) {
                        Bitmap bit = casualWaterUtil.getBitmap();
                        newBitmap = bit;
                    } else if (mosaic.getVisibility() == View.VISIBLE) {
                        Bitmap bit1 = mosaic.getMosaicBitmap();
                        newBitmap = bit1;
                    } else if (cropmageView.getVisibility() == View.VISIBLE) {
                        Bitmap bit2 = cropmageView.getCroppedImage();
                        newBitmap = bit2;
                    } else if (warpImage.getVisibility() == View.VISIBLE) {
                        Bitmap bit = warpImage.getWrapBitmap();
                        newBitmap = bit;
                    } else if (!isWater) {
                        operateView.save();
                        Bitmap bmp = getBitmapByView(operateView);
                        newBitmap = bmp;
                    } else if (!isWords) {
                        operateView1.save();
                        Bitmap bmp = getBitmapByView(operateView1);
                        newBitmap = bmp;
                    } else {
                        if (OriginalBitmap == null) {
                            if (resultImg == null) {
                                newBitmap = newBitmap;
                            } else {
                                newBitmap = resultImg;
                            }
                        } else {
                            if (resultImg == null) {
                                newBitmap = OriginalBitmap;
                            } else {
                                newBitmap = newBitmap;
                            }
                        }
                    }
                    updateImageFrame(newBitmap);
                    toneSubMenu.setVisibility(View.GONE);
                    llAdd.setVisibility(View.GONE);
                    mosaic.setVisibility(View.GONE);
                    drawView.setVisibility(View.GONE);
                    cropmageView.setVisibility(View.GONE);
                    waterlinear.setVisibility(View.GONE);
                    wordlinear.setVisibility(View.GONE);
                    pictureShow.setVisibility(View.VISIBLE);
                    recyBringinto.setVisibility(View.VISIBLE);
                    pictureShow.setImageBitmap(newBitmap);//设置图片最终效果反馈
                }

            }
        });
    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    /* 从相机中获取照片 */
    private void getPictureFormCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        tempPhotoPath = FileUtils.DCIMCamera_PATH + FileUtils.getNewFileName()
                + ".jpg";

        mCurrentPhotoFile = new File(tempPhotoPath);

        if (!mCurrentPhotoFile.exists()) {
            try {
                mCurrentPhotoFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mCurrentPhotoFile));
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        tllet.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        llAdd.setVisibility(View.INVISIBLE);
        recyBringinto.setVisibility(View.VISIBLE);

        switch (requestCode) {
            case CAMERA_WITH_DATA:

                photoPath = tempPhotoPath;
                if (mainLayout.getWidth() == 0) {
                    timer.schedule(task, 10, 1000);
                } else {
                    compressed();
                }

                break;

            case PHOTO_PICKED_WITH_DATA:

                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();

                // 延迟每次延迟10 毫秒 隔1秒执行一次
                if (mainLayout.getWidth() == 0) {
                    timer.schedule(task, 10, 1000);
                } else {
                    compressed();
                }
                break;
            case PHOTO_FRAME_WITH_DATA:
            case PHOTO_MOSAIC_WITH_DATA:
            case PHOTO_DRAW_WITH_DATA:
            case PHOTO_CROP_WITH_DATA:
            case PHOTO_FILTER_WITH_DATA:
            case PHOTO_ENHANCE_WITH_DATA:
            case PHOTO_REVOLVE_WITH_DATA:
            case PHOTO_WARP_WITH_DATA:
            case PHOTO_ADD_WATERMARK_DATA:
            case PHOTO_ADD_TEXT_DATA:
            case PHOTO_TEST_TEXT_DATA:

                String resultPath = data.getStringExtra("camera_path");
                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
                pictureShow.setImageBitmap(resultBitmap);
                break;

            default:
                break;
        }

    }

    /**
     * 操作图片，保存等
     */
    private void compressed() {
        Bitmap resizeBmp = operateUtils.compressionFiller(photoPath,
                mainLayout);
        newBitmap = resizeBmp;
        oldBitmap = resizeBmp;
        mImageFrame = new PhotoFrame(this, resizeBmp);
        pictureShow.setImageBitmap(resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");

        Bitmap bit = MosaicUtil.getMosaic(resizeBmp);

        mosaic.setMosaicBackgroundResource(camera_path);
        mosaic.setMosaicResource(bit);
        mosaic.setMosaicBrushWidth(10);

        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        cropmageView.setCropOverlayCornerBitmap(hh);
        cropmageView.setImageBitmap(resizeBmp);
        cropmageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格

        cropmageView.setFixedAspectRatio(false);// 自由剪切

        operateView = new OperateView(PicActivity.this, resizeBmp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                resizeBmp.getWidth(), resizeBmp.getHeight());
        operateView.setLayoutParams(layoutParams);
        waterlinear.addView(operateView);
        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片

        operateView1 = new OperateView(PicActivity.this, resizeBmp);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                resizeBmp.getWidth(), resizeBmp.getHeight());
        operateView1.setLayoutParams(layoutParams1);
        wordlinear.addView(operateView1);
        operateView1.setMultiAdd(true); // 设置此参数，可以添加多个图片

        pe = new PhotoEnhance(resizeBmp);

        warp.initArray();
        warpImage.setWarpBitmap(resizeBmp);

        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) llBottom.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = 0;// 控件的高强制设成0
        llBottom.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    /**
     * 更新ImageFrame
     */
    private void updateImageFrame(Bitmap resizeBmp) {
        casualWaterUtil = new ScrawlTools(this, drawView, resizeBmp);
        mImageFrame = new PhotoFrame(this, resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");

        Bitmap bit = MosaicUtil.getMosaic(resizeBmp);

        mosaic.setMosaicBackgroundResource(camera_path);
        mosaic.setMosaicResource(bit);
        mosaic.setMosaicBrushWidth(10);

        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        cropmageView.setCropOverlayCornerBitmap(hh);
        cropmageView.setImageBitmap(resizeBmp);
        cropmageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格

        cropmageView.setFixedAspectRatio(false);// 自由剪切

        waterlinear.removeAllViews();
        operateView = new OperateView(PicActivity.this, newBitmap);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                newBitmap.getWidth(), newBitmap.getHeight());
        operateView.setLayoutParams(layoutParams);
        waterlinear.addView(operateView);
        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片

        operateView1 = new OperateView(PicActivity.this, resizeBmp);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                newBitmap.getWidth(), newBitmap.getHeight());
        operateView1.setLayoutParams(layoutParams1);
        wordlinear.addView(operateView1);
        operateView1.setMultiAdd(true); // 设置此参数，可以添加多个图片


        pe = new PhotoEnhance(resizeBmp);

        warpImage.setWarpBitmap(resizeBmp);

    }

    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (mainLayout.getWidth() != 0) {
                    Log.i("LinearLayoutW", mainLayout.getWidth() + "");
                    Log.i("LinearLayoutH", mainLayout.getHeight() + "");
                    // 取消定时器
                    timer.cancel();
                    compressed();
                    initdraw();
//                    fillContent();
                }
            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    // 将生成的图片保存到内存中
    public String SaveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 加载横向list数据
     *
     * @return
     */
    private List<String> getData() {
        listBrings.add("滤镜");
        listBrings.add("图片变形");
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

    /**
     * 水印图片资源
     */
    int watermark[] = {R.drawable.watermark_chunvzuo, R.drawable.comment,
            R.drawable.gouda, R.drawable.guaishushu, R.drawable.haoxingzuop,
            R.drawable.wanhuaile, R.drawable.xiangsi, R.drawable.xingzuokong,
            R.drawable.xinnian, R.drawable.zaoan, R.drawable.zuile,
            R.drawable.zuo, R.drawable.zui};

    /**
     * 水印添加图片
     *
     * @param position
     */
    private void addpic(int position) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), position);
        // ImageObject imgObject = operateUtils.getImageObject(bmp);
        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
                5, 150, 100);
        operateView.addItem(imgObject);
    }

    // 将模板View的图片转化为Bitmap
    public Bitmap getBitmapByView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 添加文字
     */
    private void addfont() {
        final EditText editText = new EditText(PicActivity.this);
        new AlertDialog.Builder(PicActivity.this).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            // 隐藏软键盘
                            imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }
                        String string = editText.getText().toString();
                        // TextObject textObj =
                        // operateUtils.getTextObject(string);
                        // if (textObj != null) {
                        // textObj.setTypeface(OperateConstants.FACE_BY);
                        // textObj.commit();
                        // }

                        TextObject textObj = operateUtils.getTextObject(string,
                                operateView1, 5, 150, 100);
                        if (textObj != null) {
                            if (menuWindow != null) {
                                textObj.setColor(menuWindow.getColor());
                            }
                            textObj.setTypeface(typeface);
                            textObj.commit();
                            operateView1.addItem(textObj);
                            operateView1.setOnListener(new OperateView.MyListener() {
                                public void onClick(TextObject tObject) {
                                    alert(tObject);
                                }
                            });
                        }
                    }
                }).show();
    }

    private void alert(final TextObject tObject) {

        final EditText editText = new EditText(PicActivity.this);
        new AlertDialog.Builder(PicActivity.this).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        String string = editText.getText().toString();
                        tObject.setText(string);
                        tObject.commit();
                    }
                }).show();
    }

    /**
     * 字体类型选择弹框
     */
    private void foutDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fout_type, null);
        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(PicActivity.this, com.lovcreate.core.R.style.dialog);
        mDialogBuilder.setView(view);
        mDialogBuilder.setCancelable(true);
        android.app.AlertDialog alert = mDialogBuilder.create();
        Window dialogWindow = alert.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);

        Button moren = (Button) view.findViewById(R.id.moren);
        Button faceby = (Button) view.findViewById(R.id.faceby);
        Button facebygf = (Button) view.findViewById(R.id.facebygf);

        moren.setTypeface(Typeface.DEFAULT);
        faceby.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"
                + OperateConstants.FACE_BY + ".ttf"));
        facebygf.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"
                + OperateConstants.FACE_BYGF + ".ttf"));

        moren.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                typeface = null;
                alert.dismiss();
            }
        });
        faceby.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                typeface = OperateConstants.FACE_BY;
                alert.dismiss();
            }
        });
        facebygf.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                typeface = OperateConstants.FACE_BYGF;
                alert.dismiss();
            }
        });
        alert.show();
    }


    /**
     * , SeekBar.OnSeekBarChangeListener  接口回调
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        pregress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int type = 0;

        switch (seekBar.getId()) {
            case R.id.saturation:
                pe.setSaturation(pregress);
                type = pe.Enhance_Saturation;

                break;
            case R.id.brightness:
                pe.setBrightness(pregress);
                type = pe.Enhance_Brightness;
                break;

            case R.id.contrast:
                pe.setContrast(pregress);
                type = pe.Enhance_Contrast;

                break;

            default:
                break;
        }

        newBitmap = pe.handleImage(type);
        pictureShow.setImageBitmap(newBitmap);
    }
}
