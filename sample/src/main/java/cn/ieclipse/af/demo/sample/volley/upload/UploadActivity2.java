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
package cn.ieclipse.af.demo.sample.volley.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import java.io.File;

import cn.ieclipse.af.demo.R;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2016年1月12日
 *       
 */
public class UploadActivity2 extends UploadActivity {
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_volley_upload;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("Upload multi images");
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        btn.setText("add image");
        retry.setText("upload files");
    }
    
    @Override
    public void onClick(View v) {
        if (v == btn) {
            Intent intent = new Intent();
            /* 开启Pictures画面Type设定为image */
            intent.setType("image/*");
            /* 使用Intent.ACTION_GET_CONTENT这个Action */
            intent.setAction(Intent.ACTION_GET_CONTENT);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, 1);
            return;
        }
        else if (v == retry) {
            if (TextUtils.isEmpty(et.getText())) {
                return;
            }
            uploadFiles();
            return;
        }
        
        super.onClick(v);
    }
    
    void uploadFile(String path) {
        if (TextUtils.isEmpty(et.getText())) {
            et.append(path);
        }
        else {
            et.append(";" + path);
        }
        
    }
    
    public void uploadFiles() {
        String[] fs = et.getText().toString().split(";");
        File[] files = new File[fs.length];
        for (int i = 0; i < fs.length; i++) {
            files[i] = new File(fs[i]);
        }
        MyUploadController controller = new MyUploadController(this);
        controller.setListener(this);
        controller.upload(files);
        sb = new StringBuilder();
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
