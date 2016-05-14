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
package cn.ieclipse.af.demo.my;

import cn.ieclipse.af.demo.common.api.AppController;
import cn.ieclipse.af.demo.common.api.BaseRequest;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.api.LogicError;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.RestError;

public class LogoutController extends AppController<LogoutController.LogoutListener> {

    public LogoutController(LogoutListener l) {
        super(l);
    }

    public void logout(BaseRequest request) {
        LogoutTask task = new LogoutTask();
        task.load(request, BaseResponse.class, false);
    }
    
    /**
     * 退出登录回调
     */
    public interface LogoutListener {
        void onLogoutSuccess(BaseResponse out);

        void onLogoutFailure(RestError error);
    }
    
    private class LogoutTask extends AppBaseTask<BaseRequest, BaseResponse> {

        @Override
        public void onSuccess(BaseResponse out, boolean fromCache) {
            mListener.onLogoutSuccess(out);
        }
        
        @Override
        public URLConst.Url getUrl() {
            return URLConst.User.LOGOUT;
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLogoutFailure(error);
        }
        
        /**
         * 可选的，像注册，登录这种，需要给出接口调用业务逻辑错误的原因，比如，用户不存在等
         */
        @Override
        public void onLogicError(LogicError error) {
            mListener.onLogoutFailure(new RestError(error));
        }
    }
}
