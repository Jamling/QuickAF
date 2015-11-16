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

import com.google.gson.Gson;

import android.os.IInterface;
import cn.ieclipse.af.volley.IBaseResponse;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月10日
 */
public class AppBaseResponse implements IBaseResponse {
    
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
