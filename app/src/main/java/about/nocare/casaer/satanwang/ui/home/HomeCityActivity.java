package about.nocare.casaer.satanwang.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinDict;
import com.lovcreate.core.base.BaseActivity;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.widget.ClearEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.adapter.city.SortAdapter;
import about.nocare.casaer.satanwang.bean.city.City;
import about.nocare.casaer.satanwang.bean.city.SortModel;
import about.nocare.casaer.satanwang.db.DBHelper;
import about.nocare.casaer.satanwang.utils.DensityUtil;
import about.nocare.casaer.satanwang.utils.city.CharacterParser;
import about.nocare.casaer.satanwang.utils.city.PinYinUtils;
import about.nocare.casaer.satanwang.utils.city.PinyinComparator;
import about.nocare.casaer.satanwang.utils.city.SideBar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 城市选择页面
 *
 * @author Satan Wang
 *         created at 2018/7/11 11:10
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT)
public class HomeCityActivity extends BaseActivity implements SideBar.OnTouchingLetterChangedListener, ListView.OnScrollListener {

    @BindView(R.id.baseTitle)
    TextView baseTitle;
    @BindView(R.id.toolbarLayout)
    FrameLayout toolbarLayout;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.ivRefresh)
    ImageView ivRefresh;
    @BindView(R.id.text1)
    ClearEditText text1;
    @BindView(R.id.tvBeijing)
    TextView tvBeijing;
    @BindView(R.id.tvShanghai)
    TextView tvShanghai;
    @BindView(R.id.tvGuangzhou)
    TextView tvGuangzhou;
    @BindView(R.id.tvShenzhen)
    TextView tvShenzhen;
    @BindView(R.id.tvHangzhou)
    TextView tvHangzhou;
    @BindView(R.id.country_lvcountry)
    ListView countryLvcountry;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sidrbar;
    public SortAdapter adapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private List<SortModel> sourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<City> cityListInfo = new ArrayList<>();
    //

    //startActivityForResult flag
    public static final int CITY_SELECT_RESULT_FRAG = 0x0000032;

    public static List<City> sHomeCityBeanList = new ArrayList<>();

    public PinYinUtils mPinYinUtils = new PinYinUtils();

    private TextView letterOverlay; // 对话框首字母textview
    private OverlayThread overlayThread; // 显示首字母对话框
    private boolean isScroll;
    private boolean isOverlayReady;
    private Handler handler;
    private Comparator comparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_city);
        ButterKnife.bind(this);
        setTitleText("选择城市").setLeftIcon(R.mipmap.ic_nav_arrow_left);
        initView();
        tvCityName.setText(AppSession.getChooseCityName());
        setCityData(getCityList());
        initOverlay();// 滑动时候的中间字母弹框,可直接注释掉
    }

    private void initView() {
        sourceDateList = new ArrayList<SortModel>();
        adapter = new SortAdapter(HomeCityActivity.this, sourceDateList);
        countryLvcountry.setAdapter(adapter);

        //滑动显示字符串
        LayoutInflater inflater = LayoutInflater.from(this);
        letterOverlay = (TextView) inflater.inflate(R.layout.v_letter_overlay, null);
        letterOverlay.setVisibility(View.INVISIBLE);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sidrbar.setTextView(dialog);

        //设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    countryLvcountry.setSelection(position);
                }
            }
        });

        countryLvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = ((SortModel) adapter.getItem(position)).getName();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
