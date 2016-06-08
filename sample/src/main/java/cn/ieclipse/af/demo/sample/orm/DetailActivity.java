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

/**
 * 
 */
package cn.ieclipse.af.demo.sample.orm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.aorm.Session;


/**
 * @author Jamling
 *         
 */
public abstract class DetailActivity extends BaseActivity {
    
    protected long id;
    protected EditText etIndex;
    protected Session session;
    
    private ImageView mEdit;
    private ImageView mSave;
    private ImageView mDel;
    
    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        id = bundle.getLong(EXTRA_ID);
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mSave = new ImageView(this);
        mEdit = new ImageView(this);
        mDel = new ImageView(this);
        mSave.setImageResource(android.R.drawable.ic_menu_save);
        mEdit.setImageResource(android.R.drawable.ic_menu_save);
        mDel.setImageResource(android.R.drawable.ic_menu_delete);
        if (id == 0) {
            mTitleBar.addRight(mSave);
        }
        else {
            mTitleBar.addRight(mEdit);
            mTitleBar.addRight(mDel);
        }
        setOnClickListener(mSave, mEdit, mDel);
    }
    
    @Override
    public void onClick(View v) {
        if (v == mSave) {
            if (save()) {
                mSave.setVisibility(View.GONE);
                mTitleBar.addRight(mEdit);
                mTitleBar.addRight(mDel);
                etIndex.setText(String.valueOf(id));
                DialogUtils.showToast(this, "Save successfully!");
            }
            return;
        }
        else if (v == mEdit) {
            if (edit()) {
                DialogUtils.showToast(this, "Update successfully!");
            }
            return;
        }
        else if (v == mDel) {
            if (delete()) {
                DialogUtils.showToast(this, "Delete successfully!");
                id = 0;
                finish();
            }
            return;
        }
        super.onClick(v);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(EXTRA_ID, id);
        super.onSaveInstanceState(outState);
    }
    
    protected abstract boolean delete();
    
    protected abstract boolean edit();
    
    protected abstract boolean save();
}
