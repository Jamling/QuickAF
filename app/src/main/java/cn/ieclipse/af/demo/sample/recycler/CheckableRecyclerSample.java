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
package cn.ieclipse.af.demo.sample.recycler;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfRecyclerChoiceAdapter;
import cn.ieclipse.af.adapter.delegate.AdapterDelegate;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.DialogUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class CheckableRecyclerSample extends RefreshRecyclerSample {

    private int[] choices
        = new int[]{AfRecyclerChoiceAdapter.CHOICE_MODE_NONE, AfRecyclerChoiceAdapter.CHOICE_MODE_SINGLE,
        AfRecyclerChoiceAdapter.CHOICE_MODE_MULTIPLE};

    private AfRecyclerChoiceAdapter adapter2;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_refresh_recycler_checkable;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        adapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AfRecyclerAdapter adapter1, View view, int position) {
                if (adapter2.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
                    DialogUtils.showToast(getContext(), "Please select an choice mode");
                }
                adapter2.toggleItemChecked(position);
                if (adapter2.getCheckedItemCount() > 0) {
                    showMode();
                }
            }
        });
    }

    @Override
    protected void registerDelegate() {
        adapter2 = new AfRecyclerChoiceAdapter();
        adapter = adapter2;
        adapter.registerDelegate(new CheckableNewsDelegate());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spn2) {
            adapter2.setChoiceMode(choices[position]);
        }
        else {
            super.onItemSelected(parent, view, position, id);
        }
    }

    private static class CheckableNewsDelegate extends AdapterDelegate<NewsController.NewsInfo> {

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_multiple_choice;
        }

        @Override
        public void onUpdateView(RecyclerView.ViewHolder holder, NewsController.NewsInfo info, int position) {
            CheckedTextView tv = (CheckedTextView) holder.itemView;
            tv.setText(info.title);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
            RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent);
            return holder;
        }
    }

    private void selectAll() {
        for (int i = 0; i < adapter2.getItemCount(); i++) {
            adapter2.setItemChecked(i, true);
        }
    }

    private void selectNone() {
        for (int i = 0; i < adapter2.getItemCount(); i++) {
            adapter2.setItemChecked(i, false);
        }
    }

    private void selectInvert() {
        for (int i = 0; i < adapter2.getItemCount(); i++) {
            adapter2.toggleItemChecked(i);
        }
    }

    private void showResult() {
        int c = adapter2.getCheckedItemCount();
        Integer[] a = adapter2.getCheckedPositions();
        List list = adapter2.getCheckedItems();
        DialogUtils.showToast(getActivity(),
            String.format("check: %d pos: %s, data: %s", c, Arrays.toString(a), list.toString()));
    }

    private void hideMode() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    private void showMode() {
        if (mActionMode == null) {
            mActionMode = getActivity().startActionMode(mActionCallback);
        }
    }

    private ActionMode mActionMode;

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_all) {
                selectAll();
                return true;
            }
            else if (item.getItemId() == R.id.action_none) {
                selectNone();
                return true;
            }
            else if (item.getItemId() == R.id.action_invert) {
                selectInvert();
                return true;
            }
            else if (item.getItemId() == R.id.action_done) {
                showResult();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            adapter2.clearChoices();
            adapter2.notifyDataSetChanged();
        }
    };
}
