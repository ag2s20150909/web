package cn.liuyin.web.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.liuyin.web.R;


public class MOnClickListener implements View.OnClickListener {
    private Context context;

    public MOnClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View p1) {
        Intent intent = new Intent("WEBVIEW_MAIN");
        intent.putExtra("KEY", "");
        switch (p1.getId()) {
            case R.id.web_exit:
                intent.putExtra("KEY", "EXIT");
                break;
            case R.id.web_goback:
                intent.putExtra("KEY", "WEB_GO_BACK");
                break;
            case R.id.web_goforward:
                intent.putExtra("KEY", "WEB_GO_FORWARD");
                break;
            case R.id.web_refresh:
                intent.putExtra("KEY", "WEB_REFRESH");
                break;
            case R.id.web_search:
                intent.putExtra("KEY", "WEB_SEARCH");
                break;
            case R.id.web_title:
                intent.putExtra("KEY", "SHOW_SEARCH");
                break;
            case R.id.web_icon:
                intent.putExtra("KEY", "SHOW_SETTING");

                break;
            case R.id.search_icon:
                intent.putExtra("KEY", "SHOW_TITLE");
                break;
            case R.id.btn_cleartext:
                intent.putExtra("KEY", "CLEAR_INPUT");
                break;
            default:
                break;
        }
        context.sendBroadcast(intent);
    }


}
