package cn.liuyin.webview;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import cn.liuyin.tool.FileUtils;
import cn.liuyin.web.R;
import cn.liuyin.web.activity.MainActivity;
import cn.liuyin.web.activity.WebViewFragment;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.function.MJsoup;
import cn.liuyin.web.function.UIClass;


public class MWebView extends WebView {
    private static final int SOUCE_MSG_SUCCESS = 1;// 获取成功的标识
    private static final int MSG_FAILURE = 0;// 获取失败的标识
    public WebViewFragment mWebviewFragment;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    String Myurl = null;
    MWebView webview;
    Context mContext;
    private OnScrollChangedListerner mOnScrollListener;
    private Handler mHandler = new Handler() {
        // 重写handleMessage()方法，此方法在UI线程运行
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 如果成功，则显示从网络获取到字符串
                case SOUCE_MSG_SUCCESS:
                    Toast.makeText(mContext, "Ok", Toast.LENGTH_LONG).show();
                    UIClass.ViewWebSouce((String) msg.obj);
                    break;
                // 否则提示失败

                case MSG_FAILURE:

                    Toast.makeText(mContext, "NO", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    public Runnable soucerunnable = new Runnable() {
        // 重写run()方法，此方法在新的线程中运行
        String html = null;

        @Override
        public void run() {
            try {
                html = MJsoup.getHmlSouce(mContext, MWebView.this.getUrl());

            } catch (Exception e) {
                mHandler.obtainMessage(MSG_FAILURE).sendToTarget();// 获取图片失败
                return;
            }
            mHandler.obtainMessage(SOUCE_MSG_SUCCESS, html).sendToTarget();
        }
    };

    @SuppressLint({"CommitPrefEdits", "ClickableViewAccessibility"})
    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setWebViewClientExtension(new X5WebViewEventHandler(this));// 配置X5webview的事件处理
        pref = context.getSharedPreferences("MyWebData", Context.MODE_PRIVATE);
        editor = pref.edit();


//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        super.setWebViewClient(new MWebViewClient(context, this));
        super.setDownloadListener(new MDownloadListener());
        super.setWebChromeClient(new MWebChromeClient(context, this));
        //super.setWebViewClientExtension(new MIX5WebViewClientExtension(this));

        new MWebSetting(context, this);


        this.getView().setClickable(true);
        this.getView().setOnTouchListener((v, event) -> false);
        View mView = this.getView();
        if (mView instanceof android.webkit.WebView) {
            android.webkit.WebView Sview = (android.webkit.WebView) mView;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Sview.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                Sview.setBackgroundColor(Color.parseColor("#50aaffcc"));
            }
        }


        super.addJavascriptInterface(new WebJsInterface(this.getContext(), this), "Android");
        //super.addJavascriptInterface(new WebJsInterface(this.getContext(), this), "WeixinJSBridge");
        super.setOnLongClickListener(new LongPressListenerWrapper(this, ConstantData.getMainActivity()));//do long press listener

    }

    public void setMainFragmetn(WebViewFragment webViewFragment) {
        this.mWebviewFragment = webViewFragment;
    }

    @Override
    public void reload() {
        this.loadJavaScript("Android.stopspeak()");
    }

    @Override
    public void goBack() {
        this.loadJavaScript("Android.stopspeak()");
        super.goBack();
    }

    @Override
    public void goForward() {
        this.loadJavaScript("Android.stopspeak()");
        super.goForward();
    }

    @Override
    public void loadUrl(String url) {
        this.loadJavaScript("Android.stopspeak()");
        //stopspeak()
        if (url.startsWith("x5://vipvideo")) {
            getVipVideo();
        } else {
            super.loadUrl(url);
        }
    }

