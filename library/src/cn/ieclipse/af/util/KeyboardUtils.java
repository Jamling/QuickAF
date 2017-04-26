/*
 * Copyright 2014-2015 ieclipse.cn.
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

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/29.
 */
public final class KeyboardUtils {
    private KeyboardUtils() {

    }

    /**
     * 点击edit text之外的区域，自动隐藏keyboard
     *
     * @param context Activity context
     * @param ev      MotionEvent
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     * @see View#dispatchTouchEvent(android.view.MotionEvent)
     */
    public static void autoHideSoftInput(Activity context, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = context.getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v);
            }
        }
    }

    /**
     * Auto hide keyboard when tap a view anywhere except EditText
     * @param rootView root view
     */
    public static void autoHideSoftInput(View rootView) {
        if (rootView != null) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    if (v.getContext() instanceof Activity) {
                        autoHideSoftInput((Activity) v.getContext(), ev);
                    }
                    return false;
                }
            });
        }
    }

    private static Rect getViewLocation(View v) {
        int[] l = {0, 0};
        v.getLocationInWindow(l);// v.getLocationOnScreen(l);
        int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
        return new Rect(left, top, right, bottom);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    private static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            Rect rect = getViewLocation(v);
            return !rect.contains((int) event.getX(), (int) event.getY());
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏输入法
     *
     * @param view typical an EditText
     */
    public static void hideSoftInput(View view) {
        if (view == null) {
            return;
        }
        IBinder token = view.getWindowToken();
        if (token != null) {
            InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 弹出输入法
     *
     * @param view typical an EditText
     */
    public static void showSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(
            Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
}
