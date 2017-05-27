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
 */

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "HH:mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";

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
     * @param dateFormat a date format (See examples)
     *
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
     * @param millis ms
     * @param format sample %02d:%02d:%02d
     *
     * @return the formated string
     */
    public static String formatDuration(long millis, String format) {
        String ret = "";
        int size = format.split("%").length - 1;

        int seconds = (int) Math.ceil((double) millis / 1000);

        int days = 0, hours = 0, minutes = 0;
        if (seconds > 86400) {
            days = seconds / 86400;
            seconds = seconds % 86400;
        }
        if (seconds > 3600) {
            hours = seconds / 3600;
            seconds = seconds % 3600;
        }
        if (seconds > 60) {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }
        if (size == 1) {
            ret = String.format(format, seconds);
        }
        else if (size == 2) {
            ret = String.format(format, minutes, seconds);
        }
        else if (size == 3) {
            ret = String.format(format, hours, minutes, seconds);
        }
        else if (size == 4) {
            ret = String.format(format, days, hours, minutes, seconds);
        }
        return ret;
    }

    /**
     * Get GMT time to UTC time
     *
     * @param millis ms unit
     *
     * @return UTC time
     * @since 2.1.1
     */
    public static Date getUTCTime(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        // 取得时间偏移量：
        int zoneOffset = c.get(java.util.Calendar.ZONE_OFFSET);
        // 取得夏令时差：
        int dstOffset = c.get(java.util.Calendar.DST_OFFSET);
        c.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return c.getTime();
    }

    // ---------------------->

    /**
     * Get a friendly string format time over, such as a year ago etc.
     *
     * @param serverTime the server time
     * @param current    client current time
     *
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
        t = time02.get(Calendar.DAY_OF_MONTH) - time01.get(Calendar.DAY_OF_MONTH);
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

    /**
     * 根据不同时间段，显示不同时间
     *
     * @param date
     *
     * @return
     */
    public static String getTodayTimeBucket(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat timeformatter0to11 = new SimpleDateFormat("KK:mm", Locale.getDefault());
        SimpleDateFormat timeformatter1to12 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 5) {
            return "凌晨 " + timeformatter0to11.format(date);
        }
        else if (hour >= 5 && hour < 12) {
            return "上午 " + timeformatter0to11.format(date);
        }
        else if (hour >= 12 && hour < 18) {
            return "下午 " + timeformatter1to12.format(date);
        }
        else if (hour >= 18 && hour < 24) {
            return "晚上 " + timeformatter1to12.format(date);
        }
        return "";
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     *
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param milliseconds data
     *
     * @return 日期变量转成对应的星期字符串
     */
    public static String getWeekOfMillis(long milliseconds) {
        int WEEKDAYS = 7;
        String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    public static boolean isSameDay(long time1, long time2) {
        return isSameDay(new Date(time1), new Date(time2));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2
            .get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    /**
     * UTM转换成日期描述，如三周前，上午，昨天等
     *
     * @param milliseconds milliseconds
     * @param isShowWeek   是否采用周的形式显示  true 显示为3周前，false 则显示为时间格式mm-dd
     *
     * @return 如三周前，上午，昨天等
     */

    public static String getTimeDesc(long milliseconds, boolean isShowWeek) {
        StringBuffer sb = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        long hour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTimeInMillis(System.currentTimeMillis());
        long hourNow = calendar.get(Calendar.HOUR_OF_DAY);

        Log.v("---------->---", System.currentTimeMillis() + "----------" + milliseconds);
        long datetime = System.currentTimeMillis() - (milliseconds);
        long day = (long) Math.floor(datetime / 24 / 60 / 60 / 1000.0f) + (hourNow < hour ? 1 : 0);// 天前

        if (day <= 7) {// 一周内
            if (day == 0) {// 今天
                if (hour <= 4) {
                    sb.append(" 凌晨 ");
                }
                else if (hour > 4 && hour <= 6) {
                    sb.append(" 早上 ");
                }
                else if (hour > 6 && hour <= 11) {
                    sb.append(" 上午 ");
                }
                else if (hour > 11 && hour <= 13) {
                    sb.append(" 中午 ");
                }
                else if (hour > 13 && hour <= 18) {
                    sb.append(" 下午 ");
                }
                else if (hour > 18 && hour <= 19) {
                    sb.append(" 傍晚 ");
                }
                else if (hour > 19 && hour <= 24) {
                    sb.append(" 晚上 ");
                }
                else {
                    sb.append("今天 ");
                }
            }
            else if (day == 1) {// 昨天
                sb.append(" 昨天 ");
            }
            else if (day == 2) {// 前天
                sb.append(" 前天 ");
            }
            else {
                sb.append(" " + getWeekOfMillis(milliseconds) + " ");
            }
        }
        else {// 一周之前
            if (isShowWeek) {
                sb.append((day % 7 == 0 ? (day / 7) : (day / 7 + 1)) + "周前");
            }
            else {
                SimpleDateFormat formatBuilder = new SimpleDateFormat("MM-dd");
                String time = formatBuilder.format(milliseconds);
                sb.append(time);
            }
        }
        Log.v("sb---", sb.toString() + "");
        return sb.toString();
    }
}