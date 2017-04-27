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
package cn.ieclipse.af.demo.common;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshRecyclerHelper;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class AppRefreshRecyclerHelper extends RefreshRecyclerHelper {

    public AppRefreshRecyclerHelper(final RefreshLayout refreshLayout) {
        super(refreshLayout);
        // use theme listDivider as default divider
        // setDividerColor(AppUtils.getColor(getContext(), R.color.divider));
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // TODO the RefreshLayout has set the default RetryListener
//        refreshLayout.setEmptyRetryListener(new EmptyView.RetryListener() {
//                @Override
//                public void onErrorClick() {
//                    refreshLayout.onRefresh();
//                }
//
//                @Override
//                public void onDataEmptyClick() {
//                    refreshLayout.onRefresh();
//                }
//            });
    }

    @Override
    protected boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public void onLoadFailure(RestError error) {
        refreshLayout.setEmptyError(VolleyUtils.getError(getContext(), error));
        super.onLoadFailure(error);
    }
}
