package cn.liuyin.web.function;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by asus on 2017/9/1.
 */

public class TBSTool {
    private Activity mActivity;

    public TBSTool(Activity activity) {
        this.mActivity = activity;
        removeBottomAd();
        //removeShare();
    }

    public static void clearView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
    }

    public void removeBottomAd() {
        mActivity.getWindow().getDecorView().addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ArrayList<View> outView = new ArrayList<>();
            mActivity.getWindow().getDecorView().findViewsWithText(outView, "安装QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
            if (outView.size() > 0) {
                for (int i = 0; i < outView.size(); i++) {

                    ViewGroup parent = (ViewGroup) outView.get(i).getParent();
                    parent.setVisibility(View.GONE);
                    clearView(parent);
                    System.out.println("ViewName ViewGroup is " + parent.getClass().getName());
                    System.out.println("ViewName View is " + outView.get(i).getClass().getName());
                    parent.removeView(outView.get(i));
                }
            }
        });
    }


}
