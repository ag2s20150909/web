package cn.liuyin.webview;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import cn.liuyin.tool.FileTool;
import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;

import static cn.liuyin.web.constant.ConstantData.getMainActivity;


public class VipVideo {
    Button t;
    ViewGroup parent;

    public VipVideo(MWebView webView) {
        parent = (ViewGroup) webView.getParent();

    }


    public String getFromUrl(String url) {

        /*腾讯*/
        if (url.startsWith("https://m.v.qq.com/x/cover/") || url.startsWith("https://m.v.qq.com/cover/")) {
            url = url.replace("https://m.v.", "https://v.");
            url = url.substring(0, url.indexOf("?"));
            return url;
        }
        /*优酷*/
        else if (url.startsWith("http://m.youku.com/video/id")) {
            url = url.replace("http://m.", "http://v.");
            url = url.replace("video", "v_show");
            return url;
        }
        /*土豆*/
        else if (url.startsWith("http://www.tudou.com/albumplay/")) {
            url = url;
            return url;

        }
        /*乐视*/
        else if (url.startsWith("http://m.le.com/vplay")) {
            url = url.replace("http://m.", "http://www.");
            url = url.replace("vplay_", "ptv/vplay/");
            return url;

        }
        /*爱奇艺*/
        else if (url.startsWith("http://m.iqiyi.com/v")) {
            url = url.replace("http://m.", "http://www.");
            //url=url.replace("vplay_","ptv/vplay/");
            return url;
        }
        /*芒果TV*/
        else if (url.startsWith("http://m.mgtv.com/#/b")) {
            String api = "http://player.quankan.tv/mdparse/mgtv.php?id=";
            url = url.replace("http://m.", "http://www.");
            url = url.replace("#/", "");
            url += ".html";
            return url;
        }
        /*PPTV*/
        else if (url.startsWith("http://m.pptv.com/show/")) {
            url = url.substring(url.indexOf("show/") + 5, url.indexOf(".html"));
            return url;
        }
        /*搜狐视频*/
        else if (url.startsWith("http://m.film.sohu.com/") || url.startsWith("http://m.tv.sohu.com/") || url.startsWith("http://edu.tv.sohu.com/")) {

            url = url;
            return url;

        }
        /*AcFun*/
        else if (url.startsWith("http://m.acfun.cn/v/")) {
            url = url.replace("http://m.", "http://www.");
            url = url.replace("/?ac=", "/ac");
            return url;
        }
        /*B站*/
        else if (url.startsWith("http://www.bilibili.com/mobile/")) {
            url = url.replace("mobile/", "");
            url = url.replace(".html", "/");
            return url;


        } else if (url.contains("pan.baidu.com")) {
            url = url;
            //Run(url);
            return url;

        } else {

            //Toast.makeText( ConstantData.getContext(), "不支持网站\n" + url, Toast.LENGTH_LONG).show();
            remove();
            return "no";
        }
    }


    public void remove() {
        if (t != null) {
            parent.removeView(t);
        }
    }


    @SuppressLint("SetTextI18n")
    public void init(MWebView wView) {
        parent = wView;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//此处相当于布局文件中的Android:layout_gravity属性
        lp.gravity = Gravity.CENTER;
        t = new Button(wView.getContext());
        t.setText("hello");
        t.setLayoutParams(lp);
        t.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
        t.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "hello", Toast.LENGTH_LONG).show();
            Run(wView.getUrl());
        });
        //wView.setLayoutParams(lp);
        if (!getFromUrl(wView.getUrl()).equals("no")) {
            parent.addView(t);
        }

    }


    public void Run(final String url) {
        String api = getMainActivity().pref.getString("VIPVideo", "https://api.47ks.com/webcloud/?v=");
        JSONArray list;

        String json = FileTool.getAssetsString(ConstantData.getContext(), "vipvideo.json");


        try {
            list = new JSONArray(json);
            final String[] apis = new String[list.length()];
            final String[] title = new String[list.length()];

            for (int i = 0; i < list.length(); i++) {
                JSONObject temp = list.getJSONObject(i);
                title[i] = temp.getString("name");
                apis[i] = temp.getString("url");
            }


            int a = 0;
            for (int i = 0; i < apis.length; i++) {
                if (api.contains(apis[i])) {
                    a = i;
                }
            }
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
            AlertDialog finalAlert = null;
            alert = builder.setIcon(R.drawable.ic_baidu)
                    .setTitle("选择解析接口")
                    .setSingleChoiceItems(title, a, (dialog, which) -> {
                        try {
                            URL aa = new URL(apis[which]);
                            String domain = aa.getHost();
                            Toast.makeText(ConstantData.getContext(), "你选择了" + domain, Toast.LENGTH_SHORT).show();
                            getMainActivity().editor.putString("VIPVideo", apis[which]).apply();
                            if (getMainActivity().mainFragement.getWebView1() != null) {
                                String html = "<html><head><title>" + getMainActivity().mainFragement.getWebView1().getTitle() + "</title></head><body><iframe width=\"100%\" height=\"100%\"  src=\"" +
                                        apis[which] + url +
                                        "\" frameborder=\"0\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\"></iframe></body></html>";
                                getMainActivity().mainFragement.getWebView1().loadDataWithBaseURL("x5://vipvideo", html, "text/html", "UTF-8", null);
                                //alert.dismiss();
                            }
                            finalAlert.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            //finalAlert.dismiss();
                        }
                    }).create();
            alert.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
