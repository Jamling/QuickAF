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
package cn.ieclipse.af.demo.sample.cview;

import android.graphics.Rect;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.HotImageView;
import cn.ieclipse.af.view.RoundImageView;

/**
 * Description
 *
 * @author Jamling
 */
public class CustomImageViewSample extends SampleBaseFragment {

    private RoundImageView iv1;
    private RoundImageView iv2;
    private HotImageView iv3;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_image_view;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        iv1 = (RoundImageView) view.findViewById(R.id.iv1);
        //iv2 = (RoundImageView) view.findViewById(R.id.iv2);
        //iv1.setImageResource(R.color.black_alpha_50);
        //iv2.setImageResource(R.mipmap.logo);
        iv3 = (HotImageView) view.findViewById(R.id.iv3);
        iv3.addRegion(3, new Rect(41, 41, 117+41, 44+41), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showToast(v.getContext(), "Quick Hit!");
            }
        });
    
        iv3.addRegion(3, new Rect(38, 104, 44+38, 44+104), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showToast(v.getContext(), "A Hit!");
            }
        });
    
        iv3.addRegion(3, new Rect(118, 104, 44+118, 44+104), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showToast(v.getContext(), "F Hit!");
            }
        });
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("CustomImageViewSample");
    }
}
