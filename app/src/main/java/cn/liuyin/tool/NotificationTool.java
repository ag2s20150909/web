package cn.liuyin.tool;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;

/**
 * Created by 刘银（本地） on 2017/4/6 0006.
 */

public class NotificationTool {

    public static void sendNotification() {
        //获取NotificationManager实例
        Context context = ConstantData.getContext();
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        Notification.Builder builder = new Notification.Builder(context
        )
                //设置小图标
                // .setLargeIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("最简单的Notification")
                //设置通知内容
                .setContentText("只有小图标、标题、内容")
                //点击通知后自动清除
                .setAutoCancel(true);
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }

    public static void sendNotification(String title, String txt) {
        //获取NotificationManager实例
        Context context = ConstantData.getContext();
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        Notification.Builder builder = new Notification.Builder(context
        )
                //设置小图标
                // .setLargeIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容
                .setContentText(txt)
                //点击通知后自动清除
                .setAutoCancel(true);
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }
}
