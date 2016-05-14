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
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class ChangePwdController extends AppController<ChangePwdController.ChangePwdListener> {
    public ChangePwdController(ChangePwdListener l) {
        super(l);
    }

    public void changePwd(BaseRequest req) {
        UpdateTask task = new UpdateTask();
        task.load(req, BaseResponse.class, false);
    }

    private class UpdateTask extends AppBaseTask<BaseRequest, BaseResponse> {

        @Override
        public IUrl getUrl() {
            return URLConst.User.CHANGE_PWD;
        }

        @Override
        public void onSuccess(BaseResponse out, boolean fromCache) {
            mListener.onChangePwdSuccess(out);
        }

        @Override
        public void onError(RestError error) {
            mListener.onChangePwdFailure(error);
        }
    }

    public interface ChangePwdListener {
        void onChangePwdSuccess(BaseResponse out);

        void onChangePwdFailure(RestError error);
    }
}
