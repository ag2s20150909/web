package cn.liuyin.webview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

import cn.liuyin.tool.LogTool;
import cn.liuyin.web.R;
import cn.liuyin.web.activity.MainActivity;
import cn.liuyin.web.constant.ConstantData;

import static cn.liuyin.web.activity.WebViewFragment.FILE_CHOOSER_RESULT_CODE;

//import android.webkit.GeolocationPermissions;


public class MWebChromeClient extends WebChromeClient {

    protected FrameLayout mFullscreenContainer;
    Context mContext;
    private MWebView mWebView;
    private MainActivity mActivity;
    private View mCustomView;
    private int mOriginalSystemUiVisibility;
    private int mOriginalOrientation;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

    public MWebChromeClient(Context context, MWebView webView) {
        ConstantData.setmWebChromeClient(this);
        this.mContext = context;
        this.mWebView = webView;
        mActivity = ConstantData.getMainActivity();

    }


    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String log = "行数：" + consoleMessage.lineNumber() + "\n" +
                "级别：" + consoleMessage.messageLevel() + "\n" +
                "资源：" + consoleMessage.sourceId() + "\n" +
                "调试：" + consoleMessage.message() + "\n";
        System.out.println(log);
        LogTool.writeLog(log);
        //Toast.makeText(context,log,Toast.LENGTH_LONG).show();
        return super.onConsoleMessage(consoleMessage);
    }


    @Override
    public void onReceivedTitle(WebView p1, String p2) {
        // TODO: Implement this method
        Intent intent = new Intent("WEBVIEW_MAIN");
        intent.putExtra("KEY", "CHANGE_TITLE");
        intent.putExtra("data", p2);
        p1.getContext().sendBroadcast(intent);
//        ConstantData.getMainActivity().web_title.setText(p2);
        super.onReceivedTitle(p1, p2);

    }

    @Override
    public void onProgressChanged(WebView p1, int p2) {
        // TODO: Implement this method
        super.onProgressChanged(p1, p2);
        MainActivity main = ConstantData.getMainActivity();

        //main.webprogress = p2;


    }


    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        ConstantData.getMainActivity().mainFragement.uploadMessage = valueCallback;
        openImageChooserActivity();
    }

    // For Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        ConstantData.getMainActivity().mainFragement.uploadMessage = valueCallback;
        //openImageChooserActivity();
    }

    //For Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        ConstantData.getMainActivity().mainFragement.uploadMessage = valueCallback;
        openImageChooserActivity();
    }

    // For Android >= 5.0
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        ConstantData.getMainActivity().mainFragement.uploadMessageAboveL = filePathCallback;
        openImageChooserActivity();
        return true;
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //i= createCameraIntent();
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        ConstantData.getMainActivity().startActivityForResult(Intent.createChooser(i, "选择文件"), FILE_CHOOSER_RESULT_CODE);
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalDataDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath() +
                File.separator + "browser-photos");
        cameraDataDir.mkdirs();
        String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator +
                System.currentTimeMillis() + ".jpg";
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
        return cameraIntent;
    }

    //====================全屏播放视频===================================================
    @Override
    public Bitmap getDefaultVideoPoster() {
        if (mActivity == null) {
            return null;
        }

        return BitmapFactory.decodeResource(mActivity.getApplicationContext().getResources(),
                R.drawable.ic_video);
    }

    // 播放网络视频时全屏会被调用的方法
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (mCustomView != null) {
            onHideCustomView();
            return;
        }


        // 1. Stash the current state
        mCustomView = view;
        mCustomView.setBackgroundColor(Color.BLACK);
        mOriginalSystemUiVisibility = mActivity.getWindow().getDecorView().getSystemUiVisibility();
        mOriginalOrientation = mActivity.getRequestedOrientation();

        // 2. Stash the custom view callback
        mCustomViewCallback = callback;

        // 3. Add the custom view to the view hierarchy
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.addView(mCustomView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setVisibility(View.INVISIBLE);

        // 4. Change the state of the window
        mActivity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    // 视频播放退出全屏会被调用的
    @Override
    public void onHideCustomView() {
        // 1. Remove the custom view
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mCustomView);
        mCustomView = null;

        // 2. Restore the state to it's original form
        mActivity.getWindow().getDecorView()
                .setSystemUiVisibility(mOriginalSystemUiVisibility);
        mWebView.setVisibility(View.VISIBLE);
        mActivity.setRequestedOrientation(mOriginalOrientation);

        // 3. Call the custom view callback
        mCustomViewCallback.onCustomViewHidden();
        mCustomViewCallback = null;
    }

    //====================全屏播放视频===================================================


    //=========HTML5定位==========================================================
    //需要先加入权限
    //<uses-permission android:name="android.permission.INTERNET"/>
    //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

    //在系统模式下工作
    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin,
                                                   final GeolocationPermissionsCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("允许访问位置信息吗？");
        DialogInterface.OnClickListener dialogButtonOnClickListener = (dialog, clickedButton) -> {
            if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
                callback.invoke(origin, true, true);
            } else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
                callback.invoke(origin, false, false);
            }
        };
        builder.setCancelable(true);
        builder.setPositiveButton("允许", dialogButtonOnClickListener);
        builder.setNegativeButton("拒绝", dialogButtonOnClickListener);
        builder.setOnCancelListener(dialog -> callback.invoke(origin, false, false));
        builder.show();
        if (mWebView.getView() instanceof android.webkit.WebView) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            //builder.show();
        } else {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            //builder.show();
            //super.onGeolocationPermissionsShowPrompt(origin, callback);
        }


    }

    //=========HTML5定位==========================================================

    /**
     * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
     */

    @Override
    public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());

        builder.setTitle("通知")
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> jsResult.confirm());

        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
//        builder.setOnKeyListener((dialog, keyCode, event) -> {
//            //Log.v("onJsAlert", "keyCode==" + keyCode + "event="+ event);
//            return true;
//        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        jsResult.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        return true;
        // return super.onJsAlert(view, url, message, result);
        //return super.onJsAlert(webView, s, s1, jsResult);
    }

    /**
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("对话框")
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> result.confirm())
                .setNeutralButton("取消", (dialog, which) -> result.cancel());
        builder.setOnCancelListener(dialog -> result.cancel());

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
//        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//                //Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
//                return true;
//            }
//        });
        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
     * window.prompt('请输入您的域名地址', '618119.com');
     */
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, final JsPromptResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("对话框").setMessage(message);

        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        builder.setView(et)
                .setPositiveButton("确定", (dialog, which) -> result.confirm(et.getText().toString()))
                .setNeutralButton("取消", (dialog, which) -> result.cancel());

//        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
//        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//                //Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
//                return true;
//            }
//        });

        // 禁止响应按back键的事件
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsPrompt(view, url, message, defaultValue,
        // result);
    }

    @Override
    public boolean onJsTimeout() {
        super.onJsTimeout();
        Toast.makeText(mContext, "JS time out ...", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onReachedMaxAppCacheSize(long l, long l1, WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(l, l1, quotaUpdater);
    }


}
