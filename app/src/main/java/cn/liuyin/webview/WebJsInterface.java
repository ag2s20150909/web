package cn.liuyin.webview;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.liuyin.tool.FileUtils;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.function.MJsoup;
import cn.liuyin.web.function.UIClass;


public class WebJsInterface {
    Context mContext;
    MWebView webview;

    public WebJsInterface(Context context, MWebView webview) {
        this.mContext = context;
        this.webview = webview;
    }

    @JavascriptInterface
    public void exe(String key, String data) {
        Intent intent = new Intent("WEBVIEW_MAIN");
        intent.putExtra("KEY", key);
        intent.putExtra("data", data);
        mContext.sendBroadcast(intent);
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void refresh() {
        //webview.setVisibility(View.GONE);
        Toast.makeText(mContext, "aaaa", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void search(String msg) {
        Intent intent = new Intent("WEBVIEW_MAIN");
        intent.putExtra("KEY", "WEB_SEARCH_T");
        intent.putExtra("data", msg);
        mContext.sendBroadcast(intent);
    }

    @JavascriptInterface
    public void deleteBookMark(String id) {
        AlertDialog.Builder ad1 = new AlertDialog.Builder(mContext);
        ad1.setTitle("删除书签:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

                DBManager db = ConstantData.getDBManger();
                int i1 = Integer.parseInt(id);
                db.deleteShuQianById(i1);

            }
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框


    }

    @JavascriptInterface
    public void showMessageBox(String msg) {
        UIClass.ViewWebSouce(msg);
    }

    @JavascriptInterface
    public String getJsObject(String js) {
        return js;
    }

    @JavascriptInterface
    public void speak(String msg) {
        ConstantData.getMainActivity().ttsBinder.getService().Speak(msg);
    }

    @JavascriptInterface
    public void stopspeak() {
        ConstantData.getMainActivity().ttsBinder.getService().StopSpeak();
    }


    @JavascriptInterface
    public void MWebviewSource(String url, String html) {
        Document doc = Jsoup.parse(html, url);
        String a = doc.html();
        UIClass.ViewSOURCE(a);
    }

    @JavascriptInterface
    public void runChaJian(String path) {
        try {
            MWebView view = webview;
            String fpath = ConstantData.DIYPath + "/js";
            if (!path.contains(fpath)) {
                path = fpath + "/" + path;
            }
            view.loadJavaScriptByPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public String readFile(String path) {
        return FileUtils.readFileContent(path);
    }

    @JavascriptInterface
    public String MWebgetAllLinks(String url, String html) {
        html = MJsoup.getAllLink(url, html);
        Toast.makeText(ConstantData.getContext(), "请稍等片刻", Toast.LENGTH_LONG).show();
        UIClass.ViewWebSouce(html);
        return html;
    }

    @JavascriptInterface
    public void MWebfindHtmlByTag(String url, String html, String tag) {
        html = MJsoup.findHtmlbyTag(url, html, tag);
        Toast.makeText(ConstantData.getContext(), "请稍等片刻\n" + tag, Toast.LENGTH_LONG).show();
        UIClass.ViewWebSouce(html);
    }


    @JavascriptInterface
    public void MWebgetAllText(String url, String html) {
        Document doc = Jsoup.parse(html, url);
        StringBuilder text = new StringBuilder();
        Elements es = doc.getAllElements();
        for (Element e : es) {
            if (!(e.ownText().isEmpty())) {
                text.append(e.ownText()).append("\n");
            }
        }
        UIClass.ViewWebSouce(text.toString());
    }

}
