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
package cn.ieclipse.af.demo.common.api;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.volley.RestError;

/**
 * Volley Utils
 * 
 * @author Jamling
 *       
 */
public final class VolleyUtils {
    
    /**
     * private constructor
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
            return ((LogicError) error.getCause()).getDesc();
        }
        int type = error.getType();
        int resId;
        if (type == RestError.TYPE_AUTH) {
            resId = R.string.error_type_auth;
        }
        else if (type == RestError.TYPE_NO_CONNECTION) {
            resId = R.string.error_type_no_network;
        }
        else if (type == RestError.TYPE_TIMEOUT) {
            resId = R.string.error_type_timeout;
        }
        else if (type == RestError.TYPE_NETWORK) {
            resId = R.string.error_type_no_network;
        }
        else if (type == RestError.TYPE_SERVER) {
            resId = R.string.error_type_server;
        }
        else if (type == RestError.TYPE_PARSE) {
            resId = R.string.error_type_parse;
        }
        else {
            resId = R.string.error_type_unknown;
        }
        return context.getString(resId);
    }
}
