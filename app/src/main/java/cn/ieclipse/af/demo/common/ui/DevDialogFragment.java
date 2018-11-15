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
package cn.ieclipse.af.demo.common.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.File;

import androidx.fragment.app.FragmentManager;
import cn.ieclipse.af.app.AfDialogFragment;
import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.SDUtils;
import cn.ieclipse.af.volley.Controller;

/**
 * Description
 *
 * @author Jamling
 */

public class DevDialogFragment extends AfDialogFragment implements CompoundButton.OnCheckedChangeListener,
    AdapterView.OnItemClickListener {

    private AutoCompleteTextView mEtUrl;
    private View mBtnDown;
    private CheckBox mChkDebug;
    private CheckBox mChkAfDebug;
    private View mBtnDump;

    private static long time = System.currentTimeMillis();
    private static int count = 0;
    private static int MAX_COUNT = 3;
    private static int MAX_INTERVAL = 300;

    public static void detect(FragmentManager fm) {
        long t2 = System.currentTimeMillis();
        if (t2 - time > MAX_INTERVAL) {
            count = 0;
        }
        else {
            count++;
        }
        time = t2;
        if (count > MAX_COUNT) {
            count = 0;
            show(fm);
        }
    }

    public static void show(FragmentManager fm) {
        DevDialogFragment dialog = new DevDialogFragment();
        dialog.setStyle(STYLE_NO_TITLE, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setAlert(true);
        dialog.show(fm, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.common_dev_dialog;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mEtUrl = (AutoCompleteTextView) view.findViewById(R.id.et_text);
        mEtUrl.setText(URLConst.BASE);
        mEtUrl.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, getUrls()));
        mEtUrl.setOnItemClickListener(this);

        mBtnDown = view.findViewById(R.id.btn_submit);
        mBtnDump = view.findViewById(android.R.id.button1);

        mChkAfDebug = (CheckBox) view.findViewById(R.id.chk1);
        mChkAfDebug.setChecked(Controller.DEBUG);
        mChkAfDebug.setOnCheckedChangeListener(this);

        mChkDebug = (CheckBox) view.findViewById(R.id.chk2);
        mChkDebug.setChecked(AppConfig.isDebug());
        mChkDebug.setOnCheckedChangeListener(this);

        setOnClickListener(mBtnDown, mBtnDump);
    }

    protected String[] getUrls() {
        return new String[]{URLConst.BASE_DEBUG, URLConst.BASE_RELEASE, "http://"};
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnDown) {
            mEtUrl.showDropDown();
        }
        else if (v == mBtnDump) {
            dump();
        }
        super.onClick(v);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        URLConst.BASE = mEtUrl.getText().toString();
    }

    protected void onCreateAlertDialog(AlertDialog.Builder builder) {
        builder.setPositiveButton(android.R.string.ok, this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mChkAfDebug) {
            Controller.DEBUG = isChecked;
        }
        else if (buttonView == mChkDebug) {
            AppConfig.debug = isChecked;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void dump() {
        String type = getActivity().getPackageName();
        try {
            File src = FileUtils.getInternal(getActivity()).getParentFile();
            File dest = SDUtils.getRootDirectory();
            FileUtils.copyDirectoryToDirectory(src, dest, null);
            DialogUtils.showToast(getActivity(), "已复制到" + dest.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showToast(getActivity(), e.toString());
        }
    }
}
