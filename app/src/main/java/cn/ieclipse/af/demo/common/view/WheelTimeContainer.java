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
package cn.ieclipse.af.demo.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

import cn.ieclipse.af.common.TimeRange;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.wheelview.OnWheelChangedListener;
import cn.ieclipse.af.view.wheelview.WheelView;
import cn.ieclipse.af.view.wheelview.adapter.AbstractWheelAdapter;
import cn.ieclipse.af.view.wheelview.adapter.NumericWheelAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public class WheelTimeContainer extends LinearLayout implements OnWheelChangedListener {
    public WheelTimeContainer(Context context) {
        super(context);
    }

    public WheelTimeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelTimeContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WheelTimeContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static final int SHOW_YEAR = 1;
    public static final int SHOW_MONTH = 2;
    public static final int SHOW_DAY = 4;
    public static final int SHOW_HOUR = 8;
    public static final int SHOW_MINUTE = 16;
    private int show = 0;

    private WheelView mYear;
    private NumericWheelAdapter mYearAdapter;
    private WheelView mMonth;
    private NumericWheelAdapter mMonthAdapter;
    private WheelView mDay;
    private NumericWheelAdapter mDayAdapter;
    private WheelView mHour;
    private NumericWheelAdapter mHourAdapter;
    private WheelView mMinute;
    private NumericWheelAdapter mMinuteAdapter;

    private TimeRange tr;
    private int textColor = 0xFF585858;
    private int textSize = 16;
    private String[] labels = {"年", "月", "日", "时", "分"};

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mYear = (WheelView) findViewById(R.id.year);
        mMonth = (WheelView) findViewById(R.id.month);
        mDay = (WheelView) findViewById(R.id.day);
        mHour = (WheelView) findViewById(R.id.hour);
        mMinute = (WheelView) findViewById(R.id.minute);

        tr = new TimeRange();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mYear) {
            int y = tr.getYearRange()[0] + mYear.getCurrentItem();
            tr.setCurrentTime(Calendar.YEAR, y);

            initMonth();
            initDay();
            initHour();
        }
        else if (wheel == mMonth) {
            int m = tr.getMonthRange()[0] + mMonth.getCurrentItem();
            tr.setCurrentTime(Calendar.MONTH, m);
            initDay();
            initHour();
        }
        else if (wheel == mDay) {
            int m = tr.getDayRange()[0] + mDay.getCurrentItem();
            tr.setCurrentTime(Calendar.DAY_OF_MONTH, m);
            initHour();
        }
        else if (wheel == mHour) {
            int h = tr.getHourRange()[0] + mHour.getCurrentItem();
            tr.setCurrentTime(Calendar.HOUR_OF_DAY, h);
            initMinute();
        }
        else if (wheel == mMinute) {
            int m = 0 + mMinute.getCurrentItem();
            tr.setCurrentTime(Calendar.MINUTE, m);
        }
    }

    public void setStartTime(int field, int value) {
        tr.setStartTime(field, value);
    }

    public void setStartTimeRelative(int field, int value) {
        tr.setStartTimeRelative(field, value);
    }

    public void setCurrentTime(int field, int value) {
        tr.setCurrentTime(field, value);
    }

    public void setCurrentTime(long time) {
        tr.setCurrentTime(time);
    }

    public void setEndTime(int field, int value) {
        tr.setEndTime(field, value);
    }

    public void setEndTimeRelative(int field, int value) {
        tr.setEndTimeRelative(field, value);
    }

    private void initYear() {
        int[] yr = tr.getYearRange();
        mYearAdapter = new NumericWheelAdapter(getContext(), yr[0], yr[1], "%04d");
        mYearAdapter.setLabel(labels[0]);
        mYearAdapter.setTextColor(textColor);
        mYearAdapter.setTextSize(textSize);
        mYear.setViewAdapter(mYearAdapter);
        mYear.setCurrentItem(tr.getCurrentTime().get(Calendar.YEAR) - yr[0]);
    }

    private void initMonth() {
        int[] mr = tr.getMonthRange();
        mMonthAdapter = new NumericWheelAdapter(getContext(), mr[0] + 1, mr[1] + 1, "%02d");
        mMonthAdapter.setLabel(labels[1]);
        mMonthAdapter.setTextColor(textColor);
        mMonthAdapter.setTextSize(textSize);
        mMonth.setViewAdapter(mMonthAdapter);
        mMonth.setCurrentItem(mr[2]);
    }

    private void initDay() {
        int[] mr = tr.getDayRange();
        mDayAdapter = new NumericWheelAdapter(getContext(), mr[0], mr[1], "%02d");
        mDayAdapter.setLabel(labels[2]);
        mDayAdapter.setTextColor(textColor);
        mDayAdapter.setTextSize(textSize);
        mDay.setViewAdapter(mDayAdapter);
        mDay.setCurrentItem(mr[2]);
    }

    private void initHour() {
        boolean showMinute = (show & SHOW_MINUTE) != 0;
        int[] mr = tr.getHourRange();
        mHourAdapter = new NumericWheelAdapter(getContext(), mr[0], mr[1], showMinute ? "%02d" : "%02d:00");
        mHourAdapter.setLabel(labels[3]);
        mHourAdapter.setTextColor(textColor);
        mHourAdapter.setTextSize(textSize);
        mHour.setViewAdapter(mHourAdapter);
        mHour.setCurrentItem(mr[2]);
    }

    private void initMinute() {
        int[] mr = {0, 59, tr.getCurrentTime().get(Calendar.MINUTE)};
        mMinuteAdapter = new NumericWheelAdapter(getContext(), mr[0], mr[1], "%02d");
        if (labels.length > 4) {
            mMinuteAdapter.setLabel(labels[4]);
        }
        mMinuteAdapter.setTextColor(textColor);
        mMinuteAdapter.setTextSize(textSize);
        mMinute.setViewAdapter(mMinuteAdapter);
        mMinute.setCurrentItem(mr[2]);
    }

    public void show(int show) {
        this.show = show;
        mYear.setVisibility((show & SHOW_YEAR) != 0 ? View.VISIBLE : View.GONE);
        mMonth.setVisibility((show & SHOW_MONTH) != 0 ? View.VISIBLE : View.GONE);
        mDay.setVisibility((show & SHOW_DAY) != 0 ? View.VISIBLE : View.GONE);
        mHour.setVisibility((show & SHOW_HOUR) != 0 ? View.VISIBLE : View.GONE);
        mMinute.setVisibility((show & SHOW_MINUTE) != 0 ? View.VISIBLE : View.GONE);

        initYear();
        initMonth();
        initDay();
        initHour();
        initMinute();

        mYear.addChangingListener(this);
        mMonth.addChangingListener(this);
        mDay.addChangingListener(this);
        mHour.addChangingListener(this);
    }

    public Calendar getCurrentTime() {
        return tr.getCurrentTime();
    }

    public Date getSelectedDate() {
        return tr.getCurrentTime().getTime();
    }

    public int getShow() {
        return show;
    }

    public void setYearLabel(String label) {
        if (mYearAdapter != null && label != null) {
            mYearAdapter.setLabel(label);
            labels[0] = label;
        }
    }

    public void setMonthLabel(String label) {
        if (mMonthAdapter != null && label != null) {
            mMonthAdapter.setLabel(label);
            labels[1] = label;
        }
    }

    public void setDayLabel(String label) {
        if (mDayAdapter != null && label != null) {
            mDayAdapter.setLabel(label);
            labels[2] = label;
        }
    }

    public void setHourLabel(String label) {
        if (mHourAdapter != null && label != null) {
            mHourAdapter.setLabel(label);
            labels[3] = label;
        }
    }

    public void setMinuteLabel(String label) {
        if (mMinuteAdapter != null && label != null) {
            mMinuteAdapter.setLabel(label);
            labels[4] = label;
        }
    }

    public void setLabels(String[] labels) {
        if (labels != null) {
            if (labels.length > 0) {
                if (labels[0] != null) {
                    setYearLabel(labels[0]);
                }
            }
            if (labels.length > 1) {
                if (labels[1] != null) {
                    setMonthLabel(labels[1]);
                }
            }
            if (labels.length > 2) {
                if (labels[2] != null) {
                    setDayLabel(labels[2]);
                }
            }
            if (labels.length > 3) {
                if (labels[3] != null) {
                    setHourLabel(labels[3]);
                }
            }
            if (labels.length > 4) {
                if (labels[4] != null) {
                    setMinuteLabel(labels[4]);
                }
            }
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public AbstractWheelAdapter getAdapter(int show) {
        AbstractWheelAdapter adapter = null;
        switch (show) {
            case SHOW_MINUTE:
                adapter = mMinuteAdapter;
                break;
            case SHOW_HOUR:
                adapter = mHourAdapter;
                break;
            case SHOW_DAY:
                adapter = mDayAdapter;
                break;
            case SHOW_MONTH:
                adapter = mMonthAdapter;
                break;
            case SHOW_YEAR:
                adapter = mYearAdapter;
                break;
        }
        return adapter;
    }

    public WheelView getWheelView(int show) {
        WheelView adapter = null;
        switch (show) {
            case SHOW_MINUTE:
                adapter = mMinute;
                break;
            case SHOW_HOUR:
                adapter = mHour;
                break;
            case SHOW_DAY:
                adapter = mDay;
                break;
            case SHOW_MONTH:
                adapter = mMonth;
                break;
            case SHOW_YEAR:
                adapter = mYear;
                break;
        }
        return adapter;
    }
}
