package cn.liuyin.web.listener;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.PopupMenu;

import cn.liuyin.web.R;


public class MPopMenuClickListener implements PopupMenu.OnMenuItemClickListener {
    Context mContext;

    public MPopMenuClickListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem p1) {
        // TODO: Implement this method
        Intent intent = new Intent("WEBVIEW_MAIN");
        intent.putExtra("KEY", "");
        switch (p1.getItemId()) {
            case R.id.main_menu_share:
                intent.putExtra("KEY", "WEB_SHARE");
                break;
            case R.id.main_menu_share_qq:
                intent.putExtra("KEY", "WEB_SHARE_QQ");
                break;
            case R.id.main_menu_exit:
                intent.putExtra("KEY", "EXIT");
                break;
            case R.id.main_menu_goto_home:
                intent.putExtra("KEY", "GO_HOME");
                break;
            case R.id.main_menu_view_websouce:
                intent.putExtra("KEY", "VIEW_WEB_SOUCE_HTTP");
                break;
            case R.id.main_menu_view_websouce_by_js:
                intent.putExtra("KEY", "VIEW_WEB_SOUCE_JS");
                break;
            case R.id.main_menu_view_websouce_by_chrome:
                intent.putExtra("KEY", "VIEW_WEB_SOUCE_CHEOME");
                break;
            case R.id.main_menu_view_get_all_links:
                intent.putExtra("KEY", "WEB_GET_ALL_LINK");
                break;
            case R.id.main_menu_history:
                intent.putExtra("KEY", "GO_HISTORY");
                break;
            case R.id.main_menu_view_save_screenshots:
                intent.putExtra("KEY", "SHORT_CUT");
                break;
            case R.id.main_menu_view_save_long_screenshots:
                intent.putExtra("KEY", "LONG_CUT");
                break;
            case R.id.main_menu_get_vip_video:
                intent.putExtra("KEY", "GET_VIP_VIDEO");
                break;
            case R.id.main_menu_upgrade_adlist:
                // CouldService.upgradeData();
                break;
            case R.id.main_menu_view_show_http_log:
                intent.putExtra("KEY", "SHOW_HTTP_LOG");
                break;
            case R.id.main_menu_clear_webview_cache:
                intent.putExtra("KEY", "CLEAR_WEBVIEW_CACHE");
                break;
            case R.id.main_menu_clear_webview_cookie:
                intent.putExtra("KEY", "CLEAR_COOKIE");
                break;
            case R.id.main_menu_day_or_night:
                intent.putExtra("KEY", "SWITCH_NIGHT_MODE");
                break;
            case R.id.main_menu_video_full_screen:
                intent.putExtra("KEY", "SET_VIDEO_FULL_SCREEN");
                break;
            case R.id.main_menu_run_javascript:
                intent.putExtra("KEY", "RUN_JAVASCRIPT");
                break;
            case R.id.main_menu_view_find_source_by_tag:
                intent.putExtra("KEY", "FIND_HTML_BY_TAG");
                break;
            case R.id.main_menu_switch_ua:
                intent.putExtra("KEY", "SWITCH_UA");
                break;
            case R.id.main_menu_javascripts:
                intent.putExtra("KEY", "LIST_JAVASCRIPT");
                break;
            case R.id.main_menu_set_tts:
                intent.putExtra("KEY", "SET_TTS");
                break;
            case R.id.main_menu_add_shuqian:
                intent.putExtra("KEY", "ADD_SHUQIAN");
                break;
            case R.id.main_menu_shuqian:
                intent.putExtra("KEY", "SHOW_SHUQIAN");
                break;
            case R.id.main_menu_add_adlist:
                intent.putExtra("KEY", "ADD_ADLIST");
                break;
        }
        mContext.sendBroadcast(intent);
        return false;
    }

}
