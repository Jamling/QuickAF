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

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.ieclipse.af.adapter.AfPagerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.AutoPlayView;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2015/12/24.
 */
public class AutoPlayViewSample extends SampleBaseFragment{
    boolean loop = true;
    TextView loopTv;
    TextView rightView;

    AutoPlayView hAutoPlayView;
    AutoPlayView vAutoPlayView;
    AutoPlayView aAutoPlayView;

    LinearLayout linearLayout;

    LoopHorizontalAdapter hLoopAdapter;
    PlainHorizontalAdapter hPlainAdapter;
    LoopVerticalAdapter vLoopAdapter;
    PlainVerticalAdapter vPlainAdapter;
    AnotherLoopAdapter aLoopAdapter;

    List<Integer> hlist = Arrays.asList(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
        android.R.color.holo_blue_dark);
    List<String> vlist = Arrays.asList("红", "绿", "蓝");

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_auto_play;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("AutoPlayView");
        rightView = createRightText("停止", true);
        loopTv = createRightText("循环关", false);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        hAutoPlayView = (AutoPlayView) view.findViewById(R.id.auto_play);
        vAutoPlayView = (AutoPlayView) view.findViewById(R.id.auto_play2);

        linearLayout = (LinearLayout) view.findViewById(R.id.indicator_layout);
        TextView tv = (TextView) view.findViewById(R.id.indicator_tv);

        // 设置indicator需要的布局
        // hAutoPlayView.setIndicatorLayout(linearLayout);
        // indicator item
        hAutoPlayView.setIndicatorItemPadding(AppUtils.dp2px(getActivity(), 6));
        //hAutoPlayView.setIndicatorItemLayout(android.R.layout.simple_list_item_single_choice);
        hAutoPlayView.setIndicatorItemLayout(R.layout.common_pager_indicator_item);

        // 设置indicator文本显示
        // hAutoPlayView.setIndicatorTextView(tv);

        hLoopAdapter = new LoopHorizontalAdapter();
        hAutoPlayView.setAdapter(hLoopAdapter);

        aAutoPlayView = (AutoPlayView) view.findViewById(R.id.auto_play3);
        aLoopAdapter = new AnotherLoopAdapter();
        aLoopAdapter.setDataList(hlist);
        aAutoPlayView.setAdapter(aLoopAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        hLoopAdapter.setDataList(hlist);
        hLoopAdapter.notifyDataSetChanged(true);
        hAutoPlayView.initIndicatorLayout();
        hAutoPlayView.setInterval(3000);
        hAutoPlayView.getViewPager().setCurrentItem(1);
        // hAutoPlayView.start();


        vLoopAdapter = new LoopVerticalAdapter();
        vLoopAdapter.setDataList(vlist);
        vAutoPlayView.setAdapter(vLoopAdapter);
        vAutoPlayView.setInterval(3000);
        //vAutoPlayView.start();

        hPlainAdapter = new PlainHorizontalAdapter();
        hPlainAdapter.setDataList(hlist);
        vPlainAdapter = new PlainVerticalAdapter();
        vPlainAdapter.setDataList(vlist);

        aAutoPlayView.getViewPager().setCurrentItem(1000, false);
        aAutoPlayView.getViewPager().setOffscreenPageLimit(2);
        aAutoPlayView.getViewPager().setPageMargin(AppUtils.dp2px(getActivity(), 10));
        aAutoPlayView.getViewPager().addOnPageChangeListener(anotherPageListener);
        aAutoPlayView.start();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chk1 == buttonView) {
            hAutoPlayView.setAdapter(isChecked ? hLoopAdapter : hPlainAdapter);
        }
        else if (chk2 == buttonView) {
            vAutoPlayView.setAdapter(isChecked ? vLoopAdapter : vPlainAdapter);
        }
        else if (chk4 == buttonView) {
            hAutoPlayView.setAutoStart(isChecked);
            if (isChecked){
                hAutoPlayView.start();
            }
        }
        else if (chk5 == buttonView) {
            vAutoPlayView.setAutoStart(isChecked);
            if (isChecked){
                vAutoPlayView.start();
            }
        }
        else if (chk6 == buttonView) {
            aAutoPlayView.setAutoStart(isChecked);
            if (isChecked){
                aAutoPlayView.start();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == rightView) {
            hAutoPlayView.stop();
            vAutoPlayView.stop();
            aAutoPlayView.stop();
        }
        else if (v == loopTv) {
            loop = !loop;
            loopTv.setText(loop ? "循环关" : "循环开");
            hAutoPlayView.setLoop(loop);
            vAutoPlayView.setLoop(loop);
        }
        super.onClick(v);
    }

    private class AnotherLoopAdapter extends AfPagerAdapter<Integer> implements View.OnClickListener{

        @Override
        public int getLayout() {
            return R.layout.common_pager_item_image;
        }

        @Override
        public int getCount() {
            // 实现无限循环
            return Integer.MAX_VALUE;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            int pos = position % getRealCount();
            convertView.setBackgroundResource(getItem(pos));
            convertView.setTag(Integer.valueOf(pos));
            convertView.setOnClickListener(this);
        }

        public int getRealCount() {
            return super.getCount();
        }

        @Override
        public void onClick(View v) {
            DialogUtils.showToast(v.getContext(), v.getTag().toString());
        }
    }

    private ViewPager.SimpleOnPageChangeListener anotherPageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            AnotherLoopAdapter adapter = (AnotherLoopAdapter) aAutoPlayView.getViewPager().getAdapter();
            int currentP = position % adapter.getRealCount();
            // 更新小点点颜色
            //changePointView2(oldpositon, currentP);
            aAutoPlayView.initIndicatorLayout();
        }
    };

    private class LoopHorizontalAdapter extends AutoPlayView.LoopPagerAdapter<Integer> implements View.OnClickListener {

        @Override
        public int getLayout() {
            return R.layout.common_pager_item_image;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            convertView.setBackgroundResource(getItem(position));
            convertView.setTag(Integer.valueOf(position));
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DialogUtils.showToast(v.getContext(), v.getTag().toString());
        }
    }

    private class PlainHorizontalAdapter extends AfPagerAdapter<Integer> implements View.OnClickListener {

        @Override
        public int getLayout() {
            return R.layout.common_pager_item_image;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            convertView.setBackgroundResource(getItem(position));
            convertView.setTag(Integer.valueOf(position));
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DialogUtils.showToast(v.getContext(), v.getTag().toString());
        }
    }

    private class PlainVerticalAdapter extends AfPagerAdapter<String> implements View.OnClickListener {
        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(getItem(position));
            tv.setTag(getItem(position));
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DialogUtils.showToast(v.getContext(), v.getTag().toString());
        }
    }

    private class LoopVerticalAdapter extends AutoPlayView.LoopPagerAdapter<String> implements View.OnClickListener {

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(getItem(position));
            tv.setTag(getItem(position));
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DialogUtils.showToast(v.getContext(), v.getTag().toString());
        }
    }
}
