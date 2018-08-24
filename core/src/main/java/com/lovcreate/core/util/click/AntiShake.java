package com.lovcreate.core.util.click;

/**
 * Created by Bright Lee on 2017/8/29 0029
 * 防抖
 */
public class AntiShake {
    private static LimitQueue<OneClickUtil> queue = new LimitQueue<>(20);

    public static boolean check(Object o) {
        String flag;
        if (o == null) {
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        } else {
            flag = o.toString();
        }
        for (OneClickUtil util : queue.getArrayList()) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        queue.offer(clickUtil);
        return clickUtil.check();
    }
}
