package cn.liuyin.web.function;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;

public class UIClass {


    public static void openByOtherApp(Context context, String url) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("应用申请打开外部应用，是否打开？")
                .setPositiveButton("确定", (dialog, which) -> {
                    Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    OpenInApp.open(url);
                })
                .setNeutralButton("取消", (dialog, which) -> {
                });
        builder.setOnCancelListener(dialog -> {
        });
        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        dialog.show();

    }

    public static void ViewSOURCE(String msg) {
        final Context context = ConstantData.getContext();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("查看源码");
        ScrollView sc = new ScrollView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);//4个参数按顺序分别是左上右下
        TextView tv = new TextView(context);
        tv.setTextIsSelectable(true);
        //tv.setSelected(true);

        tv.setPadding(30, 5, 30, 5);
        //sc.setLayoutParams(layoutParams);
        sc.addView(tv);


        tv.setText(msg);
        builder.setView(sc);
        builder.setPositiveButton("确定", (dialog, which) -> {
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        }
        int h = context.getResources().getDisplayMetrics().heightPixels;
        int w = context.getResources().getDisplayMetrics().widthPixels;
        if (lp != null) {
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = w - 20; // 宽度
            lp.height = h - 150; // 高度
            //lp.alpha = 0.7f; // 透明度
        }

        assert dialogWindow != null;
        dialogWindow.setAttributes(lp);
        dialog.show();


    }

    public static void OpeanOtherApp(final String url) {
        final Context context = ConstantData.getContext();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        assert inflater != null;
        View layout = inflater.inflate(R.layout.opeanapp, nullParent);
        TextView message = layout.findViewById(R.id.opeanappTextViewMessage);
        Button btn_ok = layout.findViewById(R.id.opeanappButtonOk);
        Button btn_no = layout.findViewById(R.id.opeanappButtonNo);
        dialog.setContentView(layout);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        }
        if (lp != null) {
            lp.x = 0; // 新位置X坐标
            lp.y = 100; // 新位置Y坐标
            lp.width = 600; // 宽度
            lp.height = 200; // 高度
            lp.alpha = 0.7f; // 透明度
        }

        assert dialogWindow != null;
        dialogWindow.setAttributes(lp);
        dialog.show();
        message.setGravity(Gravity.CENTER);
        message.setText("应用申请打开外部应用，是否打开？");
        btn_ok.setOnClickListener(p1 -> {
            Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            OpenInApp.open(url);


        });
        btn_no.setOnClickListener(p1 -> {
            //Toast.makeText(context,"NO",1).show();
            dialog.dismiss();
        });
    }

    public static void ViewError(final String message, final boolean isclose) {
        final Context context = ConstantData.getContext();
        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        assert inflater != null;
        View layout = inflater.inflate(R.layout.souce, nullParent);
        TextView tv = layout.findViewById(R.id.souceTextView);
        tv.setText(message);

        Button btn_cancler = layout.findViewById(R.id.souceButtonCancer);
        Button btn_copy = layout.findViewById(R.id.souceButtonCopy);
        dialog.setContentView(layout);

        //dialog.setTitle("查看源码");

        Window dialogWindow = dialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
            dialogWindow.setWindowAnimations(R.style.mystyle);  //添加动画
        }
        int h = context.getResources().getDisplayMetrics().heightPixels;
        int w = context.getResources().getDisplayMetrics().widthPixels;
        if (lp != null) {
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = w - 20; // 宽度
            lp.height = h - 150; // 高度
        }

        //lp.alpha = 0.7f; // 透明度
        assert dialogWindow != null;
        dialogWindow.setAttributes(lp);
        dialog.show();
        btn_cancler.setOnClickListener(p1 -> {
            // TODO: Implement this method
            dialog.dismiss();
            if (isclose)
                ConstantData.getMainActivity().finish();
        });
        btn_copy.setOnClickListener(p1 -> {
            // TODO: Implement this method
            dialog.dismiss();

            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            assert cm != null;
            ClipData clipData = cm.getPrimaryClip();
            clipData.addItem(new ClipData.Item(tv.getText()));
            cm.setPrimaryClip(clipData);
            Toast.makeText(context, "源码已经复制到剪贴板:)", Toast.LENGTH_LONG).show();
            if (isclose)
                ConstantData.getMainActivity().finish();
        });
    }


    public static void ViewWebSouce(final String message) {
        final Context context = ConstantData.getContext();
        final Dialog dialog = new Dialog(context);
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup nullParent = null;
            assert inflater != null;
            View layout = inflater.inflate(R.layout.souce, nullParent);
            TextView tv = layout.findViewById(R.id.souceTextView);
            tv.setText(message);
            ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "rotationX", 360, 180, 360);
            animator.setDuration(1000);
            animator.start();

            Button btn_cancler = layout.findViewById(R.id.souceButtonCancer);
            Button btn_copy = layout.findViewById(R.id.souceButtonCopy);
            dialog.setContentView(layout);

            dialog.setTitle("查看源码");

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
            if (dialogWindow != null) {
                dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
            }
            int h = context.getResources().getDisplayMetrics().heightPixels;
            int w = context.getResources().getDisplayMetrics().widthPixels;
            if (lp != null) {
                lp.x = 0; // 新位置X坐标
                lp.y = 0; // 新位置Y坐标
                lp.width = w - 20; // 宽度
                lp.height = h - 150; // 高度
                //lp.alpha = 0.7f; // 透明度
            }

            assert dialogWindow != null;
            dialogWindow.setAttributes(lp);
            dialog.show();
            btn_cancler.setOnClickListener(p1 -> {
                // TODO: Implement this method
                dialog.dismiss();
            });
            btn_copy.setOnClickListener(p1 -> {
                // TODO: Implement this method
                dialog.dismiss();

                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                ClipData clipData = cm.getPrimaryClip();
                clipData.addItem(new ClipData.Item(message));
                cm.setPrimaryClip(clipData);
                //cm.setText(message);
                Toast.makeText(context, "源码已经复制到剪贴板:)", Toast.LENGTH_LONG).show();
            });
        } catch (Exception e) {
            UIClass.ViewError(e.toString(), false);
        }
    }

}
