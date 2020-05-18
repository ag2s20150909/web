package cn.liuyin.tool;


import android.util.Base64;

public class MBase64 {
    public static String MEncode(String str) {
        // 在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
        return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
    }

    public static String MDecode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }
}
