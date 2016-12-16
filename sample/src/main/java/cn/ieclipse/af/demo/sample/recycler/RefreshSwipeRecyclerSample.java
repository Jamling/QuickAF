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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import cn.ieclipse.af.adapter.delegate.AdapterDelegate;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.recycle.SwipeMenuRecyclerView;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshSwipeRecyclerSample extends RefreshRecyclerSample {
    SwipeMenuRecyclerView swipeListView;
    private int[] swipeDirs = new int[]{SwipeMenuRecyclerView.DIRECTION_LEFT, SwipeMenuRecyclerView.DIRECTION_RIGHT};

    @Override
    public CharSequence getTitle() {
        return "RefreshSwipeRecycler(New)";
    }

    @Override
    protected int getContentLayout() {
        return R.layout.sample_refresh_swipe;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        swipeListView = (SwipeMenuRecyclerView) refreshLayout.findViewById(R.id.swipe_rv);
        swipeListView.setSwipeDirection(swipeDirs[0]);
    }

    @Override
    protected void registerDelegate() {
        adapter.registerDelegate(new NewDelegate());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spn2) {
            swipeListView.setSwipeDirection(swipeDirs[position]);
        }
        else {
            super.onItemSelected(parent, view, position, id);
        }
    }

    private class NewDelegate extends AdapterDelegate<NewsController.NewsInfo> {

        @Override
        public int getLayout() {
            return R.layout.sample_list_item_news_swipe;
        }

        @Override
        public void onUpdateView(RecyclerView.ViewHolder holder, NewsController.NewsInfo info, int position) {
            SwipeNewsHolder vh = (SwipeNewsHolder) holder;
            vh.setInfo(info);
        }

        @Override
        public Class<? extends RecyclerView.ViewHolder> getViewHolderClass() {
            return SwipeNewsHolder.class;
        }
    }

    private static class SwipeNewsHolder extends NewsHolder {
        View btOpen;
        View btDelete;

        public SwipeNewsHolder(View view) {
            super(view);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            btOpen.setOnClickListener(this);
            btDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == btOpen) {
                super.onClick(itemView);
            }
            else if (btDelete == v) {
                getAdapter().deleteItem(getLayoutPosition());
            }
            else {
                super.onClick(v);
            }
        }
    }
}
