package cn.liuyin.webview;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context p1, Intent p2) {
        // TODO: Implement this method
        long ID = p2.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        //Toast.makeText(ConstantData.getContext(), "任务:" + ID + " 下载完成!", Toast.LENGTH_LONG).show();
        //还未完成
    }

}
