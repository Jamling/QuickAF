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
package cn.ieclipse.af.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 防止多次点击出现多个toast
 *
 * @author wangjian
 * @date 2016-09-01.
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
