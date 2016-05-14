/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.legcy;

/**
 * Log tool for commons. Don't use this Log in other place.
 * 
 * @author melord
 * @version 1.0
 * @deprecated
 */
final class Log {
    
    private Log() {
        
    }
    
    // public static void log(int level, String tag, String message,
    // Throwable throwable) {
    //
    // }
    
    public static void v(String tag, String msg, Throwable throwable) {
        if (throwable == null) {
            android.util.Log.v(tag, msg);
        }
        else {
            android.util.Log.v(tag, msg, throwable);
        }
    }
    
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }
    
    public static void d(String tag, String msg, Throwable throwable) {
        if (throwable == null) {
            android.util.Log.d(tag, msg);
        }
        else {
            android.util.Log.d(tag, msg, throwable);
        }
    }
    
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }
    
    public static void i(String tag, String msg, Throwable throwable) {
        if (throwable == null) {
            android.util.Log.i(tag, msg);
        }
        else {
            android.util.Log.i(tag, msg, throwable);
        }
    }
    
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }
    
    public static void w(String tag, String msg, Throwable throwable) {
        if (throwable == null) {
            android.util.Log.w(tag, msg);
        }
        else {
            android.util.Log.w(tag, msg, throwable);
        }
    }
    
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }
    
    public static void e(String tag, String msg, Throwable throwable) {
        if (throwable == null) {
            android.util.Log.e(tag, msg);
        }
        else {
            android.util.Log.e(tag, msg, throwable);
        }
    }
    
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }
}
