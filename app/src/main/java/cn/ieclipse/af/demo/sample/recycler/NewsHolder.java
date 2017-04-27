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

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.H5Activity;
import cn.ieclipse.af.util.DialogUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class NewsHolder extends AfViewHolder {

    public ImageView icon;
    public TextView tvTitle;
    public TextView tvTime;
    public TextView tvDesc;

    public NewsHolder(View view) {
        super(view);
        icon = (ImageView) view.findViewById(R.id.icon);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
    }

    private NewsController.NewsInfo info;

    public void setInfo(NewsController.NewsInfo info) {
        this.info = info;
        tvTitle.setText(info.title);
        tvTime.setText(info.ctime);
        tvDesc.setText(info.description);
        ImageLoader.getInstance().displayImage(info.picUrl, icon);
    }

    @Override
    public void onClick(View v) {
        DialogUtils.showToast(getContext(), String
            .format("ViewHolder#onClick() adapter position = %d, layout " + "position = %d", getAdapterPosition(),
                getLayoutPosition()));
        getContext().startActivity(H5Activity.create(getContext(), info.url, info.title));
    }
}
