package cn.liuyin.webview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.liuyin.web.constant.ConstantData;

public class WebFunction {

    public static void sharePage(Context c, String title, String url,
                                 Bitmap favicon, Bitmap screenshot) {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, url);
        send.putExtra(Intent.EXTRA_SUBJECT, title);
        send.putExtra(Intent.EXTRA_SHORTCUT_ICON, favicon);
        send.putExtra(Intent.EXTRA_SHORTCUT_INTENT, screenshot);
        try {
            c.startActivity(Intent.createChooser(send, "分享"));
        } catch (android.content.ActivityNotFoundException ex) {
            // if no app handles it, do nothing
        }
    }


    public static String search(String url, String searchengine) {
        String tagUrl;
        String url1 = url;
        String searchengine1 = "http://m.baidu.com/s?word=%s";
        if (searchengine == null || searchengine.equals("")) {
            searchengine = searchengine1;
        }

        if (url.startsWith("file:///") || url.startsWith("javascript:") || url.startsWith("view-source:") ||
                url.startsWith("x5://") || url.startsWith("http://") || url.startsWith("https://")) {
            tagUrl = url;
        } else {
            url = "http://" + url;
            if (Patterns.WEB_URL.matcher(url).matches()) {
                //符合标准
                tagUrl = url;
            } else {
                //不符合标准
                url = url1;
                //url = URLEncoder.encode(url,"utf-8");
                try {
                    url = URLEncoder.encode(url, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                tagUrl = String.format(searchengine, url);
                ConstantData.getMainActivity().editor.putString("SearchEngine", searchengine).apply();
            }
        }

        return tagUrl;
    }

    public static String getToolBox() {
        return "javascript:x=document.createElement('script');x.setAttribute('src','https://pixkoy.github.io/archive/bookmarklet.js');document.body.appendChild(x);";
    }

    public static String ViewInDouban(String name) {
        return "https://movie.douban.com/subject_search?search_text=" + name + "&cat=100";
    }

    //	/transpage?query=https%3A%2F%2Fm.baidu.com%2Fs%3Fword%3D%E8%B0%B7%E6%AD%8C&from=en&to=zh&source=url HTTP/1.1
//	Host: translate.baiducontent.com
    public static String translatePage(String url, String from, String to) {
        return "http://translate.baiducontent.com/transpage?query=" +
                url + "&from=" + from + "&to=" + to + "&source=url";
    }

}
