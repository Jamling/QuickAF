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
package cn.ieclipse.af.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月9日
 *       
 */
public class RestError extends Exception {
    public static final int TYPE_NO_CONNECTION = 0x01;
    public static final int TYPE_TIMEOUT = 0x02;
    public static final int TYPE_NETWORK = 0x03;
    public static final int TYPE_AUTH = 0x04;
    public static final int TYPE_SERVER = 0x05;
    public static final int TYPE_PARSE = 0x10;
    public static final int TYPE_CLIENT = 0x11;
    public int mErrorType;
    
    public RestError(VolleyError error) {
        super(error);
        initType();
    }
    
    private void initType() {
        Throwable error = getCause();
        if (error == null) {
            return;
        }
        if (error instanceof AuthFailureError) {
            mErrorType = TYPE_AUTH;
        }
        else if (error instanceof NoConnectionError) {
            mErrorType = TYPE_NO_CONNECTION;
        }
        else if (error instanceof TimeoutError) {
            mErrorType = TYPE_TIMEOUT;
        }
        else if (error instanceof NetworkError) {
            mErrorType = TYPE_NETWORK;
        }
        else if (error instanceof ServerError) {
            mErrorType = TYPE_SERVER;
        }
        else if (error instanceof ParseError) {
            mErrorType = TYPE_PARSE;
        }
        else if (error instanceof ClientError) {
            mErrorType = TYPE_CLIENT;
        }
        else {
            mErrorType = 0;
        }
    }
    
    public int getType() {
        return mErrorType;
    }
}
