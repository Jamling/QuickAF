/*
 * Copyright (C) 2015-2016 QuickAF2
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
package cn.ieclipse.af.demo.sample.appui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.FileChooserActivity;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class FileChooserSample extends SampleBaseFragment {

    public String dir;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_chooser_file;
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            FileChooserActivity.Params params = new FileChooserActivity.Params();
            params.initDir = dir;
            Intent intent = FileChooserActivity.create(getActivity(), params);
            startActivityForResult(intent, 1);
        }

        else if (v == btn2) {
            FileChooserActivity.Params params = new FileChooserActivity.Params();
            params.maxCount = 99;
            params.chooserMode = FileChooserActivity.Params.CHOOSER_ALL;
            Intent intent = FileChooserActivity.create(getActivity(), params);
            startActivityForResult(intent, 2);
        }
        else if (v == btn3) {
            FileChooserActivity.Params params = new FileChooserActivity.Params();
            params.maxCount = 1;
            params.chooserMode = FileChooserActivity.Params.CHOOSER_FOLDER;
            Intent intent = FileChooserActivity.create(getActivity(), params);
            startActivityForResult(intent, 3);
        }
        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1 || requestCode == 3) {
                dir = data.getStringExtra(Intent.EXTRA_RETURN_RESULT);
                tv1.setText(dir);
            }
            else if (requestCode == 2) {
                tv1.setText(data.getSerializableExtra(Intent.EXTRA_RETURN_RESULT).toString());
            }
        }
        else {
            tv1.setText(null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
