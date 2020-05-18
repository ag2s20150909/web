package cn.liuyin.tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

public class StateBarTool {
    public static int getStateBarColor(View v) {
        int a = Color.argb(50, 220, 220, 220);
        if (v != null) {
            Bitmap b = getBitmapFromView(v);
            if (b != null) {
                int pixel, ca, cr, cg, cb;
                pixel = b.getPixel(b.getWidth() / 2, 2);

                ca = Color.alpha(pixel);
                cr = Color.red(pixel);
                cg = Color.green(pixel);
                cb = Color.blue(pixel);
                int max = 220;
                if (cr >= max && cg >= max && cb >= max) {
                    cr = cg = cb = max;

                }
                return Color.argb(ca, cr, cg, cb);
            }
            return a;
        }
        return a;
    }

    private static Bitmap getBitmapFromView(View v) {
        Bitmap b = null;
        if (v != null) {
            int w, h;
            w = v.getWidth();
            h = 4;
            if (w <= 0) {
                w = 4;
            }
            b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            Drawable d = v.getBackground();
            if (d != null) {
                d.draw(c);
            } else {
                c.drawColor(Color.argb(50, 220, 220, 220));
            }
            v.draw(c);

        }
        return b;
    }
}
