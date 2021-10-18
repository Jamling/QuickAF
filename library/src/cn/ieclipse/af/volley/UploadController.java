/*
 * Copyright 2014-2016 ieclipse.cn.
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
package cn.ieclipse.af.volley;

import android.os.Handler;
import android.os.Message;

import java.io.File;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/7/23.
 */
public class UploadController<Listener> extends Controller<Listener> {

    protected abstract class AbstractUploadTask<Output> extends
        RequestObjectTask<File, Output> implements UploadProgressListener {

        @Override
        public void onSuccess(Output out, boolean fromCache) {
            onUploadSuccess(input, out);
        }

        @Override
        public void onError(RestError error) {
            onUploadFailure(input, error);
        }

        @Override
        protected String getBody(File input) {
            return null;
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            UploadRequest request = new UploadRequest(url.getMethod(), url.getUrl(), body, this, this, this);
            upload(request);
            return request;
        }

        @Override
        public void updateProgress(long transferred, long total, int progress) {
            Message msg = new Message();
            msg.what = progress;
            msg.obj = new long[]{transferred, total, progress};
            handler.sendMessage(msg);
        }

        /**
         * upload file or string parameters to body or set your custom request parameter
         * see {@link cn.ieclipse.af.volley.UploadRequest#addBody(String, java.io.File, String)}
         *
         * @param request
         * @see cn.ieclipse.af.volley.UploadRequest#addBody(String, java.io.File, String)
         * @see cn.ieclipse.af.volley.UploadRequest#addBitmapBody(String, java.io.File)
         */
        protected abstract void upload(UploadRequest request);

        protected abstract void onUploadFailure(File file, RestError error);

        protected abstract void onUploadSuccess(File file, Output out);

        public abstract void onProgress(long transferred, long total, int progress);

        private final Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                long[] p = (long[]) msg.obj;
                onProgress(p[0], p[1], (int) p[2]);
            }
        };
    }
}
