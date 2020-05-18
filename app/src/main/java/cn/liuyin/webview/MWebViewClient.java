package cn.liuyin.webview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.Calendar;

import cn.liuyin.tool.StateBarTool;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.database.Web;
import cn.liuyin.web.function.UIClass;

import static cn.liuyin.webview.ADFilterTool.getClearAdDivJs;


public class MWebViewClient extends WebViewClient {
    public ArrayList<String> httprequest;//http请求数组
    boolean isRefush = false;//
    ADFilterTool adfitertool;
    private Context context;
    private MWebView x5webview;


    public MWebViewClient(Context context, MWebView webView) {
        adfitertool = new ADFilterTool(context);

        this.context = context;
        this.x5webview = webView;
        ConstantData.setMWebViewClient(this);
        httprequest = new ArrayList<>();
        //resourceurls = new ArrayList<>();


    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView p1, String p2) {
        if (p2.startsWith("x5://")) {
            p1.loadUrl(p2);
            return true;
        } else if (p2.startsWith("http://") || p2.startsWith("https://") || p2.startsWith("file:///") || p2.startsWith("view-source:")) {
            return false;
        } else {

            UIClass.openByOtherApp(p1.getContext(), p2);
            return true;
        }

    }

    @Override
    public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
        chageStateBar(webView);
        super.doUpdateVisitedHistory(webView, s, b);
        isRefush = b;

    }

    @Override
    public void onPageStarted(WebView p1, String p2, Bitmap p3) {
        adfitertool.update();
        chageStateBar(p1);
        MWebView view = (MWebView) p1;
        httprequest.clear();
        super.onPageStarted(p1, p2, p3);
    }


    @Override
    public void onPageFinished(WebView p1, String p2) {
        super.onPageFinished(p1, p2);
        chageStateBar(p1);
        System.out.println("onPageFinished");
        if (!isRefush) {
            initHistory(p1);
        }
        p1.setVisibility(View.VISIBLE);
        //this.onPageCommitVisible(p1, p2);
    }

    @Override
    public void onLoadResource(WebView webView, String s) {

        super.onLoadResource(webView, s);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }


    public void onPageCommitVisible(WebView view, String url) {
        if (!x5webview.IsX5WebView()) {
            getClearAdDivJs(x5webview);
            x5webview.loadJavaScriptByPath("file://" + ConstantData.DIYPath + "/init.js");
        }

    }


    //--------------资源过滤 start-----------------///

    /**
     * 在低版本上资源过滤
     *
     * @param view
     * @param url
     * @return
     */

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                if (adfitertool.Go(url)) {
                    httprequest.add("【P】" + url);
                    return new WebResourceResponse(null, null, null);
                } else {
                    httprequest.add(url);
                    return super.shouldInterceptRequest(view, url);
                }
            } catch (Exception e) {
                e.printStackTrace();
                httprequest.add(url + "\n" + e);
                return super.shouldInterceptRequest(view, url);
            }

        } else {
            return null;
        }

    }


    private void doHttp(boolean isblock, WebResourceRequest webResourceRequest) {
        httprequest.add("【地址】" + webResourceRequest.getUrl());
        httprequest.add("【方法】" + webResourceRequest.getMethod());
        httprequest.add("【头部】" + webResourceRequest.getRequestHeaders().toString());
        if (isblock) {
            httprequest.add("【状态】" + "屏蔽");
        } else {
            httprequest.add("【状态】" + "通过");
        }
    }

    /**
     * 在 api21 后资源过滤
     *
     * @param view
     * @param webResourceRequest
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest webResourceRequest) {
        String url = webResourceRequest.getUrl().toString();
        url = url.toLowerCase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                if (adfitertool.Go(url)) {
                    doHttp(true, webResourceRequest);
                    return new WebResourceResponse(null, null, null);
                } else {
                    doHttp(false, webResourceRequest);
                    return super.shouldInterceptRequest(view, webResourceRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
                doHttp(false, webResourceRequest);
                return super.shouldInterceptRequest(view, webResourceRequest);
            }
        } else {
            return null;
        }


    }


    /**
     * @param view
     */

    private void chageStateBar(WebView view) {
        int colorcode = StateBarTool.getStateBarColor(view);
        StatusBarCompat.setStatusBarColor(ConstantData.getMainActivity(), colorcode);
    }


    public void update() {

    }


    private void initHistory(WebView view) {
        System.out.println("initHistory start");
        DBManager db = new DBManager(context);
        Calendar cal = Calendar.getInstance();
        String url = view.getUrl();

        int day = cal.get(Calendar.DAY_OF_YEAR);
        String time = "";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if (hour <= 9) {
            time += "0" + hour + ":";
        } else {
            time += "" + hour + ":";
        }
        if (minute <= 9) {
            time += "0" + minute;
        } else {
            time += "" + minute;
        }
        if (!url.startsWith("http://127.0.0.1")) {
            System.out.println("initHistory go");
            String title = view.getTitle();
            if (!view.getTitle().equals(view.getUrl()) && !view.getTitle().isEmpty()) {
                Web web = new Web(day, time, title, view.getUrl());
                db.addHistory(web);
            }
        }
    }


}

