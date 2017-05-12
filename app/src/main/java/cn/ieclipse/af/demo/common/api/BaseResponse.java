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
package cn.ieclipse.af.demo.common.api;

import cn.ieclipse.af.volley.IBaseResponse;

public class BaseResponse<Output> implements IBaseResponse {
    private static final long serialVersionUID = -3440061414071692254L;

    /**
     * 状态码
     */
    private int status;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Output data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Output getData() {
//        if (data instanceof String){
//            return (String) data;
//        }
//        return new Gson().toJson(data);
        return data;
    }

    public void setData(Output data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("status=%s, message=%s, data=%s", getStatus(), getMessage(), getData());
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
}
