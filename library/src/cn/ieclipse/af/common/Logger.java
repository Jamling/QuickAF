/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Logger for log runtime information.
 * 
 * @author melord
 * @version 1.0
 * 
 */
public class Logger {
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARN = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    
    private static int level;
    
    /**
     * @return the level
     */
    public static int getLevel() {
        return level;
    }
    
    /**
     * @param level
     *            the level to set
     */
    public static void setLevel(int level) {
        Logger.level = level;
    }
    
    private static Map<String, Logger> intances;
    
    private String tag;
    
    private Logger(String tag) {
        this.tag = tag;
    }
    
    /**
     * Use class simple name as log tag. See {@link #getLogger(String)}
     * 
     * @param clazz
     *            class.
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }
    
    /**
     * Get Logger by tag, if tag exists, return exists Logger, otherwise new
     * Logger.
     * 
     * @param tag
     *            Logger tag name
     * @return Logger instance
     */
    public static Logger getLogger(String tag) {
        if (intances == null) {
            intances = new HashMap<String, Logger>();
        }
        Logger logger = intances.get(tag);
        if (logger == null) {
            logger = new Logger(tag);
        }
        return logger;
    }
    
    public void v(String msg, Throwable throwable) {
        if (LEVEL_VERBOSE >= level) {
            if (throwable == null) {
                android.util.Log.v(tag, msg);
            }
            else {
                android.util.Log.v(tag, msg, throwable);
            }
        }
    }
    
    public void v(String msg) {
        v(msg, null);
    }
    
    public void d(String msg, Throwable throwable) {
        if (LEVEL_DEBUG >= level) {
            if (throwable == null) {
                android.util.Log.d(tag, msg);
            }
            else {
                android.util.Log.d(tag, msg, throwable);
            }
        }
    }
    
    public void d(String msg) {
        d(msg, null);
    }
    
    public void i(String msg, Throwable throwable) {
        if (LEVEL_INFO >= level) {
            if (throwable == null) {
                android.util.Log.i(tag, msg);
            }
            else {
                android.util.Log.i(tag, msg, throwable);
            }
        }
    }
    
    public void i(String msg) {
        i(msg, null);
    }
    
    public void w(String msg, Throwable throwable) {
        if (LEVEL_WARN >= level) {
            if (throwable == null) {
                android.util.Log.w(tag, msg);
            }
            else {
                android.util.Log.w(tag, msg, throwable);
            }
        }
    }
    
    public void w(String msg) {
        w(msg, null);
    }
    
    public void e(String msg, Throwable throwable) {
        if (LEVEL_ERROR >= level) {
            if (throwable == null) {
                android.util.Log.e(tag, msg);
            }
            else {
                android.util.Log.e(tag, msg, throwable);
            }
        }
    }
    
    public void e(String msg) {
        e(msg, null);
    }
}
