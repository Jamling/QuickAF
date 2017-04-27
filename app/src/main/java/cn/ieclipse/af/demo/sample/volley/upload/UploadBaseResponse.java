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

package cn.ieclipse.af.demo.sample.volley.upload;

import com.google.gson.Gson;

import cn.ieclipse.af.volley.IBaseResponse;

public class UploadBaseResponse implements IBaseResponse {
    
    public String status;
    public String message;
    public Object data;
    
    @Override
    public String getData() {
        if (data instanceof String) {
            return (String) data;
        }
        return new Gson().toJson(data);
    }
}
