package cn.liuyin.webview;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cn.liuyin.tool.FileTool;
import cn.liuyin.tool.HttpTool;
import cn.liuyin.web.constant.ConstantData;

public class LongPressListenerWrapper implements View.OnLongClickListener {

    private MWebView webview;


    public LongPressListenerWrapper(MWebView webview, Context context) {
        this.webview = webview;
        /*
      一个长按监听器的实例，可以在此实现针对不同长按对象 引导不同的长按处理
     */


    }

    private static void addAsAD(String content) {
        FileWriter fw;
        content += "\n";
        try {
            String file = Environment.getExternalStorageDirectory().getPath() + "Download/adb.txt";
            fw = new FileWriter(file, true);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {

        MWebView.HitTestResult htr = webview.getHitTestResult();
        int type = htr.getType();

        switch (type) {

            //case MWebView.HitTestResult.ANCHOR_TYPE:
            //<14
            //doActionType(htr);
            //Toast.makeText(ConstantData.getContext(), "ANCHOR_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
            // break;
            //case MWebView.HitTestResult.IMAGE_ANCHOR_TYPE:
            //<14
            //Toast.makeText(ConstantData.getContext(), "IMAGE_ANCHOR_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
            // break;
            case MWebView.HitTestResult.EMAIL_TYPE:
                //打开邮箱地址
                Toast.makeText(ConstantData.getContext(), "EMAIL_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();

                break;
            case MWebView.HitTestResult.PHONE_TYPE:
                //打开电话号码
                Toast.makeText(ConstantData.getContext(), "PHONE_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                break;
            case MWebView.HitTestResult.GEO_TYPE:
                Toast.makeText(ConstantData.getContext(), "GEO_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(ConstantData.getContext(), htr.getType() + htr.getExtra(), Toast.LENGTH_SHORT).show();
                doActionType(htr);
                return true;
            case MWebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                Toast.makeText(ConstantData.getContext(), "SRC_IMAGE_ANCHOR_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                doSrcImageLongPressEvent(htr);
                return true;
            case MWebView.HitTestResult.IMAGE_TYPE:
                Toast.makeText(ConstantData.getContext(), "IMAGE_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                return doImageLongPressEvent(htr);
            case MWebView.HitTestResult.EDIT_TEXT_TYPE:
                Toast.makeText(ConstantData.getContext(), "EDIT_TEXT_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                //doTextLongPressEvent(htr);
                break;

            case MWebView.HitTestResult.UNKNOWN_TYPE:

                Toast.makeText(ConstantData.getContext(), "UNKNOWN_TYPE" + htr.getExtra(), Toast.LENGTH_SHORT).show();
                doUnknownAreaPressEvent(htr);
                break;
            default:
                Toast.makeText(ConstantData.getContext(), htr.getType() + htr.getExtra(), Toast.LENGTH_SHORT).show();
                break;

        }

        return false;
    }

    private void doSrcImageLongPressEvent(final MWebView.HitTestResult htr) {

        final String[] list = new String[]{"打开图片", "复制图片链接", "分享图片链接", "屏蔽该图片", "保存图片"};
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        alert = builder//.setIcon(R.drawable.baidu)
                .setTitle("选择对图片的操作")
                .setItems(list, (dialog, which) -> {
                    Toast.makeText(ConstantData.getContext(), "你选择了" + list[which], Toast.LENGTH_SHORT).show();
                    switch (which) {
//
                        case 0:
                            webview.loadUrl(htr.getExtra());
                            break;
                        case 1:
                            copy(ConstantData.getContext(), htr.getExtra());
                            break;
                        case 2:
                            share(ConstantData.getContext(), htr.getExtra());
                            break;
                        case 3:
                            addAsAD(htr.getExtra());
                        case 4:
                            downloadImage(htr);

                            break;


                    }
                }).create();
        alert.show();
    }

    private boolean doImageLongPressEvent(final MWebView.HitTestResult htr) {

        final String[] lesson = new String[]{"新窗口打开图片", "复制图片链接", "分享图片链接", "屏蔽该图片", "保存图片"};
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        alert = builder//.setIcon(R.drawable.baidu)
                .setTitle("选择对图片的操作")
                .setItems(lesson, (dialog, which) -> {
                    Toast.makeText(ConstantData.getContext(), "你选择了" + lesson[which], Toast.LENGTH_SHORT).show();
                    switch (which) {
                        case 0:
                            webview.loadUrl(htr.getExtra());
                            break;
                        case 1:
                            copy(ConstantData.getContext(), htr.getExtra());
                            break;
                        case 2:
                            share(ConstantData.getContext(), htr.getExtra());
                            break;
                        case 3:
                            addAsAD(htr.getExtra());
                            break;
                        case 4:
                            downloadImage(htr);
                            break;
                        default:
                            break;

                    }
                }).create();
        alert.show();
        return true;
    }

    private void doActionType(final MWebView.HitTestResult htr) {
        final String[] lesson = new String[]{"新窗口打开链接", "复制链接", "分享链接"};
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        alert = builder//.setIcon(R.drawable.baidu)
                .setTitle("选择对链接的操作")
                .setItems(lesson, (dialog, which) -> {
                    Toast.makeText(ConstantData.getContext(), "你选择了" + lesson[which], Toast.LENGTH_SHORT).show();
                    switch (which) {
                        case 0:
                            webview.loadUrl(htr.getExtra());
                            break;
                        case 1:
                            copy(ConstantData.getContext(), htr.getExtra());
                            break;
                        case 2:
                            share(ConstantData.getContext(), htr.getExtra());
                            break;

                        default:
                            break;

                    }
                }).create();
        alert.show();
    }

    private void doUnknownAreaPressEvent(final MWebView.HitTestResult htr) {
//        ConstantData.getMainActivity().top_layout.setVisibility(View.VISIBLE);
    }

    public void downloadImage(MWebView.HitTestResult htr) {
        new Thread(() -> {
            File f = new File(ConstantData.DIYPath + "/pictures");
            if (!f.exists()) {
                f.mkdirs();
            }
            FileTool.saveBytes(ConstantData.DIYPath + "/pictures", System.currentTimeMillis() + ".jpg", HttpTool.getBytes(htr.getExtra()));
        }).start();
    }

    private void copy(Context context, String msg) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        assert cm != null;
        ClipData clipData = cm.getPrimaryClip();
        clipData.addItem(new ClipData.Item(msg));
        cm.setPrimaryClip(clipData);
        //cm.setText(msg);

        Toast.makeText(context, "网址已经复制到剪贴板:)", Toast.LENGTH_LONG).show();
    }

    private void share(Context context, String msg) {
        String msgs = "hi，我通过APP发给你一个图片：\n" + msg;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msgs);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

}
