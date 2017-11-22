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

import android.content.Intent;

import cn.ieclipse.af.common.api.CommonController;
import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.demo.my.LoginActivity;
import cn.ieclipse.af.volley.IBaseResponse;
import cn.ieclipse.af.volley.InterceptorError;
import cn.ieclipse.af.volley.RestError;

/**
 * App base controller
 *
 * @author Harry
 */
public abstract class AppController<Listener> extends CommonController<Listener> {

    public AppController(Listener l) {
        super(l);
    }

    protected abstract class AppBaseTask<Input, Output> extends CommonBaseTask<Input, Output> {

        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (!checkListener()) {
                return true;
            }
            if (response instanceof BaseResponse) {
                BaseResponse resp = (BaseResponse) response;
                if (0 != resp.code) {
                    onLogicError(new LogicError(null, resp.code, resp.message));
                    throw new InterceptorError();
                }
            }

            return false;
        }

        public void onLogicError(LogicError error) {
            onError(new RestError(error));
            if (404 == error.getCode() || 104 == error.getCode()) {
                AppConfig.logout();
                // EventBus.getDefault().post(new LogoutEvent());
                if (context != null) {
                    Intent intent = LoginActivity.create(context);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                return;
            }
        }
    }
}
