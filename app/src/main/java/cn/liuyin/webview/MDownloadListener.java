package cn.liuyin.webview;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;

import cn.liuyin.tool.LogTool;
import cn.liuyin.tool.PackageTool;
import cn.liuyin.web.constant.ConstantData;

import static cn.liuyin.web.constant.ConstantData.getMainActivity;

public class MDownloadListener implements DownloadListener {


    private static void downloadByADM(String url, String mimetype) {

        if (PackageTool.hasApp("com.dv.adm.pay")) {
            try {

                Intent it = new Intent();
                it.setComponent(new ComponentName("com.dv.adm.pay", "com.dv.adm.pay.AEditor"));
                it.setDataAndType(Uri.parse(url), mimetype);
                ConstantData.getMainActivity().startActivity(it);

            } catch (Exception e) {
                LogTool.writeLog(e.toString());
            }
        } else {
            //Toasty.warning(APPAplication.getContext(),"ADM未安装，现转到酷安下载。").show();
            if (getMainActivity().mainFragement.getWebView1() != null) {
                goToMarket(getMainActivity(), "com.dv.adm.pay");
            }

        }
    }

    /**
     * 去市场下载页面
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void playVideoByX5(String url) {
        if (TbsVideo.canUseTbsPlayer(ConstantData.getContext())) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false); //true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false); //false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2); //1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            TbsVideo.openVideo(ConstantData.getContext(), url, null);


        }
    }

    private static void down_file(String url, String userAgent, String contentDisposition, String mimetype) {


        DownloadManager downloadManager = (DownloadManager) ConstantData.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.addRequestHeader("User-Agent", userAgent);
        request.setMimeType(mimetype);//设置文件的mineType
        request.setDestinationInExternalPublicDir(ConstantData.DownloadPath, URLUtil.guessFileName(url, contentDisposition, mimetype));
        // 通知栏中将出现的内容
        request.setTitle("我的下载");
        request.setDescription("下载一个大文件" + contentDisposition);
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        assert downloadManager != null;
        long download_id = downloadManager.enqueue(request);

    }

    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, long contentLength) {


        final ArrayList<String> list = new ArrayList<>();
        list.add(0, "调用系统下载");
        if (PackageTool.hasApp("com.dv.adm.pay")) {
            list.add(1, "调用ADM下载");
        } else {
            list.add(1, "调用ADM下载(需要下载)");
        }
        list.add(2, "调用Tbs播放器");
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        alert = builder//.setIcon(R.drawable.baidu)
                .setTitle("选择的操作方式")
                .setItems(list.toArray(new String[list.size()]), (dialog, which) -> {
                    //Toasty.info(APPAplication.getContext(),"选择了"+list.get(which)).show();
                    switch (which) {
                        case 0:
                            down_file(url, userAgent, contentDisposition, mimetype);
                            break;
                        case 1:
                            downloadByADM(url, mimetype);
                            break;
                        case 2:
                            playVideoByX5(url);
                            break;
                        default:
                            break;

                    }
                }).create();
        alert.show();


    }


}
