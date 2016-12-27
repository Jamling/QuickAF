/*
 * Copyright 2014-2016 QuickAF
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
package cn.ieclipse.af.view.recycle;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/4/6 .
 * @deprecated use {@link cn.ieclipse.af.view.recycle.GridSpaceDecoration} instead
 */
@Deprecated
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffsetV;
    private int mItemOffsetH;

    public ItemOffsetDecoration(int verticalSpacing, int horizontalSpacing) {
        this.mItemOffsetV = verticalSpacing;
        this.mItemOffsetH = horizontalSpacing;
    }

    public void setItemOffsetV(int mItemOffsetV) {
        this.mItemOffsetV = mItemOffsetV;
    }

    public void setItemOffsetH(int horizontalSpacing) {
        this.mItemOffsetH = horizontalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffsetH, mItemOffsetV, mItemOffsetH, mItemOffsetV);
    }
}
