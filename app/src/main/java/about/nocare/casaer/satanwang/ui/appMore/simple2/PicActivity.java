package about.nocare.casaer.satanwang.ui.appMore.simple2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.base.OnItemClickListener;
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
import about.nocare.casaer.satanwang.utils.appAr.simple2.FileUtils;
import about.nocare.casaer.satanwang.utils.appAr.simple2.OperateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片处理
 */
public class PicActivity extends BaseActivity {

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

    /* 照相机拍照得到的图片 */
    private File mCurrentPhotoFile;
    private String photoPath = null, tempPhotoPath, camera_path;

    private int scale = 2;
    int width = 0;

    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";

    OperateUtils operateUtils;

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
        recyBringinto.setOnItemClickListener(new OnItemClickListener() {
            @Override
            protected void onItemNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToastBottomShort(" " + position);
            }
        });
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        operateUtils = new OperateUtils(this);

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
        pictureShow.setImageBitmap(resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");
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
