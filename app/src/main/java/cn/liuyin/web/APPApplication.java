package cn.liuyin.web;

import android.app.Application;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.ArrayList;

import cn.liuyin.tool.CrashHandler;
import cn.liuyin.tool.FileTool;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.ADdiv;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.function.UIClass;
import cn.liuyin.webview.ADFilterTool;


public class APPApplication extends Application {


    Runnable updata = () -> {

        if (!new File(ConstantData.DIYPath + "/js").exists()) {
            new File(ConstantData.DIYPath + "/js").mkdirs();
        }
        FileTool.copyFilesFromAssets(APPApplication.this, "ad", APPApplication.this.getExternalFilesDir("ads").getPath());
        FileTool.copyFilesFromAssets(APPApplication.this, "js", APPApplication.this.getExternalFilesDir("www").getPath() + "/js");
        FileTool.copyFilesFromAssets(APPApplication.this, "css", APPApplication.this.getExternalFilesDir("www").getPath() + "/css");
        if (!new File(ConstantData.DIYPath, "data.txt").exists()) {
            FileTool.copyFilesFromAssets(APPApplication.this, "data/data.txt", ConstantData.DIYPath + "/data.txt");
        }
        if (!new File(ConstantData.DIYPath, "adlist.txt").exists()) {
            FileTool.copyFilesFromAssets(APPApplication.this, "data/adlist.txt", ConstantData.DIYPath + "/adlist.txt");
        }
        if (!new File(ConstantData.DIYPath, "addiv.txt").exists()) {
            FileTool.copyFilesFromAssets(APPApplication.this, "data/addiv.txt", ConstantData.DIYPath + "/addiv.txt");
        }
        //TEST.run(APPApplication.this);
    };

    public static void initADHost() {
        //资源请求过滤
        ArrayList<String> adlist = new ArrayList<>();
        adlist.addAll(ADFilterTool.ReadTxtFiles(ConstantData.AdPath + "/adlist.txt"));
        adlist.addAll(ADFilterTool.ReadTxtFiles(ConstantData.DIYPath + "/adlist.txt"));
        ConstantData.getDBManger().addADHost(adlist);
    }

    public static void initAdDiv() {
        ArrayList<ADdiv> addiv;//广告div标识
        addiv = new ArrayList<>();
        addiv.addAll(ADFilterTool.getAddiv(ConstantData.AdPath + "/addiv.txt"));
        addiv.addAll(ADFilterTool.getAddiv(ConstantData.DIYPath + "/addiv.txt"));
        ConstantData.getDBManger().addAdDiv(addiv);

    }

    @Override
    public void onCreate() {

        super.onCreate();
        //startService(new Intent(this, MyService.class));
        CrashHandler.getInstance().init(this)
                .setOnCrashListener((context, errorMsg) -> {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    UIClass.ViewError(errorMsg, true);
                })
                .setCrashSave()
                .setCrashSaveTargetFolder(this.getExternalFilesDir("crash").getPath());


        ConstantData.setDBManger(new DBManager(this));
        Thread t = new Thread(updata);
        t.start();

        initTBS();
        initADHost();
        initAdDiv();


    }

    private void initTBS() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                //Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }


}
