package cn.liuyin.tool;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import cn.liuyin.web.constant.ConstantData;


public class PackageTool {
    /**
     * @param packgename String 传入的包名
     * @return boolean 返回是否存在该app
     */
    public static boolean hasApp(String packgename) {
        PackageInfo packageInfo;
        try {
            packageInfo = ConstantData.getContext().getPackageManager().getPackageInfo(
                    packgename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            System.out.println("没有安装");
            return false;
        } else {
            System.out.println("已经安装");
            return true;
        }

    }
    /*
     *
     */

    /**
     * 获取程序 图标
     *
     * @param packname String 应用的包名
     * @return Drawable 应用的图标
     */
    public static Drawable getAppIcon(String packname) {
        try {
            Context context = ConstantData.getContext();
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

    /*
     * 获取程序的名字
     */
    public static String getAppName(String packname) {
        try {
            Context context = ConstantData.getContext();
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

    public static Drawable getApkIconByFilePath(String apkFilePath) {
        Context context = ConstantData.getContext();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, 0);
        if (packageInfo == null) {
            return null;
        }

        packageInfo.applicationInfo.sourceDir = apkFilePath;
        packageInfo.applicationInfo.publicSourceDir = apkFilePath;
        Drawable iconDrawable = packageInfo.applicationInfo.loadIcon(packageManager);
        if (iconDrawable == null) {
            return null;
        }

        if (iconDrawable instanceof BitmapDrawable && ((BitmapDrawable) iconDrawable).getBitmap() == ((BitmapDrawable) packageManager.getDefaultActivityIcon()).getBitmap()) {
            return null;
        }

        return iconDrawable;
    }

    /*
     *获取程序的版本号
     */
    public String getAppVersion(String packname) {

        try {
            Context context = ConstantData.getContext();
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /*
     * 获取程序的权限
     */
    public String[] getAppPremission(String packname) {
        try {
            Context context = ConstantData.getContext();
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /*
     * 获取程序的签名
     */
    public String getAppSignature(String packname) {
        try {
            Context context = ConstantData.getContext();
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }


}
