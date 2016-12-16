/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.demo.sample.legacy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.recycle.RefreshEmptyView;

/**
 * 自定义emptyview
 *
 * @author wangjian
 * @date 2016/9/27.
 * @deprecated
 */
public class LegacyMyEmptyView extends RefreshEmptyView {

    public LegacyMyEmptyView(Context context) {
        super(context);
    }

    public LegacyMyEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getEmptyLayout(Context context) {
        View v = View.inflate(context, R.layout.legacy_refresh_empty_view, null);
        // 必须实现对以下3个view的赋值，否则EmptyView不起作用
        mLoadingLayout = v.findViewById(R.id.layout_loading);
        mNetworkErrorLayout = v.findViewById(R.id.layout_network_error);
        mDataEmptyLayout = v.findViewById(R.id.layout_empty_data);
        return v;
    }
}
