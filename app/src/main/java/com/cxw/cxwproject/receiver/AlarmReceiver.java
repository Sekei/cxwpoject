package com.cxw.cxwproject.receiver;

import com.cxw.cxwproject.MyApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "系统闹钟叫了？？", Toast.LENGTH_LONG).show();
		// 当系统到我们设定的时间点的时候会发送广播，执行这里
		MyApp.getApp().getVibrator();
	}
}
