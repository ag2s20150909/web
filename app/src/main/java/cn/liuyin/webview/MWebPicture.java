package cn.liuyin.webview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import cn.liuyin.web.constant.ConstantData;


public class MWebPicture {


    public static void saveLongScreenshots(MWebView mWebView) {

        // 这里的 mWebView 就是 X5 内核的 WebView ，代码中的 longImage 就是最后生成的长图
        mWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mWebView.layout(0, 0, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
        mWebView.setDrawingCacheEnabled(true);
        mWebView.buildDrawingCache();
        Bitmap longImage = Bitmap.createBitmap(mWebView.getMeasuredWidth(),
                mWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas longCanvas = new Canvas(longImage);
        Canvas canvas = new Canvas(longImage);    // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        canvas.drawBitmap(longImage, 0, mWebView.getMeasuredHeight(), paint);
        float scale = ConstantData.getMainActivity().getResources().getDisplayMetrics().density;
        Bitmap x5Bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas x5Canvas = new Canvas(x5Bitmap);
        //x5Canvas.drawColor(ContextCompat.getColor(mWebView.getContext(),Color.BLACK));
        mWebView.getX5WebViewExtension().snapshotWholePage(x5Canvas, false, false);  // 少了这行代码就无法正常生成长图

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        longCanvas.drawBitmap(x5Bitmap, matrix, paint);
        saveBitMap(longImage, true);

    }

    public static void saveScreenshots(WebView webview) {
        View view = ConstantData.getMainActivity().getWindow().getDecorView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        saveBitMap(bitmap, false);
    }


    private static void saveBitMap(Bitmap bitmap, Boolean isFull) {
        String basepath = Environment.getExternalStorageDirectory().getPath() + "/Download" + "/Picture/";
        Calendar cal = Calendar.getInstance();
        File f = new File(basepath);
        f.mkdirs();
        String filename;
        if (isFull) {
            filename = "LS-";
        } else {
            filename = "S-";
        }
        filename += cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY)
                + "-" + cal.get(Calendar.MINUTE) + "-" + cal.get(Calendar.SECOND) + ".jpg";
        try {
            String path = basepath + filename;
            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
            Toast.makeText(ConstantData.getContext(), "图片保存在：" + path, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    //X5内核不支持
    @SuppressLint("ObsoleteSdkInt")
    public static void savaAsPDF(WebView webview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) ConstantData.getContext().getSystemService(Context.PRINT_SERVICE);
            String jobName = "X5Web" + "Document";
            //printManager.print(jobName,printAdapter,new PrintAttributes.Builder().build());

            PrintDocumentAdapter printAdapter;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = (PrintDocumentAdapter) webview.createPrintDocumentAdapter(jobName);
            } else {
                printAdapter = (PrintDocumentAdapter) webview.createPrintDocumentAdapter("gg");
            }

            PrintJob printJob = printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());

            // Save the job object for later status checking
            //mPrintJobs.add(printJob);
        } else {
            Toast.makeText(ConstantData.getContext(), "当前系统版本过低，不支持该功能。", Toast.LENGTH_LONG).show();

        }
    }
}
