package about.nocare.casaer.satanwang.control;

import android.content.Context;
import android.content.Intent;

import about.nocare.casaer.satanwang.bean.chat.MessageEntity;
import about.nocare.casaer.satanwang.ui.chat.DetailActivity;
import about.nocare.casaer.satanwang.ui.chat.NewsActivity;


public class NavigateManager {

    public static void gotoNewsActivity(Context context, MessageEntity messageEntity) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("messageEntity", messageEntity);
        context.startActivity(intent);
    }

    public static void gotoDetailActivity(Context context, String url) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

}
