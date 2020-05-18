package cn.liuyin.ndk;

import android.graphics.Bitmap;

/**
 * Created by asus on 2017/11/19.
 */

public class MyNdkTool {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native boolean toHeiBai(Bitmap bitmap);
}
