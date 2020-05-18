package cn.liuyin.web.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.liuyin.tool.FileTool;
import cn.liuyin.tool.QQUrlScheme;
import cn.liuyin.web.APPApplication;
import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.database.ShuQian;
import cn.liuyin.web.function.TBSTool;
import cn.liuyin.web.function.UIClass;
import cn.liuyin.web.listener.MMenu;
import cn.liuyin.web.listener.MOnClickListener;
import cn.liuyin.web.listener.MOnLongClickListener;
import cn.liuyin.web.pages.HomePage;
import cn.liuyin.web.pages.MBookMark;
import cn.liuyin.web.pages.MHistory;
import cn.liuyin.webview.ComendBean;
import cn.liuyin.webview.MWebPicture;
import cn.liuyin.webview.MWebView;
import cn.liuyin.webview.MWebViewClient;
import cn.liuyin.webview.WebFunction;
import cn.liuyin.webview.WebviewUA;

import static android.app.Activity.RESULT_OK;

/**
 * Created by asus on 2017/9/7.
 */

public class WebViewFragment extends Fragment {
    public final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private static final String TAG = WebViewFragment.class.getSimpleName();
    private static final int MSG_SUCCESS = 1;// 获取成功的标识
    private static final int MSG_FAILURE = 0;// 获取失败的标识
    public String homeurl;
    public ValueCallback uploadMessage;
    public ValueCallback<Uri[]> uploadMessageAboveL;
    //UI控件部分
    @BindView(R.id.top_title)
    public LinearLayout top_title;
    @BindView(R.id.top_secrch)
    public LinearLayout top_search;
    @BindView(R.id.top_layout)
    public LinearLayout top_layout;
    @BindView(R.id.web_title)
    public TextView web_title;
    @BindView(R.id.search_text)
    public EditText search_text;
    @BindView(R.id.web_icon)
    public Button img_webicon;
    @BindView(R.id.search_icon)
    public Button img_searchicon;
    @BindView(R.id.web_exit)
    public Button btn_exit;
    @BindView(R.id.web_goback)
    public Button btn_goback;
    @BindView(R.id.web_goforward)
    public Button btn_goforward;
    @BindView(R.id.web_refresh)
    public Button btn_refresh;
    @BindView(R.id.web_search)
    public Button btn_search;
    @BindView(R.id.fragment_main_webview)
    public FrameLayout web_view_framelayout;
    @BindView(R.id.btn_cleartext)
    Button btn_cleartext;
    /////////////////////////
    Bundle bundle1;
    Bundle webviewBundle;

    //View mrootView;
    Handler timehandler = new Handler();
    private MainReceiver mainreceiver = new MainReceiver();
    private MWebView mWebView;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                // 如果成功，则显示从网络获取到字符串
                case MSG_SUCCESS:
                    doTask((ComendBean) msg.obj);
                    break;


