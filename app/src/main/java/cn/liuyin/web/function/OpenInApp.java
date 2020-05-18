package cn.liuyin.web.function;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import cn.liuyin.web.constant.ConstantData;

import static cn.liuyin.web.constant.ConstantData.getMainActivity;

public class OpenInApp {
    public static void open(String url) {
        if (url.startsWith("magnet:?xt=urn:btih:")) {
            openCili(url);
        }
        if (url.startsWith("tel:")) {
            Call(url);
        }
        if (url.startsWith("smsto:")) {
            SendSMSTo(url);
        } else {
            openXunlei(url);
        }
    }

    public static void SendSMSTo(String url) {
        if (url.startsWith("smsto:")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "phoneNumber"));
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setType("vnd.android-dir/mms-sms");
            ConstantData.getContext().startActivity(intent);
        }
    }

    public static void Call(String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent();
            //系统默认的action，用来打开默认的电话界面
            intent.setAction(Intent.ACTION_CALL);
            //需要拨打的号码
            intent.setData(Uri.parse(url));
            ConstantData.getContext().startActivity(intent);
        }
    }

    public static void openXunlei(String url) {

        Toast.makeText(ConstantData.getContext(), url, Toast.LENGTH_LONG).show();
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            ConstantData.getContext().startActivity(i);
            ClipboardManager cm = (ClipboardManager) ConstantData.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            assert cm != null;
            ClipData clipData = cm.getPrimaryClip();
            clipData.addItem(new ClipData.Item(url));
            cm.setPrimaryClip(clipData);
            //cm.setText(url);
        } catch (Exception e) {
            ClipboardManager cm = (ClipboardManager) ConstantData.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            assert cm != null;
            ClipData clipData = cm.getPrimaryClip();
            clipData.addItem(new ClipData.Item(url));
            cm.setPrimaryClip(clipData);
            Toast.makeText(ConstantData.getContext(), "打开应用失败", Toast.LENGTH_LONG).show();
        }
    }

    public static void openCili(String url) {
        if (url.startsWith("magnet:?xt=urn:btih:")) {

            url = url.substring(url.lastIndexOf(":") + 1);
            if (getMainActivity().mainFragement.getWebView1() != null) {
                ConstantData.getMainActivity().mainFragement.getWebView1().loadUrl("http://apiv.ga/magnet/" + url);
            }
        }
    }

    public void sendEmail(String url) {
        if (url.startsWith("mailto:")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            ConstantData.getContext().startActivity(intent);
        }
    }
}
