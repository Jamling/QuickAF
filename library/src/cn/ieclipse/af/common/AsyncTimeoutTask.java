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
package cn.ieclipse.af.common;

import android.os.AsyncTask;

import java.util.concurrent.TimeoutException;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class AsyncTimeoutTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected Params[] params;
    private Result result;
    private Exception exception;
    private long timeout;
    private boolean finish;
    private boolean finish2;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };
//    private static final int MSG_EXCEPTION = -1;
//    private static final int MSG_INTERRUPT = -2;
//    private static final int MSG_TIMEOUT = -3;

    @Override
    protected Result doInBackground(Params... params) {
        this.params = params;
        if (timeout <= 0) {
            try {
                result = doInBackground();
            } catch (Exception e) {
                exception = e;
            }
            finish = true;
            finish2 = true;
            return result;
        }
        final Thread thread = new Thread() {
            public void run() {
                try {
                    result = doInBackground();
                } catch (Exception e) {
                    exception = e;
                }
                finish = true;
            }
        };
        thread.start();
        try {
            thread.join(timeout); // 阻塞当前线程，等待thread执行完毕，可以带参数，最多等待多长时间
        } catch (InterruptedException e) {
            // Thread.join()可以被interrupt，调用AsyncTask.cancel(true);即可退出等待
            exception = e;
            thread.interrupt();
            return result;
        }
        finish2 = true;
        return result;
    }

    protected abstract Result doInBackground() throws Exception;

    @Override
    protected void onPostExecute(Result result) {
        if (exception != null) {
            onError(exception);
        }
        else {
            if (finish) {
                onSuccess(result);
            }
            else {
                String msg = String.format("the async task (%s) not responded in %s", toString(), timeout);
                onError(new TimeoutException(msg));
            }
        }
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        if (!isFinish()) {
            return;
        }
    }

    protected void onError(Exception e) {

    }

    protected void onSuccess(Result result) {

    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isFinish() {
        return finish2;
    }
}
