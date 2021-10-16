/*
 * Copyright (C) 2014 The Android Open Source Project
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


package cn.ieclipse.af.view;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

/**
 * A calendar-like view displaying a specified month and the appropriate selectable day numbers within the specified
 * month.
 */
public class SimpleMonthView extends View {
    private static final String TAG = "SimpleMonthView";

    private static final int DEFAULT_HEIGHT = 32;
    private static final int MIN_HEIGHT = 10;

    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final int DEFAULT_WEEK_START = Calendar.SUNDAY;
    private static final int DEFAULT_NUM_DAYS = 7;
    private static final int DEFAULT_NUM_ROWS = 6;
    private static final int MAX_NUM_ROWS = 6;

    private static final int SELECTED_CIRCLE_ALPHA = 60;

    private static final int DAY_SEPARATOR_WIDTH = 1;

    private static final int datepicker_day_number_size = 24;
    private static final int datepicker_month_day_label_text_size = 24;
    private static final int datepicker_day_number_select_circle_radius = 32;

    private int mMiniDayNumberTextSize = datepicker_day_number_size;
    private int mMonthDayLabelTextSize = datepicker_month_day_label_text_size;
    private int mDaySelectedCircleSize = datepicker_day_number_select_circle_radius;

    /**
     * Single-letter (when available) formatter for the day of week label.
     */
    private SimpleDateFormat mDayFormatter = new SimpleDateFormat("EEEEE", Locale.SIMPLIFIED_CHINESE);

    // affects the padding on the sides of this view
    private final int mPadding = 0;

    private Paint mDayNumberPaint;
    private Paint mDayNumberDisabledPaint;
    private Paint mDayNumberSelectedPaint;

    private Paint mMonthDayLabelPaint;

    private final Formatter mFormatter;
    private final StringBuilder mStringBuilder;

    private int mMonth;
    private int mYear;

    // Quick reference to the width of this view, matches parent
    private int mWidth;
    private int mHeight;
    private int mMonthHeaderSize;

    // The height this view should draw at in pixels, set by height param
    private int mRowHeight = DEFAULT_HEIGHT;

    // If this view contains the today
    private boolean mHasToday = false;

    // Which day is selected [0-6] or -1 if no day is selected
    private int mSelectedDay = -1;

    // Which day is today [0-6] or -1 if no day is today
    private int mToday = DEFAULT_SELECTED_DAY;

    // Which day of the week to start on [0-6]
    private int mWeekStart = DEFAULT_WEEK_START;

    // How many days to display
    private final int mNumDays = DEFAULT_NUM_DAYS;

    // The number of days + a spot for week number if it is displayed
    private int mNumCells = mNumDays;

    private int mDayOfWeekStart = 0;

    // First enabled day
    private int mEnabledDayStart = 1;

    // Last enabled day
    private int mEnabledDayEnd = 31;

    private final Calendar mCalendar = Calendar.getInstance();
    private final Calendar mDayLabelCalendar = Calendar.getInstance();

    private int mNumRows = DEFAULT_NUM_ROWS;

    // Optional listener for handling day click actions
    private OnDayClickListener mOnDayClickListener;

    private final int mNormalTextColor = 0xff4a4b4c;
    private final int mDisabledTextColor = 0xff8e99b6;
    private final int mSelectedDayTxtColor = 0xffffffff;
    private final int mSelectedDayColor = 0xffd20212;

    public SimpleMonthView(Context context) {
        this(context, null);
    }

    public SimpleMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SimpleMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        final Resources res = context.getResources();

        mStringBuilder = new StringBuilder(50);
        mFormatter = new Formatter(mStringBuilder, Locale.getDefault());

        mMiniDayNumberTextSize = datepicker_day_number_size;
        mMonthDayLabelTextSize = datepicker_month_day_label_text_size;
        mDaySelectedCircleSize = datepicker_day_number_select_circle_radius;

        //mRowHeight = (datepicker_view_animator_height - mMonthHeaderSize) / MAX_NUM_ROWS;

