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
package cn.ieclipse.af.demo.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.view.checkable.CheckableLinearLayout;

/**
 * File chooser item layout.
 *
 * @author Jamling
 */
public class FileItemLayout extends CheckableLinearLayout implements View.OnClickListener {

    public FileItemLayout(Context context) {
        super(context);
    }

    public FileItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FileItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FileItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private ImageView icon;
    private TextView name;
    private TextView time;
    private TextView size;
    private CheckBox checkBox;
    private View content;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileFilter fileFilter;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        icon = (ImageView) findViewById(android.R.id.icon);
        name = (TextView) findViewById(android.R.id.title);
        time = (TextView) findViewById(android.R.id.text1);
        size = (TextView) findViewById(android.R.id.text2);
        checkBox = (CheckBox) findViewById(android.R.id.checkbox);
        content = findViewById(android.R.id.content);
        // checkBox.setOnClickListener(this);
    }

    public void setData(File file) {
        name.setText(file.getName());
        time.setText(sdf.format(file.lastModified()));
        if (file.isDirectory()) {
            icon.setImageDrawable(getFolderDrawable(file));
            size.setText(null);
        }
        else {
            size.setText(FileUtils.formatFileSize(file.length()));
            icon.setImageDrawable(getFileDrawable(file));
            // icon.setImageResource(R.drawable.ic_file);
        }
        content.setTag(file);
    }

    public void setUp2Parent() {
        name.setText("..");
        time.setText("up to parent");
        size.setText(null);
        icon.setImageResource(R.drawable.ic_folder);
        setClickable(false);
        checkBox.setVisibility(View.INVISIBLE);
    }

    public void setFileFilter(FileFilter filter) {
        this.fileFilter = filter;
    }

    public void setCheckboxVisible(boolean visible) {
        checkBox.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setContentClickListener(View.OnClickListener listener) {
        content.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        if (checkBox == v) {
            if (checkBox.getVisibility() == View.VISIBLE) {
                setChecked(checkBox.isChecked());
                setActivated(isChecked());
            }
        }
        else if (content == v) {

        }
    }

    private Drawable getFolderDrawable(File f) {
        File[] fs = f.listFiles(fileFilter);
        Drawable d = null;
        if (fs == null || fs.length == 0) {
            // d = AppUtils.getDrawable(getContext(), R.drawable.ic_folder_empty);
            d = VectorDrawableCompat.create(getResources(), R.drawable.ic_folder_empty,
                getContext().getTheme());
        }
        else {
            // d = AppUtils.getDrawable(getContext(), R.drawable.ic_folder);
            d = VectorDrawableCompat.create(getResources(), R.drawable.ic_folder,
                getContext().getTheme());
        }
        d = AppUtils.tintDrawable(d, AppUtils.getColor(getContext(), R.color.colorPrimary));
        return d;
    }

    private Drawable getFileDrawable(File file) {
        String ext = FileUtils.getExtension(file.getName());
        VectorDrawableCompat d = VectorDrawableCompat.create(getResources(), R.drawable.ic_folder_empty,
            getContext().getTheme());
        d.setTint(AppUtils.getColor(getContext(), R.color.colorAccent));
        return d;
        // return AppUtils.tintDrawable(getContext(), R.drawable.ic_file, R.color.colorAccent);
        // return AppUtils.getDrawable(getContext(), R.drawable.ic_file);
    }
}
