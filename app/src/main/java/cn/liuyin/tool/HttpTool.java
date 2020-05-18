package cn.liuyin.tool;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpTool {

    public static InputStream getInputStream(String url) {
        try {
            URL murl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) murl.openConnection();
            return new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String url) {
        try {
            byte[] data = getBytes(url);
            return new String(data);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static byte[] getBytes(String url) {
        try {
            InputStream in = getInputStream(url);
            return mRead(in);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] mRead(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


}
