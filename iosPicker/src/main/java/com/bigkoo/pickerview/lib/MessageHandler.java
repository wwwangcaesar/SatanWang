package com.bigkoo.pickerview.lib;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public final class MessageHandler extends Handler {
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;

    final WheelView loopview;

    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    MessageHandler(WheelView loopview) {
        this.loopview = loopview;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                loopview.invalidate();

                if(callback!=null){
                    callback.afterRoll(String.valueOf(loopview.adapter.getItem(loopview.getCurrentItem())));
                }
                break;

            case WHAT_SMOOTH_SCROLL:
                loopview.smoothScroll(WheelView.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                loopview.onItemSelected();
                break;
        }
    }

    /**
     * 动画完成后的回调接口
     */
    public interface Callback{
        void afterRoll(String value);
    }
}
