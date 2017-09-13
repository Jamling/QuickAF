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

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.demo.my.LoginActivity;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.volley.Controller;
import cn.ieclipse.af.volley.IBaseResponse;
import cn.ieclipse.af.volley.InterceptorError;
import cn.ieclipse.af.volley.RestError;

/**
 * App base controller
 *
 * @author Harry
 */
public abstract class AppController<Listener> extends cn.ieclipse.af.volley.Controller<Listener> {

    public AppController(Listener l) {
        super();
        setListener(l);
    }

    protected abstract class AppBaseTask<Input, Output> extends RequestObjectTask<Input, Output> {
        protected Context context;

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

        protected boolean checkListener() {
            if (mListener == null) {
                if (Controller.DEBUG) {
                    Controller.log("listener is null");
                }
                return false;
            }
            else if (mListener instanceof Activity) {
                if (Build.VERSION.SDK_INT >= 17 && ((Activity) mListener).isDestroyed()) {
                    if (Controller.DEBUG) {
                        Controller.log("listener(Activity) has been destroyed");
                    }
                    return false;
                }
                context = (Context) mListener;
            }
            else if (mListener instanceof Fragment) {
                if (!((Fragment) mListener).isAdded() || ((Fragment) mListener).isDetached()) {
                    if (Controller.DEBUG) {
                        Controller.log("listener(Fragment) is not added");
                    }
                    return false;
                }
            }
            return true;
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

        int count;

        @Override
        public void onError(RestError restError) {
            if (count == 0) {
                count++;
                mock();
            }
        }

        // if you need mock response, call listener.onXXXSuccess(mocked data)
        protected void mock() {

        }

        protected Output mockOutput(int max) {
            if (mDataClazz != null) {
                return mock(mDataClazz);
            }
            else if (mDataItemClass != null) {
                return (Output) mockList(max, mDataItemClass);
            }
            return null;
        }

        protected <T> T mock(Class<T> clazz) {
            try {
                T output = clazz.newInstance();
                try {
                    Method m = clazz.getDeclaredMethod("mock", (Class<?>[]) null);
                    m.invoke(output, (Object[]) null);
                } catch (Exception e) {
                    //
                }
                return output;
            } catch (Exception e) {
                return null;
            }
        }

        protected <T> List<T> mockList(int max, Class<T> clazz) {
            int size = RandomUtils.genInt(max);
            List<T> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                T t = mock(clazz);
                if (t != null) {
                    list.add(t);
                }
            }
            return list;
        }
    }
}
