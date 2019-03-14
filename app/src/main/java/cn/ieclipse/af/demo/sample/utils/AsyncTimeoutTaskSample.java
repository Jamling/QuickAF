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
package cn.ieclipse.af.demo.sample.utils;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.ieclipse.af.app.AfDialogFragment;
import cn.ieclipse.af.common.AsyncTimeoutTask;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class AsyncTimeoutTaskSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_async_timeout_task;
    }

    AsyncTimeoutTask task;

    ProgressFragment dialog;

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            task = new TestTask();
            boolean mockex = chk1.isChecked();
            boolean mockto = chk2.isChecked();
            if (mockto) {
                task.setTimeout(10 * 1000);
            }
            task.execute(mockex, mockto);
        }
        else if (v == btn2) {
            if (task != null) {
                task.cancel(true);
            }
        }
        else if (v == btn3) {
            tv2.setText(null);
        }
        super.onClick(v);
    }

    private void onStartLoading() {
        dialog = new ProgressFragment();
        dialog.setStyle(AfDialogFragment.STYLE_NO_TITLE, 0);
        dialog.setCanceledOnTouchOutside(false);
        Bundle bundle = new Bundle();
        bundle.putString("title", "Loading...");
        bundle.putString("msg", String.format("do something step %d/%d progress %d, please waiting", 0, 0, 0));
        dialog.setArguments(bundle);
        dialog.setDialogListener(new ProgressFragment.CancelDialogListener(){
            @Override
            public void onCancel(ProgressFragment dialog) {
                onClick(btn2);
            }
        });
        dialog.show(getFragmentManager(), true);

    }

    private void updateProgress(int p, int c, int t) {
        dialog.setMessage(String.format("do something step %d/%d progress %d, please waiting", c, t, p));
    }

    @Override
    public void hideLoadingDialog() {
        super.hideLoadingDialog();
        dialog.dismiss();
    }

    private class TestTask extends AsyncTimeoutTask<Object, Integer, String> {

        @Override
        protected String doInBackground() throws Exception {
            boolean mockException = (boolean) params[0];
            boolean mockTimeout = (boolean) params[1];
            if (mockException) {
                Thread.sleep(3000);
                throw new NullPointerException("mock NullPointerException");
            }
            int count = 15;
            int i = 0;
            publishProgress(0, i, count);
            while (i < count) {
                Thread.sleep(1000);
                i++;
                publishProgress(i * 100 / count, i, count);
            }
            return String.valueOf(i);
        }

        @Override
        protected void onPreExecute() {
            onStartLoading();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (isFinish()) {
                return;
            }
            int p = values[0];
            int c = values[1];
            int t = values[2];
            updateProgress(values[0], values[1], values[2]);
            String msg = String.format("step %d/%d progress %d", c, t, p);
            tv2.setText(tv2.getText() + toString() + " onProgressUpdate " + msg + " \n");
        }

        @Override
        protected void onError(Exception e) {
            hideLoadingDialog();
            tv2.setText(tv2.getText() + toString() + " onError " + e + " \n");
        }

        @Override
        protected void onSuccess(String s) {
            hideLoadingDialog();
            tv2.setText(tv2.getText() + toString() + " onSuccess " + s + " \n");
        }

        @Override
        protected void onCancelled() {
            hideLoadingDialog();
            tv2.setText(tv2.getText() + toString() + " onCanceled " + " \n");
        }

        @Override
        public String toString() {
            return "Task@" + hashCode() + " ";
        }
    }

    public static class ProgressFragment extends AfDialogFragment<ProgressFragment.CancelDialogListener> {

        private TextView tvTitle;
        private TextView tvMessage;

        private String title;
        private String message;

        @Override
        protected int getContentLayout() {
            return R.layout.sample_dialog_alert;
        }

        @Override
        protected void initContentView(View view) {
            super.initContentView(view);
            tvTitle = (TextView) view.findViewById(android.R.id.title);
            tvMessage = (TextView) view.findViewById(android.R.id.message);
            title = getArguments().getString("title");
            message = getArguments().getString("msg");
            setTitle(title);
            setMessage(message);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            if (listener != null) {
                listener.onCancel(this);
            }
        }

        public void setTitle(String s) {
            if (tvTitle != null) {
                tvTitle.setText(s);
            }
        }

        public void setMessage(String s) {
            if (tvMessage != null) {
                tvMessage.setText(s);
            }
        }

        public interface CancelDialogListener {
            void onCancel(ProgressFragment dialog);
        }
    }
}
