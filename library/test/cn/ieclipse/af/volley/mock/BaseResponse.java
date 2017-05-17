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
package cn.ieclipse.af.volley.mock;

import cn.ieclipse.af.volley.IBaseResponse;

public class BaseResponse<Output> implements IBaseResponse {
    private static final long serialVersionUID = -3440061414071692254L;

    /**
     * 状态码
     */
    public int code;

    /**
     * 消息
     */
    public String message;

    /**
     * 数据
     */
    public Output data;

    public Output getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("code=%s, message=%s, data=%s", code, message, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseResponse && obj != null) {
            BaseResponse another = (BaseResponse) obj;
            if (getData() == null) {
                return another.getData() == null;
            }
            else {
                return getData().equals(another.getData());
            }
        }
        return false;
    }

    public void mock(){
        code = 0;
        message = "Success";
    }
}
