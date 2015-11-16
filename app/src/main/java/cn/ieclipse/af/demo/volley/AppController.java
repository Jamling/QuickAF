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

import cn.ieclipse.af.volley.Controller;
import cn.ieclipse.af.volley.GsonRequest;
import cn.ieclipse.af.volley.IBaseResponse;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/11/16.
 */
public abstract class AppController<Listener> extends Controller<Listener> {
    
    public abstract class AppBaseTask<Input, Output>
            extends RequestObjectTask<Input, Output> {
            
        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (response instanceof AppBaseResponse) {
                AppBaseResponse resp = (AppBaseResponse) response;
                if (resp.errNum != 0) {
                    throw new LogicError(resp);
                }
            }
            return false;
        }
        
        @Override
        protected void customRequest(GsonRequest request) {
            super.customRequest(request);
            request.addHeader("apikey", "e8c043231152d9cbcf30a648382ca4c5");
        }
    }
}
