/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.cview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfPagerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.AutoPlayView;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2015/12/24.
 */
public class AutoPlayViewActivity extends BaseActivity {
    TextView rightView;
    AutoPlayView autoPlayView;
    LinearLayout linearLayout;
    AutoAdapter adapter;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_auto_play;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();

        setTitle("AutoPlayView");
        rightView = createRightText("清除");
        mTitleBar.addRight(rightView);
        
        rightView.setOnClickListener(this);
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        autoPlayView = (AutoPlayView) findViewById(R.id.auto_play);
        linearLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        TextView tv = (TextView) findViewById(R.id.indicator_tv);
        
        // 设置indicator需要的布局
        // autoPlayView.setIndicatorLayout(linearLayout);
        // indicator item
        autoPlayView.setIndicatorItemPadding(AppUtils.dp2px(this, 6));
        autoPlayView.setIndicatorItemLayout(android.R.layout.simple_list_item_single_choice);
        
        // 设置indicator文本显示
        // autoPlayView.setIndicatorTextView(tv);

        adapter = new AutoAdapter();
        autoPlayView.setAdapter(adapter);
    }
    
    @Override
    protected void initData() {
        super.initData();
        ArrayList<Integer> list = new ArrayList<>();
        // list.add("http://test.mainaer.com/uploads/adv/2015-11-19/564d485f90e64.jpg");
        // list.add("http://test.mainaer.com/uploads/adv/2015-11-24/5654349b56178.jpg");
        // list.add("http://test.mainaer.com/uploads/adv/2015-11-24/5654330bbc480.jpg");
        // list.add("http://test.mainaer.com/uploads/adv/2015-11-17/564afedf2de31.jpg");
        list.add(android.R.color.holo_green_dark);
        list.add(android.R.color.holo_orange_dark);
        list.add(android.R.color.holo_red_dark);
        list.add(android.R.color.holo_blue_bright);
        
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
        autoPlayView.initIndicatorLayout();
        autoPlayView.setInterval(1000);
        autoPlayView.start();
    }
    
    class AutoAdapter extends AfPagerAdapter<Integer> {
        
        @Override
        public int getLayout() {
            return R.layout.title_right_iv;
        }
        
        @Override
        public void onUpdateView(View convertView, int position) {
            convertView.setBackgroundResource(getItem(position));
        }
    }
}
