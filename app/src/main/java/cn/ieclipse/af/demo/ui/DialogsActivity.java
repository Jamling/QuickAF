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
package cn.ieclipse.af.demo.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import cn.ieclipse.af.app.AlertDialogFragment.AlertInterceptor;
import cn.ieclipse.af.demo.BaseActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.DialogUtils;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年12月1日
 *       
 */
public class DialogsActivity extends BaseActivity {
    
    @Override
    protected int getContentLayout() {
        return R.layout.activity_dialogs;
    }
    
    public void showToast(View view) {
        DialogUtils.showToast(this, "DialogUtils.showToast(context, msg)");
        
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
    
    public void showCustomAlert(View view) {
        DialogUtils.showAlert(this, new AlertInterceptor() {
            
            @Override
            public void onCreated(Builder builder) {
                TextView view = new TextView(DialogsActivity.this);
                view.setText(Html.fromHtml(
                        "I am use <font color='blue'>cn.ieclipse.af.util.DialogUtils.showAlert(Activity context, AlertInterceptor interceptor)</font> to show an alert Dialog, you can access all method of AlertDialog in <font color='#ff0000'>AlertInteceptor.onDialogCreated()</font>"));
                builder.setView(view);
                builder.setTitle(getString(android.R.string.dialog_alert_title));
                builder.setPositiveButton("我可以更改Button名字哦", DialogUtils.defaultOnClick());
            }
        });
    }
    
    public void showCommonAlert(View view) {
        DialogUtils.showAlert(this, android.R.drawable.ic_dialog_alert,
                getString(android.R.string.dialog_alert_title), "我是最简单的警告对话框");
    }
    
    public void showCommonAlertWithListener(View view) {
        DialogUtils.showAlert(this, android.R.drawable.ic_dialog_alert,
                getString(android.R.string.dialog_alert_title),
                "我是简单的警告对话框, 带一个Button", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogUtils.showToast(DialogsActivity.this, "我点击的是确定");
                    }
                });
    }
}
