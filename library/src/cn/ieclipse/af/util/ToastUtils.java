/*
 * Copyright (C) 2015 FengYunDuoBao All rights reserved
 */
package cn.ieclipse.af.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 防止多次点击出现多个toast
 *
 * @author wangjian
 * @date 2016/9/1.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, CharSequence desc) {
        // 防止多次点击出现多个toast
        if (mToast == null) {
            mToast = Toast.makeText(context, desc, Toast.LENGTH_LONG);
        }
        else {
            mToast.setText(desc);
        }
        mToast.show();
    }

    public static void showToast(Context context, int resid) {
        if (context != null) {
            String desc = context.getString(resid);
            showToast(context, desc);
        }
    }

    public static void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

    public static void setDuration(int duration) {
        if (mToast != null && mToast.getDuration() != duration) {
            mToast.setDuration(duration);
        }
    }

    public static Toast getToast() {
        return mToast;
    }


}
