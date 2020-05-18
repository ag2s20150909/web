package cn.liuyin.tool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import cn.liuyin.web.constant.ConstantData;

/**
 * Created by 刘银（本地） on 2017/4/6 0006.
 */

public class FileTool {
    private FileTool() {
        throw new AssertionError();
    }

    public static void saveBytes(String path, String name, byte[] data) {
        File f = new File(path, name);
        try {
            FileOutputStream fos = new FileOutputStream(f, false);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            ConstantData.getMainActivity().sendBroadcast(intent);
        }
    }


    public static String getAssetsString(Context context, String name) {
        try {
            //得到资源中的asset数据流
            InputStream in = context.getResources().getAssets().open(name);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveADString(String content, String name) {

        saveString(ConstantData.AdPath, content, name);
    }

    public static void saveString(String path, String name, String content, boolean append) {
        FileWriter fw;
        content += "\n";
        try {
            File f = new File(path);
            String file = path + "/" + name;
            fw = new FileWriter(file, append);//false 直接替换
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveString(String path, String content, String name) {
        FileWriter fw;
        content += "\n";
        try {
            File f = new File(path);
            String file = path + "/" + name;
            fw = new FileWriter(file, false);//false 直接替换
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;
            for (String aFile : file) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + aFile);
                } else {
                    temp = new File(oldPath + File.separator + aFile);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            temp.getName());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + aFile, newPath + "/" + aFile);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }


    public static void copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        try {
            String[] fileNames = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
