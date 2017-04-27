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
package cn.ieclipse.af.demo.sample.volley.weather;

import com.google.gson.Gson;

import cn.ieclipse.af.volley.IBaseResponse;

/**
 * Base response for weather sample
 *
 * @author Jamling
 */
public class WeatherBaseResponse implements IBaseResponse {
    
    public int errNum;
    public String errMsg;
    public Object retData;
    
    @Override
    public String getData() {
        if (retData instanceof String) {
            return (String) retData;
        }
        return new Gson().toJson(retData);
    }
}
