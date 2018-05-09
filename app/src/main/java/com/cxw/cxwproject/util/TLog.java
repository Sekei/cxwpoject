package com.cxw.cxwproject.util;

import com.cxw.cxwproject.AppConfig;

import android.util.Log;

public class TLog {
	public static final String LOG_TAG = "tag";
	public static final boolean Debug = AppConfig.DEBUG_LOG;

	public static final void v(Object... args) {
		println(Log.VERBOSE, args);
	}

	public static final void d(Object... args) {
		println(Log.DEBUG, args);
	}

	public static final void i(Object... args) {
		println(Log.INFO, args);
	}

	public static final void w(Object... args) {
		println(Log.WARN, args);
	}

	public static final void e(Object... args) {
		println(Log.ERROR, args);
	}

	public static String getLineNumber() {
		StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

		String caller = "<unknown>";
		// Walk up the stack looking for the first caller outside of VolleyLog.
		// It will be at least two frames up, so start there.
		for (int i = 3; i < trace.length; i++) {
//			Class<?> clazz = trace[i].getClass();
			return trace[i].getClassName() + "  lineNumber:" + trace[i].getLineNumber() + " "
					+ trace[i].getMethodName();
			// Log.e("main.classname", trace[i].getClassName());
			// Log.e("main.class", trace[i].getClass().toString());
			// if (!clazz.equals(TLog.class)) {//跳过当前的类
			// String callingClass = trace[i].getClassName();
			// callingClass =
			// callingClass.substring(callingClass.lastIndexOf('.') + 1);
			// callingClass =
			// callingClass.substring(callingClass.lastIndexOf('$') + 1);
			//
			// caller = callingClass + "." + trace[i].getMethodName();
			// break;
		}
		return "";
	}

	private static int println(int priority, Object... msgs) {
		if (!Debug) {
			return 0;
		}
		if (msgs.length == 0) {
			return Log.println(priority, LOG_TAG,getLineNumber());
		} else if (msgs.length == 1) {
			return Log.println(priority, LOG_TAG, msgs[0].toString());
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < msgs.length; i++) {
			sb.append(msgs[i]);
			sb.append(" ");
		}
		return Log.println(priority, msgs[0].toString(), sb.toString());
	}

}
