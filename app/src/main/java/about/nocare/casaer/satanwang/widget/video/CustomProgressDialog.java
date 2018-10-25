package about.nocare.casaer.satanwang.widget.video;

import android.app.ProgressDialog;
import android.content.Context;
/**
 & @Description:   录制完成 进度条
 & @Author:  Satan
 & @Time:  2018/10/25 10:28
 */
public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
        setMessage("处理中...");
        setMax(100);
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setProgress(0);
    }

    @Override
    public void cancel() {
        super.cancel();
        setProgress(0);
    }
}
