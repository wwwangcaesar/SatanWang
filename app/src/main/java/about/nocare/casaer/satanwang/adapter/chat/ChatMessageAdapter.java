package about.nocare.casaer.satanwang.adapter.chat;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.library.bubbleview.BubbleTextVew;
import com.lovcreate.core.base.OnClickListener;
import com.lovcreate.core.util.AppSession;
import com.lovcreate.core.widget.CircularImage;

import java.io.File;
import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.bean.chat.MessageEntity;
import about.nocare.casaer.satanwang.constant.chat.TulingParams;
import about.nocare.casaer.satanwang.control.NavigateManager;
import about.nocare.casaer.satanwang.utils.chat.SpecialViewUtil;
import about.nocare.casaer.satanwang.utils.chat.TimeUtil;


public class ChatMessageAdapter extends BaseListAdapter<MessageEntity> {

    private Context mContext;
    private PackageManager packageManager;

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    public ChatMessageAdapter(Context context, List<MessageEntity> list,PackageManager packageManager) {
        super(context, list);
        mContext = context;
        packageManager=packageManager;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == TYPE_LEFT) {
            return TYPE_LEFT;
        }
        return TYPE_RIGHT;
    }

    private View createViewByType(int position) {
        if (getItem(position).getType() == TYPE_LEFT) {
            return mInflater.inflate(R.layout.item_conversation_left, null);
        }
        return mInflater.inflate(R.layout.item_conversation_right, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createViewByType(position);
        }
        packageManager = mContext.getPackageManager();
        final MessageEntity entity = getItem(position);

        TextView tvTime = ViewHolder.get(convertView, R.id.tv_time);
        BubbleTextVew btvMessage = ViewHolder.get(convertView, R.id.btv_message);
        CircularImage civ_avatar = ViewHolder.get(convertView, R.id.civ_avatar_1);
        if (getItem(position).getType() == TYPE_RIGHT) {
            if (TextUtils.isEmpty(AppSession.getHeadUrl())) {

            } else {
                Uri uri = Uri.fromFile(new File(AppSession.getHeadUrl()));
                civ_avatar.setImageURI(uri);
            }
        }
        if (isDisplayTime(position)) {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(TimeUtil.friendlyTime(mContext, entity.getTime()));
        } else {
            tvTime.setVisibility(View.GONE);
        }

        switch (entity.getCode()) {
            case TulingParams.TulingCode.URL:
                btvMessage.setText(SpecialViewUtil.getSpannableString(entity.getText(), entity.getUrl()));
                break;
            case TulingParams.TulingCode.NEWS:
                btvMessage.setText(SpecialViewUtil.getSpannableString(entity.getText(), "点击查看"));
                break;
            case TulingParams.TulingCode.TEXT:
                btvMessage.setText(SpecialViewUtil.getSpannableString(entity.getText(), "美团"));//特别关键字标记颜色
                break;
            default:
                btvMessage.setText(entity.getText());
                break;
        }
        btvMessage.setOnClickListener(v -> {
            switch (entity.getCode()) {
                case TulingParams.TulingCode.URL:
                    NavigateManager.gotoDetailActivity(mContext, entity.getUrl());
                    break;
                case TulingParams.TulingCode.NEWS:
                    NavigateManager.gotoNewsActivity(mContext, entity);
                    break;
                case TulingParams.TulingCode.TEXT:
                    /*打开美团*/
                    Intent intent = packageManager.getLaunchIntentForPackage("com.sankuai.meituan");//"jp.co.johospace.jorte"就是我们获得要启动应用的包名
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                    break;
            }
        });

        btvMessage.setOnLongClickListener(v -> {
            copyDeleteDialog(mContext, entity);
            return false;
        });
        return convertView;
    }

    //  一分钟内的请求与回复不显示时间
    public boolean isDisplayTime(int position) {
        if (position > 0) {
            if ((getItem(position).getTime() - getItem(position - 1).getTime()) > 60 * 1000) {
                return true;
            } else {
                return false;
            }
        } else if (position == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void copyDeleteDialog(final Context context, final MessageEntity entity) {
        new MaterialDialog.Builder(context)
                .items("复制该文本", "删除这一条")
                .itemsCallback(new MaterialDialog.ListCallback() {
                                   @Override
                                   public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                       switch (position) {
                                           case 0:
                                               ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                               cm.setText(entity.getText());
                                               Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
                                               break;
                                           case 1:
                                               getData().remove(entity);
                                               notifyDataSetChanged();
                                               break;
                                       }
                                   }
                               }
                )
                .show();
    }

}
