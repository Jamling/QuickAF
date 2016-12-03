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
package cn.ieclipse.af.view.refresh;

import android.view.View;

import cn.ieclipse.af.view.VScrollView;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshVScrollDetector extends RefreshLayout.RefreshDetector<VScrollView> {

    private VScrollView.OnScrollChangeListener mOnScrollChangeListener = new VScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (getRefresh().isEnableLoadMore() && getView().isScroll2Bottom()) {
                getRefresh().loadMore();
            }
        }
    };

    @Override
    public void setEnabled(boolean enable) {
        if (enable) {
            getView().setOnScrollChangeListener(mOnScrollChangeListener);
        }
        else {
            getView().setOnScrollChangeListener((VScrollView.OnScrollChangeListener) null);
        }
    }
}
