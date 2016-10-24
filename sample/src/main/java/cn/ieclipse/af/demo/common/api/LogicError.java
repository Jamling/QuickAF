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

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public class LogicError extends VolleyError {
    private static final long serialVersionUID = 7739186324576518504L;
    private String status;
    private String desc;
    
    public LogicError(NetworkResponse response, String status, String message) {
        super(response);
        this.desc = message;
        this.status = status;
    }
    
    /**
     * 获取服务端返回的status
     * 
     * @return
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 获取服务端返回的message
     * 
     * @return
     */
    public String getDesc() {
        return desc == null ? "null" : desc;
    }
}
