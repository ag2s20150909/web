package cn.liuyin.webview;


import androidx.annotation.Nullable;

/**
 * Created by asus on 2017/11/10.
 */

public class ComendBean {
    public String key = "";
    @Nullable
    public String data = "";

    public String getData() {
        return "key is :" + key + "\ndata is :" + data;
    }
}