        // Sets up any standard paints that will be used
        initView();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDayFormatter = new SimpleDateFormat("EEEEE", newConfig.locale);
    }

    private static final int holo_blue_light = 0xffd20212;

    void setTextColor() {

        mMonthDayLabelPaint.setColor(holo_blue_light);

        mDayNumberDisabledPaint.setColor(mDisabledTextColor);

        mDayNumberSelectedPaint.setColor(mSelectedDayColor);

        //mDayNumberSelectedPaint.setAlpha(SELECTED_CIRCLE_ALPHA);
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        mOnDayClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                final int day = getDayFromLocation(event.getX(), event.getY());
                if (day >= 0) {
                    onDayClick(day);
                }
                break;
        }
        return true;
    }

    /**
     * Sets up the text and style properties for painting.
     */
    private void initView() {

        mMonthDayLabelPaint = new Paint();
        mMonthDayLabelPaint.setAntiAlias(true);
        mMonthDayLabelPaint.setTextSize(mMonthDayLabelTextSize);
        mMonthDayLabelPaint.setTextAlign(Align.CENTER);
        mMonthDayLabelPaint.setStyle(Style.FILL);
        mMonthDayLabelPaint.setFakeBoldText(true);

        mDayNumberSelectedPaint = new Paint();
        mDayNumberSelectedPaint.setAntiAlias(true);
        mDayNumberSelectedPaint.setColor(mSelectedDayColor);
        mDayNumberSelectedPaint.setAlpha(SELECTED_CIRCLE_ALPHA);
        mDayNumberSelectedPaint.setTextAlign(Align.CENTER);
        mDayNumberSelectedPaint.setStyle(Style.FILL);
        mDayNumberSelectedPaint.setFakeBoldText(true);

        mDayNumberPaint = new Paint();
        mDayNumberPaint.setAntiAlias(true);
        mDayNumberPaint.setTextSize(mMiniDayNumberTextSize);
        mDayNumberPaint.setTextAlign(Align.CENTER);
        mDayNumberPaint.setStyle(Style.FILL);
        mDayNumberPaint.setFakeBoldText(false);

        mDayNumberDisabledPaint = new Paint();
        mDayNumberDisabledPaint.setAntiAlias(true);
        mDayNumberDisabledPaint.setColor(mDisabledTextColor);
        mDayNumberDisabledPaint.setTextSize(mMiniDayNumberTextSize);
        mDayNumberDisabledPaint.setTextAlign(Align.CENTER);
        mDayNumberDisabledPaint.setStyle(Style.FILL);
        mDayNumberDisabledPaint.setFakeBoldText(false);
        setTextColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawWeekDayLabels(canvas);
        drawDays(canvas);
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    private static boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }

    /**
     * Sets all the parameters for displaying this week. Parameters have a default value and will only update if a new
     * value is included, except for focus month, which will always default to no focus month if no value is passed in.
     * The only required parameter is the week start.
     *
     * @param selectedDay the selected day of the month, or -1 for no selection.
     * @param month the month.
     * @param year the year.
     * @param weekStart which day the week should start on. {@link Calendar#SUNDAY} through {@link Calendar#SATURDAY}.
     * @param enabledDayStart the first enabled day.
     * @param enabledDayEnd the last enabled day.
     */
    public void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart,
        int enabledDayEnd) {
        if (mRowHeight < MIN_HEIGHT) {
            mRowHeight = MIN_HEIGHT;
        }

        mSelectedDay = selectedDay;

        if (isValidMonth(month)) {
            mMonth = month;
        }
        mYear = year;

        // Figure out what day today is
        final Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mHasToday = false;
        mToday = -1;

        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

        if (isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }

        if (enabledDayStart > 0 && enabledDayEnd < 32) {
            mEnabledDayStart = enabledDayStart;
        }
        if (enabledDayEnd > 0 && enabledDayEnd < 32 && enabledDayEnd >= enabledDayStart) {
            mEnabledDayEnd = enabledDayEnd;
        }

        mNumCells = getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mNumCells; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mHasToday = true;
                mToday = day;
            }
        }
        mNumRows = calculateNumRows();

        invalidate();
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public void reuse() {
        mNumRows = DEFAULT_NUM_ROWS;
        requestLayout();
    }

    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + mNumCells) / mNumDays;
        int remainder = (offset + mNumCells) % mNumDays;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    private boolean sameDay(int day, Time today) {
        return mYear == today.year &&
            mMonth == today.month &&
            day == today.monthDay;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;

        mRowHeight = (int) (mHeight * 1.0f / (mNumRows + 1));
        mMonthHeaderSize = mRowHeight;

        mMiniDayNumberTextSize = (int) (mMonthHeaderSize * 0.5f);
        mMonthDayLabelTextSize = mMiniDayNumberTextSize;
        mDaySelectedCircleSize = (int) (mMiniDayNumberTextSize * 0.7f);

        initView();
    }

    public String getMonthAndYearString() {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
            | DateUtils.FORMAT_NO_MONTH_DAY;
        mStringBuilder.setLength(0);
        long millis = mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), mFormatter, millis, millis, flags,
            Time.getCurrentTimezone()).toString();
    }

    private void drawWeekDayLabels(Canvas canvas) {
        final int y = mMonthHeaderSize - (mMonthDayLabelTextSize / 2);
        final int dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2);

        for (int i = 0; i < mNumDays; i++) {
            final int calendarDay = (i + mWeekStart) % mNumDays;
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);

            final String dayLabel = mDayFormatter.format(mDayLabelCalendar.getTime());
            final int x = (2 * i + 1) * dayWidthHalf + mPadding;
            canvas.drawText(dayLabel, x, y, mMonthDayLabelPaint);
        }
    }

    /**
     * Draws the month days.
     */
    private void drawDays(Canvas canvas) {
        int y = (((mRowHeight + mMiniDayNumberTextSize) / 2) - DAY_SEPARATOR_WIDTH)
            + mMonthHeaderSize;
        int dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2);
        int j = findDayOffset();
        for (int day = 1; day <= mNumCells; day++) {
            int x = (2 * j + 1) * dayWidthHalf + mPadding;
            if (mSelectedDay == day && day >= mEnabledDayStart && day <= mEnabledDayEnd) {
                canvas.drawCircle(x, y - (mMiniDayNumberTextSize / 3), mDaySelectedCircleSize,
                    mDayNumberSelectedPaint);
                mDayNumberPaint.setColor(mSelectedDayTxtColor);
            } else {
                mDayNumberPaint.setColor(mNormalTextColor);
            }
            final Paint paint = (day < mEnabledDayStart || day > mEnabledDayEnd) ?
                mDayNumberDisabledPaint : mDayNumberPaint;
            canvas.drawText(String.format("%d", day), x, y, paint);
            j++;
            if (j == mNumDays) {
                j = 0;
                y += mRowHeight;
            }
        }
    }

    private int findDayOffset() {
        return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart) - mWeekStart;
    }

    /**
     * Calculates the day that the given x position is in, accounting for week number. Returns the day or -1 if the
     * position wasn't in a day.
     *
     * @param x The x position of the touch event
     * @return The day number, or -1 if the position wasn't in a day
     */
    private int getDayFromLocation(float x, float y) {
        int dayStart = mPadding;
        if (x < dayStart || x > mWidth - mPadding) {
            return -1;
        }
        // Selection is (x - start) / (pixels/day) == (x -s) * day / pixels
        int row = (int) (y - mMonthHeaderSize) / mRowHeight;
        int column = (int) ((x - dayStart) * mNumDays / (mWidth - dayStart - mPadding));

        int day = column - findDayOffset() + 1;
        day += row * mNumDays;
        if (day < 1 || day > mNumCells) {
            return -1;
        }
        return day;
    }

    /**
     * Called when the user clicks on a day. Handles callbacks to the {@link OnDayClickListener} if one is set.
     *
     * @param day The day that was clicked
     */
    private void onDayClick(int day) {
        if (day >= mEnabledDayStart && day <= mEnabledDayEnd) {
            if (mOnDayClickListener != null) {
                Calendar date = Calendar.getInstance();
                date.set(mYear, mMonth, day);
                mOnDayClickListener.onDayClick(this, date);
            }

            mSelectedDay = day;
            postInvalidate();
        }
    }

    /**
     * Handles callbacks when the user clicks on a time object.
     */
    public interface OnDayClickListener {
        void onDayClick(SimpleMonthView view, Calendar day);
    }
}
