package com.lovcreate.core.base;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lovcreate.core.util.DialogUtil;
import com.lovcreate.core.util.Logcat;
import com.lovcreate.core.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/16.
 * <p>
 * OkHTTP请求基础回掉函数, 继承自okhttp-callback, 并实现其中的方法
 */
public abstract class BaseCallBack extends Callback<BaseBean> {
    protected Dialog dialog;
    public Activity activity;
    public Callback callback;//用于显示正在加载中的回调

    public BaseCallBack() {
    }

    public BaseCallBack(Activity activity) {
        this.activity = activity;
    }

    public BaseCallBack(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public void onBefore(Request request, int id) {
        onBefore(request);
    }

    @Override
    public void onAfter(int id) {
        onAfter();
    }

    @Override
    public BaseBean parseNetworkResponse(Response response, int id) throws Exception {
        return parseNetworkResponse(response);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Logcat.e("请求错误:" + e.getMessage());
        onError(call, e);
    }

    @Override
    public void onResponse(BaseBean response, int id) {
        onResponse(response);
    }

    // 请求中加载中弹窗
    public void onBefore(Request request) {
        if (callback != null) {
            callback.onBeforeListener();
        } else {
            if (activity != null) {
                dialog = DialogUtil.buildLoading(activity);
                try {
                    dialog.show();
                } catch (Exception e) {
                    Logcat.e(e.getMessage());
                }
            }
        }
    }

    // 请求完成取消弹窗
    public void onAfter() {
        if (callback != null) {
            callback.onAfterListener();
        } else {
            if (dialog != null && dialog.isShowing() && null != activity) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    Logcat.e(e.getMessage());
                }
            }
        }
    }

    // 解析服务端响应数据
    public BaseBean parseNetworkResponse(Response response) throws Exception {
        String s = response.body().string();
        Log.i("BaseCallBack", String.format("parseNetworkResponse : %s", s));
        BaseBean bean = new BaseBean();
        JSONObject jsonObject = new JSONObject(s);
        bean.setReturnMsg(jsonObject.getString("message"));
        bean.setReturnState(jsonObject.getString("state"));
        if (s.contains("returnData")) {
            if (!TextUtils.isEmpty(jsonObject.getString("returnData"))) {
                bean.setReturnData(jsonObject.getString("returnData"));
                return bean;
            }
        }
        // 如果没有returnData则查看是否有mapData
        if (s.contains("mapData")) {
            if (!TextUtils.isEmpty(jsonObject.getString("mapData"))) {
                String mapData = jsonObject.getString("mapData");
                JSONObject j = new JSONObject(mapData);
                if (mapData.contains("_Return_Data_")) {
                    String returnData = j.getString("_Return_Data_");
                    bean.setReturnData(returnData);
                }
            }
        }
        return bean;
    }

    // 连接错误
    public void onError(Call call, Exception e) {
        Log.e("BaseCallBack", "网络请求失败", e);
        if (e.toString().contains("SocketTimeoutException")) {
            if (activity != null) {
                ToastUtil.showToastBottomShort("网络连接超时，请稍后重试");
            }
            if (callback != null) {
                callback.onAfterListener();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } else if (e.getMessage().contains("500")) {
            if (activity != null) {
                ToastUtil.showToastBottomShort("服务器错误");
            }
        } else {
            if (activity != null) {
                ToastUtil.showToastBottomShort("请求异常，请稍后重试！");
            }
        }
    }

    public void onResponse(BaseBean response) {
    }

    public interface Callback {
        void onBeforeListener();

        void onAfterListener();
    }
}