    @TargetApi(19)
    public void loadJavaScript(String js) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            this.evaluateJavascript(js, s -> {

            });
        } else {


        }
    }

    @TargetApi(19)
    public void loadJavaScriptByPath(String path) {
        Activity activity = (Activity) this.getContext();
        if (path.startsWith("file://")) {
            path = path.substring(path.indexOf(":") + 3);
        }
        String finalPath = path;
        activity.runOnUiThread(() -> {
            String js = FileUtils.readFileContent(finalPath);
            MWebView.this.evaluateJavascript(js, s -> {
                if (!(s.isEmpty() || s.equals("null")))
                    System.out.println("onReceiveValue is " + s);
            });

            // TODO Your code
        });


    }

    public void getVipVideo() {
        VipVideo vip = new VipVideo(this);
        if (!vip.getFromUrl(this.getUrl()).equals("no")) {
            vip.Run(this.getUrl());
        }

    }

    public void setUA(String ua) {
        WebSettings webSetting = this.getSettings();

        webSetting.setUserAgentString(ua);
    }

    public void clearCookie() {
        CookieSyncManager.createInstance(this.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    public void setVideoFullScreen(boolean b) {
        Context context = this.getContext();
        context.getApplicationInfo().processName = "com.tencent.android.qqdownloader";
        if (b) {
            //this.setVideoFullScreen(context, false);
            editor.putBoolean("VideoFullScreen", false).apply();
            //Toasty.info(context,"自动全屏已关闭").show();
        } else {
            //this.setVideoFullScreen(context, true);
            editor.putBoolean("VideoFullScreen", true).apply();
            //Toasty.info(context,"自动全屏已开启").show();
        }
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {
        this.clearCache(true);
        QbSdk.clearAllWebViewCache(ConstantData.getContext(), true);
    }

    public void doBackPressed() {
        MainActivity main = ConstantData.getMainActivity();
        if (this.canGoBack()) {
            this.goBack();// 返回前一个页面
        } else {
            if ((System.currentTimeMillis() - main.exitTime) > 2000) {
                Toast.makeText(main, "再按一次退出程序(2秒内)", Toast.LENGTH_SHORT).show();
                main.exitTime = System.currentTimeMillis();
            } else {
                main.finish();
            }
        }
    }

    public void setMWebViewDayOrNight(boolean isDay, boolean isFirst) {
        this.setDayOrNight(isDay);
        editor.putBoolean("DayOrNight", isDay);
        editor.apply();
        MainActivity main = ConstantData.getMainActivity();
        if (isDay) {
            //变为日间模式
            //main.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //mWebviewFragment.top_layout.setBackgroundColor(main.getResources().getColor(R.color.md_blue_A100, main.getTheme()));
            } else {
                //mWebviewFragment.top_layout.setBackgroundColor(main.getResources().getColor(R.color.md_blue_A100));
            }
            main.getApplication().setTheme(R.style.AppTheme);
        } else {
            //变为夜间模式
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWebviewFragment.top_layout.setBackgroundColor(main.getResources().getColor(R.color.md_blue_grey_800, main.getTheme()));
            } else {
                mWebviewFragment.top_layout.setBackgroundColor(main.getResources().getColor(R.color.md_blue_grey_800));
            }
            main.getApplication().setTheme(R.style.AppThemeNight);
        }


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t <= 1) {
            if (mOnScrollListener != null)
                mOnScrollListener.onTop();
        } else if (this.getContentHeight() * this.getScale() - (this.getHeight() + this.getScrollY()) <= 1) {
            if (mOnScrollListener != null)
                mOnScrollListener.onBottom();
        } else {
            if (mOnScrollListener != null)
                mOnScrollListener.onScroll(l, t, oldl, oldt);
        }
    }

    public String getHTML() {
        IX5WebViewExtension ix5e = super.getX5WebViewExtension();
        return ix5e.getDocumentOuterHTML();
    }

    public void setOnScrollChangedListerner(OnScrollChangedListerner listerner) {
        mOnScrollListener = listerner;
    }

    public boolean IsX5WebView() {
        return this.getX5WebViewExtension() != null;
    }

    public void getOutJavasccript(String url) {
        String js;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            js = "(function(){" +
                    "var d=document;" +
                    "var s=d.createElement('script');" +
                    "s.setAttribute('type', 'text/javascript');" +
                    "s.setAttribute('src', '" + url + "');" +
                    "d.body.appendChild(s);" +
                    "})();";
        } else {
            if (url.startsWith("file://")) {
                url = url.substring(7);
            }
            url = FileUtils.readFileContent(url);
            js = "(function(){" +
                    "var d=document;" +
                    "var s=d.createElement('script');" +
                    "s.setAttribute('type', 'text/javascript');" +
                    "s.textContent=" + url + ";" +
                    "d.body.appendChild(s);" +
                    "})();";
        }
        this.loadJavaScript(js);
    }

    public void getOutCss(String url) {

        String js;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            js = "(function(){" +
                    "var d=document;" +
                    "var s=d.createElement('script');" +
                    "s.setAttribute('type', 'text/javascript');" +
                    "s.setAttribute('src', '" + url + "');" +
                    "d.head.appendChild(s)" +
                    "})();;";
        } else {
            if (url.startsWith("file://")) {
                url = url.substring(7);
            }
            url = FileUtils.readFileContent(url);
            js = "(function(){" +
                    "var d=document;" +
                    "var s=d.createElement('script');" +
                    "s.setAttribute('type', 'text/javascript');" +
                    "s.=" + url + ";" +
                    "var b=document.body;" +
                    "b.appendChild(s);" +
                    "})();";
        }
        //innerHTML
        this.loadJavaScript(js);
    }

    public interface OnScrollChangedListerner {
        void onTop();

        void onBottom();

        void onScroll(int l, int t, int oldl, int oldt);
    }


}
