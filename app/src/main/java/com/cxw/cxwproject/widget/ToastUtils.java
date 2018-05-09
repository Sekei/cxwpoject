package com.cxw.cxwproject.widget;

import com.cxw.cxwproject.MyApp;

import android.widget.Toast;

public class ToastUtils {
    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;

    public static void show(String message) {
        if (message == null || message.equals("") || message.length() == 0) {
            message = "未知错误";
        }
        if (toast == null) {
            toast = Toast.makeText(MyApp.getApp(), message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
