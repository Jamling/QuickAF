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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;

import cn.ieclipse.af.app.AlertDialogFragment;
import cn.ieclipse.af.demo.common.api.AppController;
import cn.ieclipse.af.demo.common.api.BasePostRequest;
import cn.ieclipse.af.demo.common.api.BaseRequest;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.volley.GsonRequest;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * Check version update controller
 *
 * @author Jamling
 */
public class CheckUpdateController extends AppController<CheckUpdateController.UpdateListener> {
    public CheckUpdateController(UpdateListener l) {
        super(l);
    }

    public interface UpdateListener {
        void onCheckSuccess(CheckResponse info);

        //void onCheckFailure(RestError error);
    }

    public static class CheckRequest extends BasePostRequest {
        public String versionName;
        public String versionCode;
    }

    public static class CheckResponse implements java.io.Serializable {
        public String versionName;
        public String downloadLink;
        public String description;
        public boolean force;
    }

    public void checkUpdate(BaseRequest request) {
        CheckTask task = new CheckTask();

        task.load(request, CheckResponse.class, false);
    }

    private class CheckTask extends AppBaseTask<BaseRequest, CheckResponse> {

        @Override
        public IUrl getUrl() {
            return null;
        }

        @Override
        public void onSuccess(CheckResponse out, boolean fromCache) {
            mListener.onCheckSuccess(out);
        }

        @Override
        public void onError(RestError error) {
            mListener.onCheckSuccess(null);
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            GsonRequest request = super.buildRequest(url, body);
            request.setRetryPolicy(new DefaultRetryPolicy(2500, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            return request;
        }
    }

    public static class DefaultUpdateListener implements UpdateListener {

        Context context;
        boolean manual;

        public DefaultUpdateListener(boolean manual, Context context) {
            this.manual = manual;
            this.context = context;
        }

        @Override
        public void onCheckSuccess(final CheckResponse info) {
            if (context != null && context instanceof Activity) {
                if (info != null) {
                    final Activity activity = (Activity) context;
                    AlertDialogFragment dialog = DialogUtils.showAlert(activity, android.R.drawable.ic_dialog_info,
                        "发现新版本", TextUtils.isEmpty(info.description) ? "发现新版本，是否升级？" : Html.fromHtml(info.description),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    if (info.force) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }
                                    intent.setData(Uri.parse(info.downloadLink));
                                    activity.startActivityForResult(Intent.createChooser(intent, "请选择浏览器下载"), 0x01);
                                } catch (Exception e) {
                                    DialogUtils.showToast(activity.getApplicationContext(), "无法打开浏览器下载");
                                } finally {

                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (info.force) {
                                    activity.finish();
                                }
                            }
                        });
                }
                else if (manual) {
                    DialogUtils.showToast(context, "已是最新版本");
                }
            }
        }
    }
}
