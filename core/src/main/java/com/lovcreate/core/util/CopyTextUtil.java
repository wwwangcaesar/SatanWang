package com.lovcreate.core.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * 复制文字工具类
 * <p>
 * Created by Bright on 2018/1/11 0011.
 */
public class CopyTextUtil {

    public static void copy(Activity activity, String s) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", s);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(activity, "复制成功", Toast.LENGTH_LONG).show();
    }

}
