/*
 * Copyright (C) 2015-2016 QuickAF
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
package cn.ieclipse.af.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TimeRange {
    private Calendar start = Calendar.getInstance();
    private Calendar current = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();

    private int yearMin;
    private int yearMax;
    private int yearIdx;

    private int monthMin;
    private int monthMax;
    private int monthIdx;

    private int dayMin;
    private int dayMax;
    private int dayIdx;

    private int hourMin;
    private int hourMax;
    private int hourIdx;

    private int minuteMin;
    private int minuteMax;
    private int minuteIdx;

    public void setStartTime(int field, int value) {
        start.set(field, value);
        init();
    }

    public void setStartTimeRelative(int field, int value) {
        start.add(field, value);
        init();
    }

    public void setStartTime(long time) {
        start.setTime(new Date(time));
    }

    public void setCurrentTime(int field, int value) {
        current.set(field, value);
        init();
    }

    public void setCurrentTime(long time) {
        current.setTime(new Date(time));
        init();
    }

    public void setEndTime(int field, int value) {
        end.set(field, value);
        init();
    }

    public void setEndTimeRelative(int field, int value) {
        end.add(field, value);
        init();
    }

    public void setEndTime(long time) {
        end.setTime(new Date(time));
        init();
    }

    public int[] getYearRange() {
        return new int[]{yearMin, yearMax, yearIdx};
    }

    public int[] getMonthRange() {
        return new int[]{monthMin, monthMax, monthIdx};
    }

    public int[] getDayRange() {
        return new int[]{dayMin, dayMax, dayIdx};
    }

    public int[] getHourRange() {
        return new int[]{hourMin, hourMax, hourIdx};
    }

    public int[] getMinuteRange() {return new int[]{minuteMin, minuteMax, minuteIdx};}

    private void printRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(sdf.format(start.getTime()));
        System.out.println(sdf.format(current.getTime()));
        System.out.println(sdf.format(end.getTime()));

        System.out.println(Arrays.toString(getYearRange()));
        System.out.println(Arrays.toString(getMonthRange()));
        System.out.println(Arrays.toString(getDayRange()));
        System.out.println(Arrays.toString(getHourRange()));
    }

    private void init() {
        System.out.println("init");
        int y1 = start.get(Calendar.YEAR);
        int y2 = end.get(Calendar.YEAR);
        int y = current.get(Calendar.YEAR);

        int m1 = y1 < y ? 0 : start.get(Calendar.MONTH);
        int m2 = y < y2 ? 11 : end.get(Calendar.MONTH);

        int m = current.get(Calendar.MONTH);
        if (m < m1 || m > m2) {
            m = m1;
            current.set(Calendar.MONTH, m);
        }

        int d1 = 1;
        int d2 = current.getActualMaximum(Calendar.DAY_OF_MONTH);
        int d = current.get(Calendar.DAY_OF_MONTH);
        if (y1 >= y && m1 >= m) {
            d1 = start.get(Calendar.DAY_OF_MONTH);
        }

        if (y2 == y && m2 == m) {
            d2 = end.get(Calendar.DAY_OF_MONTH);
        }
        if (d < d1 || d > d2) {
            d = d1;
            current.set(Calendar.DAY_OF_MONTH, d);
        }

        int h1 = 0;
        int h2 = 23;

        int h = current.get(Calendar.HOUR_OF_DAY);
        if (y1 == y && m1 == m && d1 == d) {
            h1 = start.get(Calendar.HOUR_OF_DAY);
        }
        if (y2 == y && m2 == m && d2 == d) {
            h2 = end.get(Calendar.HOUR_OF_DAY);
        }
        if (h < h1 || h > h2) {
            h = h1;
            current.set(Calendar.HOUR_OF_DAY, h);
        }

        int M = current.get(Calendar.MINUTE);
        int M1 = 0;
        int M2 = 59;
        if (y1 == y && m1 == m && d1 == d && h1 == h) {
            M1 = start.get(Calendar.MINUTE);
        }
        if (y2 == y && m2 == m && d2 == d && h2 == h) {
            M2 = end.get(Calendar.MINUTE);
        }
        if (M < M1 || M > M2) {
            M = M1;
            current.set(Calendar.MINUTE, M);
        }

        yearMax = y2;
        yearMin = y1;
        yearIdx = y - y1;

        monthMax = m2;
        monthMin = m1;
        monthIdx = m - m1;

        dayMax = d2;
        dayMin = d1;
        dayIdx = d - d1;

        hourMax = h2;
        hourMin = h1;
        hourIdx = h - h1;

        minuteMax = M2;
        minuteMin = M1;
        minuteIdx = M - M1;
    }

    public Calendar getCurrentTime() {
        return current;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        System.out.println(c.getMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(c.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(c.get(Calendar.DAY_OF_MONTH));

        TimeRange tr = new TimeRange();
        tr.setStartTimeRelative(Calendar.DAY_OF_MONTH, -5);
        tr.printRange();
        tr.setCurrentTime(Calendar.DAY_OF_MONTH, 6);
        tr.printRange();
    }
}
