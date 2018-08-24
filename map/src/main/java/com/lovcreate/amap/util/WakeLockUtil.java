package com.lovcreate.amap.util;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Bright Lee on 2017/9/25 0025
 */

public class WakeLockUtil {

    private static PowerManager.WakeLock mWakeLock;

    //申请设备电源锁
    public static void acquireWakeLock(Context context) {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    public static void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

}
