/*
 * Copyright (C) 2015-2017 QuickAF
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

import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */

public class SimpleController<Output> extends AppController<SimpleController.SimpleListener> {
    public SimpleController(SimpleListener l) {
        super(l);
    }

    public void load(URLConst.Url url, Object req, Class<Output> outputClass) {
        SimpleTask task = new SimpleTask();
        task.setUrl(url);
        task.load(req, outputClass, false);
    }

    public interface SimpleListener<Output> {
        void onSuccess(Output output);

        void onError(RestError error);
    }

    private class SimpleTask extends AppBaseTask<Object, Output> {
        URLConst.Url url;

        public void setUrl(URLConst.Url url) {
            this.url = url;
        }

        @Override
        public IUrl getUrl() {
            return url;
        }

        @Override
        public void onSuccess(Output output, boolean b) {
            mListener.onSuccess(output);
        }

        @Override
        public void onError(RestError restError) {
            mListener.onError(restError);
            super.onError(restError);
        }

        @Override
        protected void mock() {
            mListener.onSuccess(mockOutput(0));
        }
    }

    public static <Output> void request(URLConst.Url url, Object req, Class<Output> clazz, SimpleListener listener) {
        SimpleController<Output> controller = new SimpleController<>(listener);
        controller.load(url, req, clazz);
    }
}
