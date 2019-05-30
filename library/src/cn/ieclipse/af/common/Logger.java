/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.common;

import android.util.Log;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Logger for log runtime information.
 * 
 * @author melord
 * @version 1.0
 * 
 */
public class Logger extends MarkerIgnoringBase {
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARN = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    
    private static int level;

    @Override
    public boolean isTraceEnabled() {
        return Log.isLoggable(getName(), Log.VERBOSE);
    }

    @Override
    public void trace(String msg) {
        Log.v(getName(), msg);
    }

    @Override
    public void trace(String format, Object arg) {
        trace(format, new Object[]{arg});
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        trace(format, new Object[]{arg1, arg2});
    }

    @Override
    public void trace(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        if (isTraceEnabled()) {
            Log.v(getName(), ft.getMessage());
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        Log.v(getName(), msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return Log.isLoggable(getName(), Log.DEBUG);
    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(String format, Object arg) {

    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void debug(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void info(String format, Object arg) {

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void warn(String format, Object arg) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(String format, Object arg) {

    }

    @Override
    public void error(String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(String format, Object... arguments) {

    }

    @Override
    public void error(String msg, Throwable t) {

    }

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

    public Logger(String tag) {
        this.name = tag.substring(tag.lastIndexOf('.') + 1);
        this.tag = name;
    }

    /**
     * Use class simple name as log tag. See {@link #getLogger(String)}
     *
     * @param clazz
     *            class.
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return (Logger) LoggerFactory.getLogger(clazz.getSimpleName());
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
        return (Logger) LoggerFactory.getLogger(tag);
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
