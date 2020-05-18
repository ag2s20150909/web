package cn.liuyin.webview;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;

public class WebviewUA {
    private static final String TAG = "WebviewUA";
    //static String brand= android.os.Build.BRAND;// 手机品牌
    private static String model = android.os.Build.MODEL; // 手机型号
    private static String vesion = android.os.Build.VERSION.RELEASE;
    private static String id = android.os.Build.ID;

    public static void SwitchUA() {
        final SharedPreferences.Editor editor;
        SharedPreferences pref;
        pref = ConstantData.getContext().getSharedPreferences("MyWebData", Context.MODE_PRIVATE);
        editor = pref.edit();
        String ua = pref.getString("UserAgent", WebviewUA.getDefaultUA());
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        final String[] name = new String[]{"默认", "UC浏览器", "Iphone", "Ipad", "Symbian", "Mac PC", "Linux PC", "Windows PC"};
        final String[] uas = new String[]{
                getDefaultUA(),//百度0
                getUCUA(),//谷歌1
                getIphoneUA(),//必应2
                getIpadUA(),//神马3
                getSymbianUA(),//好搜4
                getMacUA(),//搜狗5
                getLinuxPCUA(),
                getWindowsPCUA()
        };
        int a = 0;
        if (ua == null) {
            Log.e(TAG, "ua is null");
        }
        if (getDefaultUA() == null) {
            Log.e(TAG, "DefaultUA is null");
        }
        try {
            for (int i = 0; i < uas.length; i++) {
                if (uas[i].contains(ua)) {
                    a = i;
                    break;
                }
            }
            alert = builder.setIcon(R.drawable.ic_baidu)
                    .setTitle("选择UA")
                    .setSingleChoiceItems(name, a, (dialog, which) -> {
                        Toast.makeText(ConstantData.getContext(), "你选择了" + name[which], Toast.LENGTH_SHORT).show();
                        editor.putString("UserAgent", uas[which]).apply();
                        if (ConstantData.getMainActivity().mainFragement.getWebView1() != null) {
                            ConstantData.getMainActivity().mainFragement.getWebView1().setUA(uas[which]);
                        }

                    }).create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDefaultUA() {
        String ua = com.tencent.smtt.sdk.WebSettings.getDefaultUserAgent(ConstantData.getContext());
        String ua2 = android.webkit.WebSettings.getDefaultUserAgent(ConstantData.getContext());
        ua = (ua != null) ? ua : ua2;
        return ua;
    }
    //Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; vivo Y66 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.5.5.943 Mobile Safari/537.36

    public static String getUCUA() {
        return "Mozilla/5.0 (Linux; U; Android " + vesion + "; zh-CN; " + model + " Build/" + id + ")" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.20 UCBrowser/11.5.5.943 Mobile Safari/537.36";
    }

    public static String getIphoneUA() {
        return "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5";
    }

    public static String getWindowsPCUA() {
        return "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36";
    }

    public static String getLinuxPCUA() {
        return "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.83 Safari/537.36";
    }

    public static String getMacUA() {
        return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.54 Safari/537.36";
    }

    public static String getIpadUA() {
        return "Mozilla/5.0 (iPad; CPU OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\n";
    }

    public static String getSymbianUA() {
        return "Nokia5250/10.0.011 (SymbianOS/9.4; U; Series60/5.0 Mozilla/5.0; Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/525 (KHTML, like Gecko) Safari/525 3gpp-gba";
    }
    //


}
