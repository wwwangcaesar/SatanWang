package com.lovcreate.core.util;

import android.os.CountDownTimer;

/**
 * 倒计时工具
 * DownTimerUtil downTimerUtil = new DownTimerUtil(60000, 1000);
 * downTimerUtil.start();
 * downTimerUtil.setTimerCallBack(new DownTimerUtil.TimerCallBack() {}
 * <p>
 * Created by Bright on 2017/12/20
 */
public class DownTimerUtil extends CountDownTimer {

    private TimerCallBack timerCallBack;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public DownTimerUtil(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setTimerCallBack(TimerCallBack timerCallBack) {
        this.timerCallBack = timerCallBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerCallBack.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        timerCallBack.onFinish();
    }

    public interface TimerCallBack {
        void onFinish();

        void onTick(long millisUntilFinished);
    }

}