                case MSG_FAILURE:
                    Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_LONG).show();

                    break;
            }

            return false;
        }
    });
    private Runnable timerunnable = new Runnable() {
        public void run() {
            timehandler.postDelayed(timerunnable, 1000);
            MainActivity main = (MainActivity) getActivity();

            if (mWebView != null) {

                //返回键
                if (mWebView.canGoBack()) {
                    btn_exit.setVisibility(View.GONE);
                    btn_goback.setVisibility(View.VISIBLE);
                } else {
                    btn_goback.setVisibility(View.GONE);
                    btn_exit.setVisibility(View.VISIBLE);
                }
            }


        }
    };

    public WebViewFragment() {
        super();
    }

    public MWebView getWebView1() {
        if (mWebView == null) {
            this.getActivity().finish();
        }
        return mWebView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bundle=savedInstanceState;
        homeurl = getWWWPath("index.html");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fargment_webview, container, false);
        ButterKnife.bind(this, rootView);

        // Get reference of WebView from layout/activity_main.xml
        //mWebView = (MWebView) rootView.findViewById(R.id.fragment_main_webview);
        mWebView = new MWebView(this.getActivity(), null);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        web_view_framelayout.addView(mWebView);
        mWebView.setMainFragmetn(this);

        // Check whether we're recreating a previously destroyed instance
        if (webviewBundle != null) {
            // Restore the previous URL and history stack
            mWebView.restoreState(webviewBundle);
        } else {
            mWebView.loadUrl(homeurl);
        }
        initAction();

        // Load the local index.html file
        if (mWebView.getUrl() == null) {

            mWebView.loadUrl(homeurl);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        mWebView.setMWebViewDayOrNight(((MainActivity) getActivity()).pref.getBoolean("DayOrNight", true), false);
        //注册广播
        IntentFilter mainfilter = new IntentFilter();
        mainfilter.addAction("WEBVIEW_MAIN");
        getActivity().registerReceiver(mainreceiver, mainfilter);
        super.onStart();
    }

    @Override
    public void onResume() {
        timehandler.removeCallbacks(timerunnable);
        timehandler.postDelayed(timerunnable, 100);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webviewBundle = new Bundle();
        mWebView.saveState(webviewBundle);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStop() {
        timehandler.removeCallbacks(timerunnable);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //bundle.putString("lasturl",mWebView.getUrl());
        //去除广播
        getActivity().unregisterReceiver(mainreceiver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //bundle=savedInstanceState;
        new TBSTool(getActivity());
        top_layout.setVisibility(View.VISIBLE);
        if (webviewBundle != null) {
            mWebView.restoreState(webviewBundle);
        }

    }

    @Override
    public void onDestroy() {
        //bundle.putString("lasturl",mWebView.getUrl());
        mWebView.saveState(webviewBundle);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(webviewBundle);
        //bundle=outState;
        //bundle.putString("lasturl",mWebView.getUrl());
    }

    public void run(Intent intent) {
        //homeurl = "file://" + getActivity().getExternalFilesDir("www") + "/index.html";
        homeurl = "x5://home";

        String mIntentUrl;
        if (intent != null) {

            try {
                mIntentUrl = intent.getData().toString();
                mIntentUrl = WebFunction.search(mIntentUrl, ((MainActivity) getActivity()).pref.getString("SearchEngine", "http://m.baidu.com/s?word=%s"));
                mWebView.loadUrl(mIntentUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //mWebView.loadUrl(mIntentUrl);


    }

    private void initAction() {
        Context context = this.getActivity();
        btn_exit.setOnClickListener(new MOnClickListener(context));
        btn_exit.setOnLongClickListener(new MOnLongClickListener());
        btn_goback.setOnClickListener(new MOnClickListener(context));
        btn_goback.setOnLongClickListener(new MOnLongClickListener());
        btn_goforward.setOnClickListener(new MOnClickListener(context));
        btn_goforward.setOnLongClickListener(new MOnLongClickListener());
        btn_refresh.setOnClickListener(new MOnClickListener(context));
        btn_refresh.setOnLongClickListener(new MOnLongClickListener());
        btn_search.setOnClickListener(new MOnClickListener(context));
        btn_search.setOnLongClickListener(new MOnLongClickListener());
        web_title.setOnClickListener(new MOnClickListener(context));
        web_title.setOnLongClickListener(new MOnLongClickListener());
        img_webicon.setOnClickListener(new MOnClickListener(context));
        img_webicon.setOnLongClickListener(new MOnLongClickListener());
        img_searchicon.setOnClickListener(new MOnClickListener(context));
        img_searchicon.setOnLongClickListener(new MOnLongClickListener());
        btn_cleartext.setOnClickListener(new MOnClickListener(context));
        btn_cleartext.setOnLongClickListener(new MOnLongClickListener());
    }

    public String getWWWPath(String name) {
        return "file://" + this.getActivity().getExternalFilesDir("www") + "/" + name;
    }

    public void search(String msg) {
        System.out.println("<<<<<<<<<<<" + msg);
        top_title.setVisibility(View.VISIBLE);
        top_search.setVisibility(View.GONE);
        String searchengine = ((MainActivity) getActivity()).pref.getString("SearchEngine", "http://m.baidu.com/s?word=%s");
        if (!mWebView.getUrl().equals(msg)) {
            mWebView.loadUrl(WebFunction.search(msg, searchengine));
            Toast.makeText(getActivity(), "开始发送请求", Toast.LENGTH_SHORT).show();
            search_text.setText(mWebView.getUrl());
        }
    }

    public void share(String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        getActivity().startActivity(Intent.createChooser(intent, "分享到"));
    }

    public void listJavaScript() {
        File f = new File(ConstantData.DIYPath + "/js");
        if (!f.exists()) {
            f.mkdirs();
        }
        String[] list = f.list();
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        alert = builder//.setIcon(R.drawable.baidu)
                .setTitle("选择要运行的脚本")
                .setItems(list, (dialog, which) -> {
                    Toast.makeText(ConstantData.getContext(), "你选择了" + list[which], Toast.LENGTH_SHORT).show();
                    mWebView.loadJavaScriptByPath(f.getAbsolutePath() + "/" + list[which]);
                }).create();
        alert.show();
    }

    public void findHtmlByTag() {
        final EditText inputServer = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("查找元素").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        AlertDialog.Builder builder1 = builder.setPositiveButton("确定", (dialog, which) -> {
            String tag = inputServer.getText().toString();
            //Toast.makeText(ConstantData.getContext(),"输入的内容为："+tag,1).show();
            mWebView.loadJavaScript("window.Android.MWebfindHtmlByTag(window.document.URL,window.document.documentElement.outerHTML,'" + tag + "');");
        });
        builder.show();
    }

    public void runJavascript() {

        final EditText inputServer = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("运行脚本").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        AlertDialog.Builder builder1 = builder.setPositiveButton("确定", (dialog, which) -> {
            String tag = inputServer.getText().toString();
            //Toast.makeText(ConstantData.getContext(),"输入的内容为："+tag,1).show();
            if (!tag.isEmpty()) {
                mWebView.loadJavaScript(tag);
            }
        });
        builder.show();
    }

    public void setNightMode() {
        boolean isDay = ((MainActivity) getActivity()).pref.getBoolean("DayOrNight", true);
        try {
            if (isDay) {
                mWebView.setMWebViewDayOrNight(false, false);
                //Toasty.info(main,"夜间模式已开启").show();
                Toast.makeText(getActivity(), "夜间模式已开启", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).editor.putBoolean("DayOrNight", false).apply();
            } else {
                mWebView.setMWebViewDayOrNight(true, false);
                //Toasty.info(main,"夜间模式已关闭").show();
                Toast.makeText(getActivity(), "夜间模式已关闭", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).editor.putBoolean("DayOrNight", true).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//////////////////////////////////////////////////////////////////////////

    public void showHttpRequestLog() {
        MWebViewClient mWebViewClient = ConstantData.getMWebViewClient();
        ArrayList<String> log = mWebViewClient.httprequest;
        StringBuilder mse = new StringBuilder();
        for (String temp : log) {
            mse.append(temp).append("\n\n");
        }
        UIClass.ViewWebSouce(mse.toString());
    }

    public void cutWebPicture() {
        MWebPicture.saveScreenshots(mWebView);
    }

    public void longCutWebPicture() {
        MWebPicture.saveLongScreenshots(mWebView);
    }

    public void switchWebView(boolean useX5) {
        if (useX5) {
            QbSdk.unForceSysWebView();
        } else {
            QbSdk.forceSysWebView();
        }
    }

    public void switchWebView() {
        if (mWebView.IsX5WebView()) {
            switchWebView(false);
            Toast.makeText(getActivity(), "切换为系统内核", Toast.LENGTH_SHORT).show();
        } else {
            switchWebView(true);
            Toast.makeText(getActivity(), "切换为X5内核", Toast.LENGTH_SHORT).show();
        }
    }

    private void webViewSet(MWebView newWebView, Message message) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) message.obj;
        transport.setWebView(newWebView);
        message.sendToTarget();
    }

    public void addBookMark() {

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(p);
        TextView title_t = new TextView(getActivity());
        title_t.setLayoutParams(p);
        title_t.setText("标题");
        root.addView(title_t);
        EditText title_i = new EditText(getActivity());
        title_i.setText(mWebView.getTitle());
        title_i.setLayoutParams(p);
        root.addView(title_i);
        TextView url_t = new TextView(getActivity());
        url_t.setText("链接");
        url_t.setLayoutParams(p);
        root.addView(url_t);
        EditText url_i = new EditText(getActivity());
        url_i.setText(mWebView.getUrl());
        url_i.setLayoutParams(p);
        root.addView(url_i);

        AlertDialog.Builder ad1 = new AlertDialog.Builder(getActivity());
        ad1.setTitle("增加书签:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(root);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                ShuQian sq = new ShuQian();
                sq.title = title_i.getText().toString();
                sq.url = url_i.getText().toString();
                DBManager db = ConstantData.getDBManger();
                db.addShuQian(sq);


            }
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }

    public void addGuangao() {

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(p);
        TextView title_t = new TextView(getActivity());
        title_t.setGravity(Gravity.CENTER);
        title_t.setLayoutParams(p);
        title_t.setText("添加广告地址");
        //root.addView(title_t);
        TextView url_t = new TextView(getActivity());
        url_t.setText("链接");
        url_t.setLayoutParams(p);
        //root.addView(url_t);
        EditText url_i = new EditText(getActivity());
        url_i.setText(mWebView.getUrl());
        url_i.setLayoutParams(p);
        root.addView(url_i);

        AlertDialog.Builder ad1 = new AlertDialog.Builder(getActivity());
        ad1.setTitle("添加广告地址:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(root);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

                FileTool.saveString(ConstantData.DIYPath, "adlist.txt", url_i.getText().toString(), true);


            }
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }
    /////////////////////////////////////////////////////////////////////////////

    private void doTask(ComendBean cmd) {
        switch (cmd.key) {
            case "HIDE_TOOL":
                top_layout.setVisibility(View.GONE);
                break;
            case "SHOW_TOOL":
                top_layout.setVisibility(View.VISIBLE);
                break;
            case "SHOW_SEARCH"://显示搜索工具栏
                search_text.setText(mWebView.getUrl());
                top_title.setVisibility(View.GONE);
                top_search.setVisibility(View.VISIBLE);
                break;

            case "SHOW_SETTING"://显示标题栏
                MMenu.showMenu(getActivity(), img_webicon);
                search_text.setText(mWebView.getUrl());
                break;
            case "SHOW_TITLE":
                top_title.setVisibility(View.VISIBLE);
                top_search.setVisibility(View.GONE);
                break;
            case "CHANGE_TITLE"://改变标题 data为标题内容
                web_title.setText(cmd.data);
                break;
            case "WEB_GO"://使webview转到
                mWebView.loadUrl(cmd.data);
                break;
            case "WEB_GO_BACK"://使webview返回一步
                mWebView.goBack();
                break;
            case "WEB_GO_FORWARD"://使webview前进一步
                mWebView.goForward();
                break;
            case "DO_BACK_PRESSED":
                mWebView.doBackPressed();//处理webview返回事件
                break;
            case "WEB_REFRESH"://webview刷新
                mWebView.reload();
                APPApplication.initADHost();
                APPApplication.initAdDiv();
                ConstantData.getMWebViewClient().update();
                break;
            case "WEB_SEARCH"://默认搜索
                search(search_text.getText().toString());
                break;
            case "WEB_SEARCH_T"://带参数搜索
                search(cmd.data);
                break;
            case "GO_HOME"://转到首页
                new HomePage(getActivity());
                mWebView.loadUrl(getWWWPath("index.html"));
                break;
            case "GO_HISTORY"://转到历史纪录
                new MHistory(getActivity());
                mWebView.loadUrl("file://" + getActivity().getExternalFilesDir("www") + "/history.html");
                break;
            case "CLEAR_INPUT"://清除输入内容
                search_text.setText("");
                break;
            case "EXIT"://退出应用
                //getActivity().stopService(new Intent(getActivity(),MyService.class));
                getActivity().finish();
                System.exit(0);
                break;
            case "SWITCH_UA"://切换ua
                WebviewUA.SwitchUA();
                break;
            case "VIEW_WEB_SOUCE_HTTP":
                new Thread(mWebView.soucerunnable).start();
                break;
            case "VIEW_WEB_SOUCE_JS":
                mWebView.loadJavaScript("window.Android.MWebviewSource(window.document.URL,window.document.documentElement.outerHTML);");
                break;
            case "VIEW_WEB_SOUCE_CHEOME":
                mWebView.loadUrl("view-source:" + mWebView.getUrl());
                break;
            case "WEB_SHARE":
                share(mWebView.getTitle() + "\n" + mWebView.getUrl());
                break;
            case "WEB_SHARE_QQ":
                String data = QQUrlScheme.ShareToFriends(mWebView.getUrl(),
                        "http://cn.bing.com/ImageResolution.aspx?w=768&h=1366",
                        mWebView.getTitle(), "图文无关：" + mWebView.getTitle());
                Intent i = new Intent();
                i.setData(Uri.parse(data));
                getActivity().startActivity(i);
                break;
            case "WEB_GET_ALL_LINK":
                mWebView.loadJavaScript("window.Android.MWebgetAllLinks(window.document.URL,window.document.documentElement.outerHTML);");
                break;
            case "LIST_JAVASCRIPT":
                listJavaScript();
                break;
            case "RUN_JAVASCRIPT":
                runJavascript();
                break;
            case "FIND_HTML_BY_TAG":
                findHtmlByTag();
                break;
            case "SWITCH_NIGHT_MODE":
                setNightMode();
                break;
            case "SET_VIDEO_FULL_SCREEN":
                mWebView.setVideoFullScreen(!((MainActivity) getActivity()).pref.getBoolean("VideoFullScreen", false));
                break;
            case "CLEAR_WEBVIEW_CACHE":
                mWebView.clearWebViewCache();
                break;
            case "CLEAR_COOKIE":
                mWebView.clearCookie();
                break;
            case "SHOW_HTTP_LOG":
                showHttpRequestLog();
                break;
            case "LONG_CUT":
                longCutWebPicture();
                break;
            case "SHORT_CUT":
                cutWebPicture();
                break;
            case "GET_VIP_VIDEO":
                mWebView.getVipVideo();
                break;
            case "SET_TTS":
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                break;
            case "SHOW_SHUQIAN":
                new MBookMark(getActivity());
                mWebView.loadUrl("file://" + getActivity().getExternalFilesDir("www") + "/bookmark.html");
                break;
            case "ADD_SHUQIAN":
                addBookMark();
                break;
            case "ADD_ADLIST":
                addGuangao();
                break;


            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //文件上传
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        //文件上传
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    private class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context p1, Intent p2) {

            ComendBean cmd = new ComendBean();
            cmd.key = p2.getStringExtra("KEY");
            if (p2.getStringExtra("data") != null) {
                cmd.data = p2.getStringExtra("data");
            }
            mHandler.obtainMessage(MSG_SUCCESS, cmd).sendToTarget();


        }

    }


}
