/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.cview;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.BadgeView2;
import cn.ieclipse.af.view.RadioBadgeView;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/1/6.
 */
public class RadioButtonBadgeViewActivity extends SampleBaseActivity
        implements OnItemSelectedListener {
    
    int hg = BadgeView2.STYLE_BOTH;
    int vg = Gravity.CENTER_VERTICAL;
    
    int[] hgs = { BadgeView2.STYLE_BOTH, BadgeView2.STYLE_TEXT,
            BadgeView2.STYLE_BACKGROUND };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    
    RadioBadgeView rb1;
    RadioBadgeView rb2;
    RadioBadgeView rb3;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_radio_badge;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        // 默认是不带有文本的tip
        int r = AppUtils.dp2px(view.getContext(), 4);
        int ts = AppUtils.sp2px(view.getContext(), 12);
        
        rb1 = (RadioBadgeView) findViewById(R.id.radio_badge1);
        rb2 = (RadioBadgeView) findViewById(R.id.radio_badge2);
        rb3 = (RadioBadgeView) findViewById(R.id.radio_badge3);
        
        rb1.getBadgeView().setBadgeBackground(ts >> 1, 0xffff0000);
        rb1.getBadgeView().setTextSize(ts);
        rb1.getBadgeView().setMax(10, null);
        
        rb2.getBadgeView().setBadgeBackground(
                getResources().getDrawable(android.R.drawable.btn_radio));
        rb2.getBadgeView().setTextColor(0xff00ff00);
        rb2.getBadgeView().setTextSize(ts * 3 / 2);
        rb2.getBadgeView().setBadgePadding(r, 0, r, 0);
        
        rb3.getBadgeView().setBadgeBackground(
                getResources().getDrawable(R.mipmap.logo));
        rb3.getBadgeView().setTextColor(0xff0000ff);
        rb3.getBadgeView().setTextSize(ts * 2 / 3);
    }
    
    @Override
    public void onClick(View v) {
        if (v == btn1) {
            rb1.incrementBadgeCount(1);
            rb2.incrementBadgeCount(1);
            rb3.incrementBadgeCount(1);
        }
        else if (v == btn2) {
            int c = rb1.getBadgeCount() - 1;
            rb1.setBadgeCount(c < 0 ? 0 : c);
            
            c = rb2.getBadgeCount() - 1;
            rb2.setBadgeCount(c < 0 ? 0 : c);
            
            c = rb3.getBadgeCount() - 1;
            rb3.setBadgeCount(c < 0 ? 0 : c);
        }
        super.onClick(v);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        int pos = parent.getSelectedItemPosition();
        if (spn1 == parent) {
            hg = hgs[pos];
            rb1.getBadgeView().setBadgeStyle(hg);
            rb2.getBadgeView().setBadgeStyle(hg);
            rb3.getBadgeView().setBadgeStyle(hg);
        }
        else if (spn2 == parent) {
            int r = Integer.parseInt(
                    getResources().getStringArray(R.array.sample_round_radius)[pos]);
            int radius = AppUtils.dp2px(this, r);
            rb1.getBadgeView().setBadgePadding(radius, 0, radius, 0);
            rb2.getBadgeView().setBadgePadding(radius, 0, radius, 0);
            rb3.getBadgeView().setBadgePadding(radius, 0, radius, 0);
        }
    }
    
}
