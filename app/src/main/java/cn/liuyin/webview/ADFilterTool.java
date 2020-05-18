package cn.liuyin.webview;


import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.ADdiv;


public class ADFilterTool {
    private ArrayList<String> adlist;//广告暂存数组
    private Context mContext;

    public ADFilterTool(Context context) {
        mContext = context;
        adlist = ConstantData.getDBManger().getAllHosts();

    }

    public static ArrayList<String> ReadTxtFiles(String Path) {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader br;
        try {
            File file = new File(Path);
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                line = line.trim();
                if (!line.isEmpty()) {
                    list.add(line);
                }
            }
            br.close();
            //}

        } catch (Exception e) {
            e.printStackTrace();
            list.add("/ad/");
        }

        return list;
    }

    public static ArrayList<ADdiv> getAddiv(String Path) {
        ArrayList<ADdiv> list = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(Path)));

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") && line.contains("$")) {
                    System.out.println(line);
                    String host = line.substring(line.indexOf("#") + 1, line.indexOf("$"));
                    String fiter = line.substring(line.indexOf("$") + 1);
                    ADdiv addiv = new ADdiv(host, fiter);
                    list.add(addiv);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<String> ReadDivTxtFiles(String Path) {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(Path)));

            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                line = line.trim();
                if (line.startsWith("#")) {
                    list.add(line);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //v_ads
    public static void getClearAdDivJs(MWebView mwebview) {
        String host = mwebview.getUrl();

        try {
            host = new URL(mwebview.getUrl()).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<String> list = ConstantData.getDBManger().getFiterByUrl(host);
        StringBuilder js = new StringBuilder("(function(){");
        String[] adDivs = list.toArray(new String[list.size()]);
        for (int i = 0; i < adDivs.length; i++) {

            js.append("" + "try{" + "var AD").append(i).append("= document.querySelectorAll('").append(adDivs[i]).append("');").append("for(var j=0;j<AD").append(i).append(".length;j++){").append(
                    //"var adDiv" + i + "j=AD" + i + "[j];" +
                    "if(AD").append(i).append("[j] != null){").append("AD").append(i).append("[j].innerHTML='';").append("AD").append(i).append("[j].parentNode.removeChild(AD").append(i).append("[j]);}").append("}").append("}").append("catch(error){console.log(error.toString());}");
        }
        js.append("})();");
        mwebview.loadJavaScript(js.toString());
        //return js.toString();
    }

    private static boolean fit(String regex, String str) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    public void update() {
        //System.out.println("[[[["+System.currentTimeMillis());
        adlist = ConstantData.getDBManger().getAllHosts();
        //System.out.println("[[[["+System.currentTimeMillis());
    }

    public boolean Go(String url) {
        return (hasAD(mContext, url) || hasADs(url, adlist)) && (!url.contains("file:///"));
    }

    //内置屏蔽
    private boolean hasAD(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (fit(adUrl, url)) {
                return true;
            }
        }
        return false;
    }

    //外置屏蔽
    private boolean hasADs(String url, ArrayList<String> adhosts) {
        for (String adUrl : adhosts) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }


}
