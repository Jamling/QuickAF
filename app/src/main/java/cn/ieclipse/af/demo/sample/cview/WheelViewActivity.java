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

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.ieclipse.af.demo.R;
import static cn.ieclipse.af.demo.R.id.et5;
import cn.ieclipse.af.demo.common.view.WheelTimeContainer;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.view.wheelview.WheelView;

/**
 * Description
 *
 * @author Jamling
 */
public class WheelViewActivity extends SampleBaseActivity {

    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;

    WheelTimeContainer container;

    private TextView mOk;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_wheel_view;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("Date Wheel");
        mOk = createRightText("确定");
        mTitleBar.addRight(mOk);
        setOnClickListener(mOk);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        year = (WheelView) view.findViewById(R.id.year);
        month = (WheelView) view.findViewById(R.id.month);
        day = (WheelView) view.findViewById(R.id.day);
        hour = (WheelView) view.findViewById(R.id.hour);

        container = (WheelTimeContainer) view.findViewById(R.id.wv_container);
        container.setStartTimeRelative(Calendar.YEAR, -10);
        container.setEndTimeRelative(Calendar.YEAR, 10);
        container.show(0);

        hour.setDrawShadows(false);
        // month.setShadowColor(0x80000000, 0x00ffffff, 0x80000000);
        month.setCyclic(true);
        month.setVisibleItems(7);
        day.setCyclic(false);

        chk1.setChecked(true);
        chk2.setChecked(true);
        chk3.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        if (v == mOk) {
            String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(container.getSelectedDate());
            //DialogUtils.showToast(this, d);
            tv1.setText(d);
        }
        super.onClick(v);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int show = container.getShow();
        if (buttonView == chk1) {
            show ^= WheelTimeContainer.SHOW_YEAR;
        }
        else if (buttonView == chk2) {
            show ^= WheelTimeContainer.SHOW_MONTH;
        }
        else if (buttonView == chk3) {
            show ^= WheelTimeContainer.SHOW_DAY;
        }
        else if (buttonView == chk4) {
            show ^= WheelTimeContainer.SHOW_HOUR;
        }
        else if (buttonView == chk5) {
            show ^= WheelTimeContainer.SHOW_MINUTE;
        }
        container.show(show);

        mOk.performClick();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s == et1.getText()) {
            container.setYearLabel(s.toString());
        }
        else if (s == et2.getText()) {
            container.setMonthLabel(s.toString());
        }
        else if (s == et3.getText()) {
            container.setDayLabel(s.toString());
        }
        else if (s == et4.getText()) {
            container.setHourLabel(s.toString());
        }
        else if (s == et5.getText()) {
            container.setHourLabel(s.toString());
        }
        mOk.performClick();
    }
}
