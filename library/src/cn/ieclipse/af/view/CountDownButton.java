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

package cn.ieclipse.af.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * CountDownButton used for send phone verify code etc.
 *
 * @author Jamling
 */
public class CountDownButton extends RoundButton {
    private long totalTime = 60 * 1000;// 默认60秒
    private String label = "秒后重发";
    private String countDownText = "%s秒后重发";
    private long time;
    private long step = 1000;
    private int interval = 1000;
    private CountDownListener countDownListener;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            time -= step;
            if (countDownListener != null) {
                countDownListener.onCountDown(time);
            }
            if (time <= 0) {
                reset();
            }
            else {
                refreshText();
                mHandler.sendEmptyMessageDelayed(0, step);
            }
        }
    };
    
    public CountDownButton(Context context) {
        this(context, null);
    }
    
    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }
    
    /**
     * Start count down counter
     *
     * @param enable whether enable this button or not, default false
     */
    public void start(boolean enable) {
        this.setEnabled(enable);
        this.time = totalTime;
        refreshText();
        mHandler.sendEmptyMessageDelayed(0, step);
    }

    /**
     * Start count down counter
     *
     * @see #start(boolean)
     */
    public void start() {
        this.start(false);
    }

    private void refreshText() {
        long t = (time / interval);
        if (t > 0) {
            if (!TextUtils.isEmpty(countDownText)) {
                this.setText(String.format(countDownText, t));
            }
            else {
                this.setText(t + label);
            }
        }
    }
    
    public void reset() {
        this.time = 0;
        setText(null);
        setEnabled(true);
    }
    
    /**
     * Set count down total time
     * <p>
     * <code> setTotalTime(30000);// 30s</code>
     * </p>
     *
     * @param totalTime
     *
     * @return CountDownButton self
     */
    public CountDownButton setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    /**
     * Set count down step
     * <p>
     * <code> setStep(1000);// 1s</code>
     * </p>
     *
     * @param step count down step, micro seconds
     *
     * @return CountDownButton self
     */
    public CountDownButton setStep(long step) {
        if (step > 0) {
            this.step = step;
        }
        return this;
    }

    /**
     * Set count down text refresh interval.
     * <p>
     * <code> setInterval(1000);//1s, text display: (getRemainingTime() / interval) + label</code>
     * </p>
     *
     * @param interval count down text refresh interval, micro seconds
     *
     * @return CountDownButton self
     */
    public CountDownButton setInterval(int interval) {
        if (interval > 0) {
            this.interval = interval;
        }
        return this;
    }

    /**
     * Set the count down display text
     *
     * @param text e.g. %s秒后重发
     */
    public void setCountDownText(String text) {
        this.countDownText = text;
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public long getRemainingTime() {
        return time;
    }

    public long getStep() {
        return step;
    }

    /**
     * The count down listener
     */
    public interface CountDownListener {
        /**
         * The step count down callback, running on UI thread
         *
         * @param time the remaining time
         */
        void onCountDown(long time);
    }
}