package cn.liuyin.webview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebSettingsExtension;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


public class MWebSetting {
    WebSettings settings;
    private Context mContext;
    private MWebView wView;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    public MWebSetting(Context context, MWebView wView) {
        this.wView = wView;
        this.mContext = context;
        //初始化数据储存

        pref = context.getSharedPreferences("MyWebData", Context.MODE_PRIVATE);
        mGetWebSetting();
        //editor = pref.edit();
    }


    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    private void mGetWebSetting() {
        WebSettings settings = wView.getSettings();
        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }


        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);


        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        settings.setSaveFormData(true);
        //settings.setSavePassword(true);
        //settings.setLightTouchEnabled(true);
        //settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);


        //webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);//开启 Application Caches 功能
        //settings.setAppCacheMaxSize(Long.MAX_VALUE);
        if (wView.IsX5WebView()) {
            IX5WebSettingsExtension ix5s = wView.getSettingsExtension();
            ix5s.customDiskCachePathEnabled(true, mContext.getExternalFilesDir("caches").getAbsolutePath());
            ix5s.setUseQProxy(true);
            ix5s.setShouldRequestFavicon(true);
            ix5s.setSmartFullScreenEnabled(true);
            ix5s.setFitScreen(true);
        }
        settings.setAppCachePath(mContext.getCacheDir().getAbsolutePath());
        //开启定位功能
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(mContext.getFilesDir().getAbsolutePath());
        //settings.get


        //wView.requestFocusFromTouch();

        String ua = pref.getString("UserAgent", WebviewUA.getDefaultUA());
        //webSetting.setUserAgent(ua);
        settings.setUserAgentString(ua);//+

    }


}
