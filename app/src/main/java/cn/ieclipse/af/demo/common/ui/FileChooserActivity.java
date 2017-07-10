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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.view.FileItemLayout;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.SDUtils;

/**
 * File chooser
 *
 * @author Jamling
 */
public class FileChooserActivity extends BaseActivity implements AbsListView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {

    private LinearLayout mProgressContainer;
    private FrameLayout mListContainer;
    private ListView mListView;
    private TextView mTvEmpty;
    private FileAdapter mAdapter;
    private Params mParams;

    private TextView mTvCheckedCount;
    private View mBtnOk;
    private int mChoiceMode = ListView.CHOICE_MODE_NONE;

    private Map<File, State> mStates = new HashMap<>();

    @Override
    protected int getContentLayout() {
        return android.R.layout.list_content;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleBar.setGravity(Gravity.LEFT);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        // mTitleTextView.setText(mParams.initDir);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mTvEmpty = (TextView) view.findViewById(android.R.id.empty);
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(mTvEmpty);
        mListView.setChoiceMode(mChoiceMode);

        mListView.setOnItemLongClickListener(this);
    }

    @Override
    protected void initBottomView() {
        super.initBottomView();
        getLayoutInflater().inflate(R.layout.common_bottom_file_chooser, mBottomBar, true);
        mTvCheckedCount = (TextView) mBottomBar.findViewById(android.R.id.text1);
        mBtnOk = mBottomBar.findViewById(android.R.id.button1);
        setOnClickListener(mBtnOk);
        mBottomBar.setBackgroundResource(R.color.bg_main);
    }

    @Override
    protected void initData() {
        super.initData();

        mAdapter = new FileAdapter();
        initFileAdapter(mAdapter);

        onCheckCountChanged(0);
        onFolderChanged(new File(mParams.initDir));
        mListView.setAdapter(mAdapter);
    }

    protected void initFileAdapter(FileAdapter adapter) {
        FileAdapter.DEFAULT_COMPARATOR.setSort(mParams.sort);
        FileAdapter.DEFAULT_FILTER.setParams(mParams);
        FileAdapter.FOLDER_FILTER.setParams(mParams);
        if (mParams.chooserMode == Params.CHOOSER_FOLDER) {
            adapter.setFileFilter(FileAdapter.FOLDER_FILTER);
        }
        else {
            if (mParams.exts != null && mParams.exts.length > 0) {
                adapter.setFileFilter(new ExtFilter(mParams.exts));
            }
            if (mParams.chooserMode == Params.CHOOSER_FILE) {
                adapter.setCheckFilter(new FileAdapter.CheckFilter() {

                    @Override
                    public boolean isCheckable(File file) {
                        return file.isFile();
                    }
                });
                return;
            }
            else if (mParams.chooserMode == Params.CHOOSER_NONE) {
                adapter.setCheckFilter(new FileAdapter.CheckFilter() {

                    @Override
                    public boolean isCheckable(File file) {
                        return false;
                    }
                });
                return;
            }
        }
        adapter.setContentClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = (File) v.getTag();
                if (file.isDirectory()) {
                    onFolderChanged(file);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mListView.getCheckedItemCount() > 0) {
            mListView.clearChoices();
            mAdapter.clearChoices();
            mAdapter.notifyDataSetChanged();
            onCheckCountChanged(mListView.getCheckedItemCount());
            return;
        }
        if (mParams.finishOnSDRoot && mAdapter.getCurrentDir().equals(SDUtils.getRootDirectory())) {
            super.onBackPressed();
            return;
        }
        if (mAdapter.getCurrentDir() != null) {
            File parent = mAdapter.getCurrentDir().getParentFile();
            if (parent != null) {
                onFolderChanged(parent);
                return;
            }
        }
        super.onBackPressed();
    }

