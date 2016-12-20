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
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

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

    private int[] modes
        = new int[]{FileChooserActivity.Params.CHOOSER_FILE, FileChooserActivity.Params.CHOOSER_FOLDER,
        FileChooserActivity.Params.CHOOSER_ALL};

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_chooser_file;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        addSort("Folder first", FileChooserActivity.Params.SORT_FOLDER_FIRST);
        addSort("Name asc", FileChooserActivity.Params.SORT_NAME_ASC);
        addSort("Name desc", FileChooserActivity.Params.SORT_NAME_DESC);
        addSort("Date asc", FileChooserActivity.Params.SORT_DATE_ASC);
        addSort("Date desc", FileChooserActivity.Params.SORT_DATE_DESC);
        addSort("Size asc", FileChooserActivity.Params.SORT_SIZE_ASC);
        addSort("Size desc", FileChooserActivity.Params.SORT_SIZE_DESC);
    }

    private void addSort(String name, int i) {
        CheckBox chk = new CheckBox(getActivity());
        chk.setText(name);
        chk.setTag(i);
        fl1.addView(chk);
    }

    private int getSort() {
        int ret = 0;
        List<View> views = fl1.getCheckedViews();
        for (int i = 0; i < views.size(); i++) {
            int v = Integer.parseInt(views.get(i).getTag().toString());
            ret |= v;
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            FileChooserActivity.Params params = FileChooserActivity.Params.newSingleFileParams();
            params.initDir = dir;
            Intent intent = FileChooserActivity.create(getActivity(), params);
            startActivityForResult(intent, 1);
        }
        else if (v == btn2) {
            // FileChooserActivity.Params params = new FileChooserActivity.Params();
            // params.maxCount = 1;
            // params.chooserMode = FileChooserActivity.Params.CHOOSER_FOLDER;
            Intent intent = FileChooserActivity.create(getActivity(),
                FileChooserActivity.Params.newSingleFolderParams());
            startActivityForResult(intent, 2);
        }

        else if (v == btn3) {
            FileChooserActivity.Params params = getParams();
            Intent intent = FileChooserActivity.create(getActivity(), params);
            startActivityForResult(intent, 3);
        }
        super.onClick(v);
    }

    private FileChooserActivity.Params getParams() {
        FileChooserActivity.Params params = new FileChooserActivity.Params();
        params.chooserMode = modes[spn1.getSelectedItemPosition()];
        params.includeEmpty = chk1.isChecked();
        params.includeHidden = chk2.isChecked();
        params.selectDirectly = chk3.isChecked();
        if (!TextUtils.isEmpty(et3.getText())) {
            params.exts = et3.getText().toString().trim().split(";");
        }
        try {
            params.maxCount = Integer.parseInt(et1.getText().toString());
            params.maxSize = Integer.parseInt(et2.getText().toString());
        } catch (NumberFormatException e) {

        }
        params.sort = getSort();
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) {
                dir = data.getStringExtra(Intent.EXTRA_RETURN_RESULT);
                tv1.setText(dir);
            }
            else if (requestCode == 3) {
                tv1.setText(data.getSerializableExtra(Intent.EXTRA_RETURN_RESULT).toString());
            }
        }
        else {
            tv1.setText(null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
