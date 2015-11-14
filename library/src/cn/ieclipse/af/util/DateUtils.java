/*
 * Copyright (C) 2011 asksven 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package cn.ieclipse.af.util;

/**
 * @author sven 
 * 
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "HH:mm:ss";
    
    /**
     * Returns the current date in the default format.
     * 
     * @return the current formatted date/time
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
    /**
     * Returns the current date in a given format.
     * DateUtils.now("dd MMMMM yyyy") DateUtils.now("yyyyMMdd")
     * DateUtils.now("dd.MM.yy") DateUtils.now("MM/dd/yy")
     * 
     * @param dateFormat
     *            a date format (See examples)
     * @return the current formatted date/time
     */
    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }
    
    public static String format(String dateFormat, Date time) {
        
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }
    
    public static String format(String dateFormat, Long time) {
        
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }
    
    public static String format(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(time);
    }
    
    public static String format(long timeMs) {
        return format(timeMs, DATE_FORMAT_NOW);
    }
    
    public static String formatShort(long timeMs) {
        return format(timeMs, DATE_FORMAT_SHORT);
    }
    
    public static String format(long timeMs, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timeMs);
    }
    
    /**
     * Formats milliseconds to a friendly form
     * 
     * @param millis
     * @return the formated string
     */
    public static String formatDuration(long millis) {
        String ret = "";
        
        int seconds = (int) Math.floor(millis / 1000);
        
        int days = 0, hours = 0, minutes = 0;
        if (seconds > (60 * 60 * 24)) {
            days = seconds / (60 * 60 * 24);
            seconds -= days * (60 * 60 * 24);
        }
        if (seconds > (60 * 60)) {
            hours = seconds / (60 * 60);
            seconds -= hours * (60 * 60);
        }
        if (seconds > 60) {
            minutes = seconds / 60;
            seconds -= minutes * 60;
        }
        ret = "";
        if (days > 0) {
            ret += days + " d ";
        }
        
        if (hours > 0) {
            ret += hours + " h ";
        }
        
        if (minutes > 0) {
            ret += minutes + " m ";
        }
        if (seconds > 0) {
            ret += seconds + " s ";
        }
        
        if (ret.equals("")) {
            ret = "0 s";
        }
        return ret;
    }
    
    /**
     * Formats milliseconds to a friendly form. Short means that seconds are
     * truncated if value > 1 Day
     * 
     * @param millis
     * @return the formated string
     */
    public static String formatDurationShort(long millis) {
        String ret = "";
        
        int seconds = (int) Math.floor(millis / 1000);
        
        int days = 0, hours = 0, minutes = 0;
        if (seconds > (60 * 60 * 24)) {
            days = seconds / (60 * 60 * 24);
            seconds -= days * (60 * 60 * 24);
        }
        if (seconds > (60 * 60)) {
            hours = seconds / (60 * 60);
            seconds -= hours * (60 * 60);
        }
        if (seconds > 60) {
            minutes = seconds / 60;
            seconds -= minutes * 60;
        }
        ret = "";
        if (days > 0) {
            ret += days + " d ";
        }
        
        if (hours > 0) {
            ret += hours + " h ";
        }
        
        if (minutes > 0) {
            ret += minutes + " m ";
        }
        if ((seconds > 0) && (days == 0)) {
            // only show seconds when value < 1 day
            ret += seconds + " s ";
        }
        
        if (ret.equals("")) {
            ret = "0 s";
        }
        return ret;
    }
    
    /**
     * Returns String i.e. hh:mm:ss representing the duration specified
     * (positive values only)
     * 
     * @param period
     *            - time in seconds
     * @return String on format mm:ss or hh:mm:ss if more than one hour.
     */
    public static String formatSecond(int period) {
        
        int totalSeconds = (period > 0) ? period : 0;
        
        totalSeconds = (totalSeconds < 360000) ? totalSeconds : 0;
        
        int minutes = (totalSeconds / 60) % 60;
        int seconds = totalSeconds % 60;
        int hours = (totalSeconds / 3600);
        
        char[] time = new char[hours > 0 ? 8 : 5];
        
        int buf = 0;
        int i = 0;
        
        if (hours > 0) {
            buf = hours % 10;
            time[i++] = (char) ((hours / 10) + 48);
            time[i++] = (char) (buf + 48);
            time[i++] = ':';
        }
        
        buf = minutes % 10;
        time[i++] = (char) ((minutes / 10) + 48);
        time[i++] = (char) (buf + 48);
        
        time[i++] = ':';
        buf = seconds % 10;
        time[i++] = (char) ((seconds / 10) + 48);
        time[i++] = (char) (buf + 48);
        
        return new String(time);
    }
    
    /**
     * Get a friendly string format time over, such as a year ago etc.
     * 
     * @param serverTime
     *            the server time
     * @param current
     *            client current time
     * @return friendly string
     */
    public static String getTimeOver(long serverTime, long current) {
        if (serverTime >= current) {
            throw new IllegalArgumentException();
        }
        
        Calendar time01 = Calendar.getInstance();
        time01.setTimeInMillis(serverTime);
        
        Calendar time02 = Calendar.getInstance();
        time02.setTimeInMillis(current);
        int t = time02.get(Calendar.YEAR) - time01.get(Calendar.YEAR);
        if (t > 0) {
            return t + " years ago";
        }
        t = time02.get(Calendar.MONTH) - time01.get(Calendar.MONTH);
        if (t > 0) {
            return t + " months ago";
        }
        t = time02.get(Calendar.DAY_OF_MONTH)
                - time01.get(Calendar.DAY_OF_MONTH);
        if (t > 1) {
            return t + " days ago";
        }
        if (t > 0) {
            return t + " yesterday";
        }
        t = time02.get(Calendar.HOUR_OF_DAY) - time01.get(Calendar.HOUR_OF_DAY);
        if (t > 0) {
            return t + " hours ago";
        }
        t = time02.get(Calendar.MINUTE) - time01.get(Calendar.MINUTE);
        if (t > 0) {
            return t + " minutes ago";
        }
        t = time02.get(Calendar.SECOND) - time01.get(Calendar.SECOND);
        if (t > 0) {
            return t + " seconds ago";
        }
        return "?????";
    }
}