    protected void openFile(File f) {
        String ext = FileUtils.getExtensionFromUrl(f.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(f));
        intent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, null));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            onFolderChanged(mAdapter.getCurrentDir().getParentFile());
            return;
        }
        if (mParams.chooserMode == Params.CHOOSER_FILE) {
            File f = mAdapter.getItem(position);
            if (f.isDirectory()) {
                mListView.setItemChecked(position, false);
                onFolderChanged(f);
                return;
            }
        }
        else if (mParams.chooserMode == Params.CHOOSER_NONE) {
            File f = mAdapter.getItem(position);
            if (f.isDirectory()) {
                mListView.setItemChecked(position, false);
                onFolderChanged(f);
                return;
            }
            else {
                openFile(f);
                return;
            }
        }

        if (mChoiceMode == ListView.CHOICE_MODE_SINGLE) {
            mAdapter.setItemChecked(position, true);
            // return result directly
            if (Params.CHOOSER_FILE == mParams.chooserMode && mParams.selectDirectly) {
                select(mAdapter.getCheckedItem());
            }
        }
        else if (mChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            mAdapter.setItemChecked(position, mListView.isItemChecked(position));
        }
        int count = mAdapter.getCheckedItems().size();//mListView.getCheckedItemCount();
        if (mChoiceMode != ListView.CHOICE_MODE_MULTIPLE) {
            count = Math.min(count, 1);
        }
        onCheckCountChanged(count);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mParams.chooserMode == Params.CHOOSER_FOLDER || mParams.chooserMode == Params.CHOOSER_ALL) {
            onFolderChanged(mAdapter.getItem(position));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mBtnOk == v) {
            if (mChoiceMode == ListView.CHOICE_MODE_SINGLE) {
                select(mAdapter.getCheckedItem());
            }
            else if (mChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
                select(mAdapter.getCheckedItems());
            }
        }
        super.onClick(v);
    }

    protected void onFolderChanged(File dir) {
        if (dir != null) {
            if (mAdapter.getCurrentDir() != null) {
                mStates.put(mAdapter.getCurrentDir(), State.get(mListView));
            }
            mListView.clearChoices();
            mAdapter.setCurrentDir(dir);
            mAdapter.notifyDataSetChanged();
            mTitleTextView.setText(dir.getAbsolutePath());

            final State state = mStates.get(dir);
            if (state != null) {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        state.set(mListView);
                    }
                }, 100);
            }
        }
    }

    protected void onCheckCountChanged(int count) {
        if (count > 0) {
            mTvCheckedCount.setText(getString(R.string.common_checked_num, String.valueOf(count)));
            mBtnOk.setEnabled(true);
            mBottomBar.setVisibility(View.VISIBLE);
        }
        else {
            mTvCheckedCount.setText(null);
            mBtnOk.setEnabled(false);
            mBottomBar.setVisibility(View.GONE);
        }
    }

    private void select(File f) {
        Intent data = new Intent();
        data.putExtra(Intent.EXTRA_RETURN_RESULT, f == null ? null : f.getAbsolutePath());
        setResult(RESULT_OK, data);
        finish();
    }

    private void select(List<File> files) {
        Intent data = new Intent();
        data.putExtra(Intent.EXTRA_RETURN_RESULT, (Serializable) files);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        mParams = (Params) bundle.getSerializable(EXTRA_DATA);
    }

    @Override
    protected void initInitData() {
        super.initInitData();
        if (mParams == null) {
            mParams = new Params();
            setInitDir();
        }
        else {
            if (TextUtils.isEmpty(mParams.initDir)) {
                setInitDir();
            }
            else {
                File f = new File(mParams.initDir);
                if (!f.exists()) {
                    setInitDir();
                }
                else if (f.isFile()) {
                    mParams.initDir = f.getParent();
                    mParams.initFile = f.getName();
                }
            }
        }

        if (mParams.maxCount > 1) {
            mChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
        }
        else if (mParams.maxCount > 0) {
            mChoiceMode = ListView.CHOICE_MODE_SINGLE;
        }
    }

    private void setInitDir() {
        if (SDUtils.isAvailable()) {
            mParams.initDir = SDUtils.getRootDirectory().getAbsolutePath();
        }
        else {
            mParams.initDir = Environment.getRootDirectory().getAbsolutePath();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_DATA, mParams);
        super.onSaveInstanceState(outState);
    }

    public static Intent create(Context context, Params params) {
        Intent intent = new Intent(context, FileChooserActivity.class);
        intent.putExtra(EXTRA_DATA, params == null ? new Params() : params);
        return intent;
    }

    public static class Params implements java.io.Serializable {
        public static final int CHOOSER_ALL = -1;
        public static final int CHOOSER_FILE = 0;
        public static final int CHOOSER_FOLDER = 1;
        public static final int CHOOSER_NONE = 2;

        public static final int SORT_FOLDER_FIRST = 0x80;
        public static final int SORT_NAME_ASC = 0x02;
        public static final int SORT_NAME_DESC = 0x03;
        public static final int SORT_DATE_ASC = 0x08;
        public static final int SORT_DATE_DESC = 0x0C;
        public static final int SORT_SIZE_ASC = 0x20;
        public static final int SORT_SIZE_DESC = 0x30;

        public String initDir;
        public String initFile;
        public int chooserMode = CHOOSER_FILE;
        public int maxCount = 1;
        public int maxSize = -1;
        public String[] exts;
        public boolean includeEmpty = true;
        public boolean includeHidden = false;
        /**
         * In {@link #CHOOSER_FILE} and {@link #maxCount} == 1 (Single Choice), whether select directly (default is
         * true)
         */
        public boolean selectDirectly = true;
        /**
         * Finish when current dir is SD root.
         */
        public boolean finishOnSDRoot = true;
        public int sort = SORT_FOLDER_FIRST | SORT_NAME_ASC;

        public static Params newSingleFileParams() {
            Params params = new Params();
            params.includeEmpty = false;
            return params;
        }

        public static Params newSingleFolderParams() {
            Params params = new Params();
            params.chooserMode = CHOOSER_FOLDER;
            return params;
        }
    }

    public static class ExtFilter implements FileFilter {

        private String[] exts;

        public ExtFilter(String[] exts) {
            this.exts = exts;
        }

        @Override
        public boolean accept(File pathname) {
            return pathname.canRead() || matchExt(pathname);
        }

        private boolean matchExt(File pathname) {
            String ext = FileUtils.getExtension(pathname.getName()).toLowerCase();
            for (String tmp : exts) {
                if (ext.equals(tmp)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class FileAdapter extends AfBaseAdapter<File> {

        @Override
        public int getLayout() {
            return R.layout.common_list_item_file;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            if (position > 0) {
                File file = getItem(position);
                FileItemLayout layout = (FileItemLayout) convertView;
                layout.setFileFilter(mFileFilter);
                layout.setData(file);
                if (mCheckFilter != null) {
                    boolean checkable = mCheckFilter.isCheckable(file);
                    layout.setCheckboxVisible(checkable);
                }
                if (mContentClick != null) {
                    layout.setContentClickListener(mContentClick);
                }
            }
            else {
                FileItemLayout layout = (FileItemLayout) convertView;
                layout.setUp2Parent();
            }
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public File getItem(int position) {
            return super.getItem(position - 1);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        public static final DefaultFileFilter DEFAULT_FILTER = new DefaultFileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.canRead() && include(pathname);
            }
        };

        public static final DefaultFileFilter FOLDER_FILTER = new DefaultFileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.canRead() && pathname.isDirectory() && include(pathname);
            }
        };

        public static final FileComparator DEFAULT_COMPARATOR = new FileComparator();

        public interface CheckFilter {
            boolean isCheckable(File file);
        }

        protected FileFilter mFileFilter = DEFAULT_FILTER;
        protected Comparator<File> mComparator = DEFAULT_COMPARATOR;
        protected File mCurrentDir;
        protected CheckFilter mCheckFilter = null;
        protected View.OnClickListener mContentClick;

        public void setFileFilter(FileFilter filter) {
            if (filter != null) {
                this.mFileFilter = filter;
            }
        }

        public void setComparator(Comparator<File> comparator) {
            if (comparator != null) {
                this.mComparator = comparator;
            }
        }

        public void setCheckFilter(CheckFilter filter) {
            this.mCheckFilter = filter;
        }

        public void setContentClick(View.OnClickListener clickListener) {
            this.mContentClick = clickListener;
        }

        public void setCurrentDir(File dir) {
            this.mCurrentDir = dir;
            if (this.mCurrentDir == null || !this.mCurrentDir.exists()) {
                setDataList(null);
            }
            else {
                File[] subs = dir.listFiles(mFileFilter);
                if (subs == null || subs.length == 0) {
                    setDataList(null);
                }
                else {
                    setDataList(Arrays.asList(subs));
                    Collections.sort(getDataList(), mComparator);
                }
            }
        }

        public File getCurrentDir() {
            return mCurrentDir;
        }
    }

    public static abstract class DefaultFileFilter implements FileFilter {
        private Params params;

        public void setParams(Params params) {
            this.params = params;
        }

        public boolean include(File pathname) {
            boolean flag = true;
            if (params != null) {
                if (!params.includeHidden) {
                    flag = !pathname.isHidden();
                }
                if (!params.includeEmpty && pathname.isDirectory()) {
                    File[] sub = pathname.listFiles();
                    flag &= (sub != null && sub.length > 0);
                }
            }
            return flag;
        }
    }

    public static class FileComparator implements Comparator<File> {
        private int sort = Params.SORT_FOLDER_FIRST | Params.SORT_NAME_ASC;

        public void setSort(int sort) {
            this.sort = sort;
        }

        @Override
        public int compare(File lhs, File rhs) {
            int ret = 0;
            if ((sort & Params.SORT_FOLDER_FIRST) != 0) {
                ret = compareFolder(lhs, rhs);
                if (ret != 0) {
                    return ret;
                }
            }
            if ((sort & Params.SORT_NAME_DESC) == Params.SORT_NAME_ASC) {
                ret = compareName(lhs, rhs);
            }
            else if ((sort & Params.SORT_NAME_DESC) == Params.SORT_NAME_DESC) {
                ret = compareName(rhs, lhs);
            }
            if (ret != 0) {
                return ret;
            }

            if ((sort & Params.SORT_DATE_DESC) == Params.SORT_DATE_ASC) {
                ret = compareDate(lhs, rhs);
            }
            else if ((sort & Params.SORT_DATE_DESC) == Params.SORT_DATE_DESC) {
                ret = compareDate(rhs, lhs);
            }
            if (ret != 0) {
                return ret;
            }

            if ((sort & Params.SORT_SIZE_DESC) == Params.SORT_SIZE_ASC) {
                ret = compareLength(lhs, rhs);
            }
            else if ((sort & Params.SORT_SIZE_DESC) == Params.SORT_SIZE_DESC) {
                ret = compareLength(rhs, lhs);
            }
            if (ret != 0) {
                return ret;
            }

            return ret;
        }

        private int compareFolder(File lhs, File rhs) {
            if (lhs.isDirectory()) {
                if (rhs.isDirectory()) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
            else if (lhs.isFile()) {
                if (rhs.isDirectory()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
            return 0;
        }

        private int compareName(File lhs, File rhs) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }

        private int compareDate(File lhs, File rhs) {
            long l1 = lhs.lastModified();
            long l2 = rhs.lastModified();
            return compareLong(l1, l2);
        }

        private int compareLength(File lhs, File rhs) {
            long l1 = lhs.length();
            long l2 = rhs.length();
            return compareLong(l1, l2);
        }

        private int compareLong(long l1, long l2) {
            long t = l1 - l2;
            if (t > 0) {
                return 1;
            }
            else if (t == 0) {
                return 0;
            }
            else {
                return -1;
            }
        }
    }

    private static class State {
        private List<Integer> checked;
        private int location;

        public static State get(ListView listView) {
            State state = new State();
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            if (checked != null) {
                state.checked = new ArrayList<>(checked.size());
                for (int i = 0; i < checked.size(); i++) {
                    int k = checked.keyAt(i);
                    boolean v = checked.valueAt(i);
                    if (v) {
                        state.checked.add(k);
                    }
                }
            }
            state.location = listView.getFirstVisiblePosition();
            return state;
        }

        public void set(ListView listView) {
            listView.setSelection(location);
            if (checked != null) {
                for (int i = 0; i < checked.size(); i++) {
                    listView.setItemChecked(checked.get(i), true);
                }
            }
        }

        @Override
        public String toString() {
            return "State(position=" + location + ",checked=" + checked + ")";
        }
    }
}
