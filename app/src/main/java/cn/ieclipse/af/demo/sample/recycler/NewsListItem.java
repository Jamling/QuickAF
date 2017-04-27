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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.ieclipse.af.demo.R;

/**
 * Description
 *
 * @author Jamling
 */
public class NewsListItem extends LinearLayout {
    public NewsListItem(Context context) {
        super(context);
    }

    public NewsListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView icon;
    public TextView tvTitle;
    public TextView tvTime;
    public TextView tvDesc;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        icon = (ImageView) findViewById(R.id.icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
    }

    private NewsController.NewsInfo info;

    public void setInfo(NewsController.NewsInfo info) {
        this.info = info;
        tvTitle.setText(info.title);
        tvTime.setText(info.ctime);
        tvDesc.setText(info.description);
        ImageLoader.getInstance().displayImage(info.picUrl, icon);
    }
}