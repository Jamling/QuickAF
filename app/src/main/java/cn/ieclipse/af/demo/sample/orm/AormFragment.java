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
package cn.ieclipse.af.demo.sample.orm;

import android.view.View;
import android.widget.Button;

import java.io.File;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.SDUtils;
import cn.ieclipse.aorm.Aorm;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016/2/15.
 */
public class AormFragment extends BaseFragment {
    private Button btn;
    private Button btnClear;
    @Override
    protected int getContentLayout() {
        return R.layout.sample_fragment_aorm;
    }

    @Override
    public CharSequence getTitle() {
        return "Aorm";
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        btn = (Button) view.findViewById(R.id.btn1);
        btnClear = (Button) view.findViewById(R.id.btn2);
        setOnClickListener(btn, btnClear);

        // enable debug log
        Aorm.enableDebug(true);
    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
            getActivity().startActivity(StudentListActivity.create(getActivity()));
        }
        else if (v == btnClear) {
            File f = new File(SDUtils.getInternal(getActivity()).getParentFile(), "databases/example.db");
            if(f.delete()){
                DialogUtils.showToast(getActivity(), "The old data is cleared successfully!");
            }
            else {
                DialogUtils.showToast(getActivity(), "Clear Failed!");
            }
        }
        super.onClick(v);
    }
}
