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

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
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
public class AutoPlayViewActivity extends BaseActivity implements View.OnTouchListener{
    TextView rightView;
    AutoPlayView autoPlayView;
    LinearLayout linearLayout;
    AutoAdapter adapter;
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_auto_play;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();

        setTitle("AutoPlayView");
        rightView = createRightText("停止");
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
        //autoPlayView.setIndicatorItemLayout(android.R.layout.simple_list_item_single_choice);
        autoPlayView.setIndicatorItemLayout(R.layout.main_pager_indicator_item);

        // 设置indicator文本显示
        // autoPlayView.setIndicatorTextView(tv);

        adapter = new AutoAdapter();
        autoPlayView.setAdapter(adapter);
    }
    
    @Override
    protected void initData() {
        super.initData();

        list.add(android.R.color.holo_green_dark);
        list.add(android.R.color.holo_orange_dark);
        list.add(android.R.color.holo_red_dark);
        list.add(android.R.color.holo_blue_bright);
        
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
        autoPlayView.initIndicatorLayout();
        autoPlayView.setInterval(1000);
        autoPlayView.start();

        // 第二个autoplay view demo
        initAnotherDemo();
    }

    @Override
    public void onClick(View v) {
        if (v == rightView) {
            autoPlayView.stop();
        }
        super.onClick(v);
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

    //==============================一下实现autoplay的另一种效果=================================
    private final static int MSG_CODE = 0x11;
    private final static int DELAY_MILLIONS = 2000;
    private ViewPager pager = null;
    public int currentpositon = 0;
    public int oldpositon = 0;

    void initAnotherDemo(){
        pager = (ViewPager) findViewById(R.id.view_pager);

        AutoAdapter2 adapter = new AutoAdapter2();
        adapter.setDataList(list);
        pager.setAdapter(adapter);
        startAutoChange();
    }

    /**
     * 开启自动切换
     */
    private void startAutoChange() {
        // 设置左右缓存页面
        pager.setOffscreenPageLimit(2);
        // 保证起始页可以左滑，防止左侧出现空白页
        pager.setCurrentItem(list.size() * 100);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                int currentP = index % list.size();
                // 更新小点点颜色
                //changePointView2(oldpositon, currentP);
                oldpositon = currentP;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        startPlay();
        //addPointView();

        // 按下时停止自动滑动,弹起时继续定时滑动
        pager.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_SCROLL:
                stopPlay();
                break;
            case MotionEvent.ACTION_UP:
                startPlay();
                break;
        }
        return false;
    }

    private void stopPlay() {
        handler.removeMessages(MSG_CODE);
    }

    private void startPlay() {
        handler.sendEmptyMessageDelayed(MSG_CODE, DELAY_MILLIONS);
    }

    /**
     * 设置选中item
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CODE) {
                Log.i("eeeeee", currentpositon + "");
                currentpositon = pager.getCurrentItem();
                currentpositon++;

                pager.setCurrentItem(currentpositon, true);
                startPlay();
            }
        }
    };

    class AutoAdapter2 extends AfPagerAdapter<Integer> {

        @Override
        public int getLayout() {
            return R.layout.title_right_iv;
        }

        @Override
        public int getCount() {
            // 实现无限循环
            return Integer.MAX_VALUE;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            int pos = position % mDataList.size();
            convertView.setBackgroundResource(getItem(pos));
        }
    }

}
