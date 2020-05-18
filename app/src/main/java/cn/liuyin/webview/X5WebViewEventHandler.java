package cn.liuyin.webview;


import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewClientExtension;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;

import cn.liuyin.web.constant.ConstantData;

import static cn.liuyin.webview.ADFilterTool.getClearAdDivJs;

/**
 * Created by asus on 2017/9/17.
 */

public class X5WebViewEventHandler extends ProxyWebViewClientExtension implements IX5WebViewClientExtension {
    /**
     * 这个类用于实现由于X5webview适配架构导致的部分client回调不会发生，或者回调中传入的值不正确
     * 这个方法中所有的interface均是直接从内核中获取值并传入内核，请谨慎修改
     * 使用时只需要在对应的方法中加入你自己的逻辑就可以
     * 同时注意：具有返回值的方法在正常情况下保持其返回效果
     * 一般而言：返回true表示消费事件，由用户端直接处理事件
     * 返回false表示需要内核使用默认机制处理事件
     */

    private MWebView webView; //the vote of x5webview

    public X5WebViewEventHandler(MWebView webView) {
        this.webView = webView;


    }


    //当js可用时
    @Override
    public void documentAvailableInMainFrame() {
        //注入js的时机；
        getClearAdDivJs(webView);
        webView.loadJavaScriptByPath("file://" + ConstantData.DIYPath + "/init.js");

    }

    @Override
    public void onReceivedViewSource(String s) {
        System.out.print(s);
        super.onReceivedViewSource(s);
    }
}
