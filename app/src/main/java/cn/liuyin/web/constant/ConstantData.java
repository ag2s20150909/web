package cn.liuyin.web.constant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import cn.liuyin.web.activity.MainActivity;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.webview.MWebChromeClient;
import cn.liuyin.webview.MWebViewClient;

/**
 * Created by 刘银（本地） on 2017/4/28 0028.
 */

public class ConstantData {

    public final static String SDPath = Environment.getExternalStorageDirectory().getPath();
    public final static String DataPath = Environment.getDataDirectory().getPath();
    //SD卡路径
    public final static String AndroidFilePath = SDPath + "/Android/data/" + "cn.liuyin.web" + "/files";
    public final static String DownloadPath = SDPath + "/Download";
    public final static String DIYPath = DownloadPath + "/Web";
    //Android 储存目录b
    public final static String AdPath = AndroidFilePath + "/ads";
    public final static String SearchEngine = "SearchEngine";
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainactivity;
    @SuppressLint("StaticFieldLeak")
    private static MWebViewClient mWebViewClient;
    @SuppressLint("StaticFieldLeak")
    private static MWebChromeClient mWebChromeClient;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static DBManager db;

    public static MWebChromeClient getmWebChromeClient() {

        return mWebChromeClient;
    }

    public static void setmWebChromeClient(MWebChromeClient mWebChromeClient) {
        ConstantData.mWebChromeClient = mWebChromeClient;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context contexts) {
        context = contexts;
    }

    public static MainActivity getMainActivity() {
        return mainactivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        mainactivity = mainActivity;
    }

    public static MWebViewClient getMWebViewClient() {
        return mWebViewClient;
    }

    public static void setMWebViewClient(MWebViewClient mWebViewClient1) {
        mWebViewClient = mWebViewClient1;
    }

    public static DBManager getDBManger() {
        if (db == null) {
            db = new DBManager(context);
        }
        return db;
    }

    public static void setDBManger(DBManager ndb) {
        db = ndb;
    }
}