//                bundle.putParcelable("cityinfo", homeCityBean);
//                intent.putExtras(bundle);
                intent.putExtra("name", cityName);
                setResult(1000, intent);
                finish();
            }
        });
        text1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //汉字转换成拼音
                List<SortModel> list = new ArrayList<>();
                list.clear();
                String cityName = s.toString();
                if (!TextUtils.isEmpty(cityName) && cityName.length() > 0) {
                    for (int i = 0; i < sourceDateList.size(); i++) {
                        if (sourceDateList.get(i).getName().contains(cityName)) {
                            SortModel sortModel = new SortModel();
                            String updatename = sourceDateList.get(i).getName();
                            sortModel.setName(updatename);
                            String pinyin = mPinYinUtils.getStringPinYin(updatename.substring(0, 1));
                            if (!TextUtils.isEmpty(pinyin)) {
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                // 正则表达式，判断首字母是否是英文字母
                                if (sortString.matches("[A-Z]")) {
                                    sortModel.setSortLetters(sortString.toUpperCase());
                                } else {
                                    sortModel.setSortLetters("#");
                                }
                            } else {
                                Log.d("citypicker_log", "null,cityName:-> " + cityName + "       pinyin:-> " + pinyin);
                            }
                            list.add(sortModel);
                        }
                    }
                    adapter.updateListView(list);
                } else {
                    adapter.updateListView(sourceDateList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 滑动时候的中间弹框
     */
    private void initOverlay() {
        handler = new Handler();
        overlayThread = new OverlayThread();
        isOverlayReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        letterOverlay = (TextView) inflater.inflate(R.layout.v_letter_overlay, null);
        letterOverlay.setVisibility(View.INVISIBLE);

        int width = DensityUtil.dp2px(this, 65);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                width, width,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(letterOverlay, lp);
        countryLvcountry.setOnScrollListener(this);
        sidrbar.setOnTouchingLetterChangedListener(this);
    }

    /**
     * 设置数据
     *
     * @param cityList
     */
    private void setCityData(List<City> cityList) {
        cityListInfo = cityList;
        if (cityListInfo == null) {
            return;
        }
        int count = cityList.size();
        String[] list = new String[count];
        for (int i = 0; i < count; i++)
            list[i] = cityList.get(i).getName();

        sourceDateList.addAll(filledData(cityList));
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);
        adapter.notifyDataSetChanged();
    }

    /**
     * a-z排序
     */
    private void sortLetter() {
        comparator = new Comparator<City>() {
            @Override
            public int compare(City lhs, City rhs) {
                String a = lhs.getPinyin().substring(0, 1);
                String b = rhs.getPinyin().substring(0, 1);
                int flag = a.compareTo(b);
                if (flag == 0) {
                    return a.compareTo(b);
                } else {
                    return flag;
                }
            }
        };
    }

    /**
     * 从db中取出数据
     *
     * @return
     */
    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortLetter();
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<SortModel> filledData(List<City> cityList) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < cityList.size(); i++) {

            String result = cityList.get(i).getName();

            if (result != null) {

                SortModel sortModel = new SortModel();

                String cityName = result;
                //汉字转换成拼音
                if (!TextUtils.isEmpty(cityName) && cityName.length() > 0) {

                    String pinyin = mPinYinUtils.getStringPinYin(cityName.substring(0, 1));

                    if (!TextUtils.isEmpty(pinyin)) {

                        sortModel.setName(cityName);

                        String sortString = pinyin.substring(0, 1).toUpperCase();

                        // 正则表达式，判断首字母是否是英文字母
                        if (sortString.matches("[A-Z]")) {
                            sortModel.setSortLetters(sortString.toUpperCase());
                        } else {
                            sortModel.setSortLetters("#");
                        }

                        mSortList.add(sortModel);
                    } else {
                        Log.d("citypicker_log", "null,cityName:-> " + cityName + "       pinyin:-> " + pinyin);
                    }

                }

            }
        }
        return mSortList;
    }

    /**
     * SideBar  自定义滑动监听
     *
     * @param s
     */
    @Override
    public void onTouchingLetterChanged(String s) {
        isScroll = false;
        Map<String, Integer> letterIndex = new HashMap<>();
        if (letterIndex.get(s) != null) {
            int position = letterIndex.get(s);
            countryLvcountry.setSelection(position);
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            if (pattern.matcher(s).matches()) {
                letterOverlay.setTextSize(40);
            } else {
                letterOverlay.setTextSize(20);
            }
            letterOverlay.setText(s);
            letterOverlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            handler.postDelayed(overlayThread, 1000);
        }
    }

    /**
     * 滑动监听
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    /**
     * 滑动监听
     *
     * @param view
     * @param
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }
        if (isOverlayReady) {
            String text;
            String name = cityListInfo.get(firstVisibleItem).getName();
            String pinyin = cityListInfo.get(firstVisibleItem).getPinyin();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = PinYinUtils.converterToFirstSpell(pinyin).substring(0, 1).toUpperCase();
            }
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            if (pattern.matcher(text).matches()) {
                letterOverlay.setTextSize(40);
            } else {
                letterOverlay.setTextSize(20);
            }
            letterOverlay.setText(text);
            letterOverlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    /**
     * 暴露接口，控制中间字母弹框显示
     */
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            letterOverlay.setVisibility(View.GONE);
        }
    }
}
