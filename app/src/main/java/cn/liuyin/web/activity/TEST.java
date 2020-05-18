package cn.liuyin.web.activity;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.Hosts;

/**
 * Created by asus on 2017/9/14.
 */

public class TEST {
    public static void run(Context context) {
        List<Hosts> list = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream ims = assetManager.open("hosts");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ims));
            String line;
            String ip;
            String domain;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\t")) {
                    ip = line.substring(0, line.indexOf("\t"));
                    domain = line.substring(line.indexOf("\t") + 1);
                    Hosts hosts = new Hosts(ip, domain);
                    list.add(hosts);
                }
            }
            ConstantData.getDBManger().addHosts(list);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
