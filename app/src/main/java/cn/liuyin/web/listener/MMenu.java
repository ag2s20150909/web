package cn.liuyin.web.listener;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import cn.liuyin.web.R;

/**
 * Created by asus on 2017/9/10.
 */

public class MMenu {
    public static void showMenu(final Context context, View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MPopMenuClickListener(context));
        popup.show();
    }
}