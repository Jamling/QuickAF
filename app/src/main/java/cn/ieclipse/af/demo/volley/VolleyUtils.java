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
package cn.ieclipse.af.demo.volley;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月16日
 *       
 */
public final class VolleyUtils {
    
    /**
     * 
     */
    private VolleyUtils() {
    }
    
    public static void toastError(Context context, RestError error) {
        String msg = getError(context, error);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
    public static void showError(TextView tv, RestError error) {
        tv.setText(getError(tv.getContext(), error));
    }
    
    private static String getError(Context context, RestError error) {
        if (error.getCause() instanceof LogicError) {
            return error.getCause().getMessage();
        }
        int type = error.getType();
        if (type > 0) {
            return String.format("code=%d,message=%s", error.getType(),
                    error.getCause().getMessage());
        }
        return "unknown error";
    }
}
