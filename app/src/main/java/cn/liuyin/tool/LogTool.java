package cn.liuyin.tool;


import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class LogTool {
    /**
     * @param content 要写入的信息
     */
    public static void writeLog(String content) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/X5Web";
        FileWriter fw;
        content += "\n";
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            String file = path + "/app.log";
            fw = new FileWriter(file, true);
            fw.write(getTime() + content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 返回当前时间。
     */
    private static String getTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd-hh:mm:ss");

        return ft.format(date) + "  ";
    }
}
