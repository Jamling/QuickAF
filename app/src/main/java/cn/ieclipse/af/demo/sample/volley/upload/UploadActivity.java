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
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.common.ui.FileChooserActivity;
import cn.ieclipse.af.util.SharedPrefsUtils;
import cn.ieclipse.af.util.StringUtils;
import cn.ieclipse.af.volley.RestError;
import cn.ieclipse.af.volley.VolleyConfig;
import cn.ieclipse.af.volley.VolleyManager;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016年1月12日
 */
public class UploadActivity extends BaseActivity implements MyUploadController.UploadListener {

    ImageView iv;
    Button btnImg;
    Button btnFile;
    EditText et;
    Button retry;
    EditText etTarget;
    TextView tvUrl;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_volley_upload;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("Upload single image");
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        iv = (ImageView) view.findViewById(R.id.iv);
        et = (EditText) view.findViewById(R.id.et_text);
        btnImg = (Button) view.findViewById(R.id.btn1);
        retry = (Button) view.findViewById(R.id.btn2);
        btnFile = (Button) view.findViewById(R.id.btn3);
        et.setText(SharedPrefsUtils.getString("upload_path"));
        setOnClickListener(btnImg, retry, btnFile);

        etTarget = (EditText) view.findViewById(R.id.et5);
        tvUrl = (TextView) view.findViewById(R.id.tv1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnImg) {
            Intent intent = new Intent();
            /* 开启Pictures画面Type设定为image */
            intent.setType("image/*");
            /* 使用Intent.ACTION_GET_CONTENT这个Action */
            intent.setAction(Intent.ACTION_GET_CONTENT);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, 1);
        }
        else if (v == btnFile) {
            FileChooserActivity.Params params = FileChooserActivity.Params.newSingleFileParams();
            Intent intent = FileChooserActivity.create(this, params);
            startActivityForResult(intent, 2);
        }
        else if (v == retry) {
            if (TextUtils.isEmpty(et.getText())) {
                return;
            }
            uploadFile(et.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x01) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                    iv.setImageBitmap(bitmap);

                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    uploadFile(path);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
            else if (requestCode == 0x02) {
                uploadFile(data.getStringExtra(Intent.EXTRA_RETURN_RESULT));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // here set base response, but you can change it in single controller

        VolleyConfig config = new VolleyConfig.Builder().setBaseResponseClass(BaseResponse.class).build();
        VolleyManager.init(getApplicationContext(), config);
    }

    ProgressDialog dialog;
    StringBuilder sb;

    void uploadFile(String path) {
        et.setText(path);
        SharedPrefsUtils.putString("upload_path", path);
        MyUploadController controller = new MyUploadController(this);
        controller.upload(etTarget.getText().toString(), null, new File(path));
        sb = new StringBuilder();
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onProgress(final long transffered, final long total, final int progress) {
        final String str = String.format("%s / %s (%s%s)", transffered, total, progress, "%");
        System.out.println(str);
        sb.append(str + "\n");
        if (dialog != null) {
            dialog.setProgress(progress);
            dialog.setMessage(sb.toString());
        }
    }

    @Override
    public void onUploadFailure(File file, RestError error) {
        if (dialog != null) {
            dialog.setMessage(String.format(" upload fail error : %s", error.getMessage()));
        }
    }

    @Override
    public void onUploadSuccess(File file, UploadInfo out) {
        if (dialog != null) {
            dialog.setProgress(100);
            final String str = String.format("response: %s", out);
            System.out.println(str);
            sb.append(str + "\n");
            dialog.setMessage(sb.toString());
        }
        tvUrl.setText(out.file_path);
    }

    @Override
    public void onUploadSuccess(File[] files, List<UploadInfo> out) {
        if (dialog != null) {
            dialog.setProgress(100);
            final String str = String.format("response: %s", out);
            System.out.println(str);
            sb.append(str + "\n");
            dialog.setMessage(sb.toString());
        }
        List<String> urls = new ArrayList<>();
        if (out != null) {
            for (UploadInfo info : out) {
                urls.add(info.file_path);
            }
        }
        tvUrl.setText(StringUtils.join(", ", urls));
    }
}
