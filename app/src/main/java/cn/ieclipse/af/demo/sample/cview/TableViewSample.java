package cn.ieclipse.af.demo.sample.cview;

import android.text.Editable;
import android.view.View;
import android.widget.CompoundButton;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.TableView;

/**
 * Description
 *
 * @author Jamling
 */

public class TableViewSample extends SampleBaseFragment {
    
    TableView mGrid;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_table_view;
    }
    
    @Override
    public CharSequence getTitle() {
        return "TableViewSample";
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mGrid = (TableView) view.findViewById(R.id.grid);
        mGrid.setTextSize(AppUtils.sp2px(getActivity(), 14));// 14sp
        mGrid.setTextColor(0xffff0000);
        chk1.setChecked(true);
        chk2.setChecked(true);
        chk3.setChecked(true);
    }
    
    private void initGrid2() {
        int columns = RandomUtils.genInt(1, 4);
        // mGrid2.setWeights(new int[] { 0, 1, 0, 1 });
        mGrid.clear();
        mGrid.setNumColumns(columns);
        int sum = 0;
        int size = RandomUtils.genInt(5);
        for (int i = 0; i < size; i++) {
            int tmp = RandomUtils.genInt(1, columns);
            String[] ss = new String[tmp];
            for (int k = 0; k < tmp; k++) {
                ss[k] = RandomUtils.genGBK(2, 8);
            }
            mGrid.addRow(ss);
        }
    }
    
    private void addRow() {
        int tmp = RandomUtils.genInt(mGrid.getNumColumns() + 1);
        String[] ss = new String[tmp];
        for (int k = 0; k < tmp; k++) {
            ss[k] = RandomUtils.genGBK(2, 5);
        }
        mGrid.addRow(ss);
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn1) {
            initGrid2();
        }
        else if (v.getId() == R.id.btn2) {
            addRow();
        }
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chk1) {
            int f = mGrid.getShowHorizontalBorder();
            int f2 = mGrid.getShowVerticalBorder();
            if (isChecked) {
                f |= (TableView.SHOW_DIVIDER_BEGINNING | TableView.SHOW_DIVIDER_END);
                f2 |= (TableView.SHOW_DIVIDER_BEGINNING | TableView.SHOW_DIVIDER_END);
            }
            else {
                f ^= (TableView.SHOW_DIVIDER_BEGINNING | TableView.SHOW_DIVIDER_END);
                f2 ^= (TableView.SHOW_DIVIDER_BEGINNING | TableView.SHOW_DIVIDER_END);
            }
            mGrid.setShowHorizontalBorder(f);
            mGrid.setShowVerticalBorder(f2);
        }
        else if (buttonView == chk2) {
            int f = mGrid.getShowHorizontalBorder();
            if (isChecked) {
                f |= (TableView.SHOW_DIVIDER_MIDDLE);
            }
            else {
                f ^= (TableView.SHOW_DIVIDER_MIDDLE);
            }
            mGrid.setShowHorizontalBorder(f);
        }
        else if (buttonView == chk3) {
            int f = mGrid.getShowVerticalBorder();
            if (isChecked) {
                f |= (TableView.SHOW_DIVIDER_MIDDLE);
            }
            else {
                f ^= (TableView.SHOW_DIVIDER_MIDDLE);
            }
            mGrid.setShowVerticalBorder(f);
        }
        mGrid.invalidate();
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        if (et1.getEditableText() == s) {
            try {
                mGrid.setHorizontalSpacing(AppUtils.dp2px(getActivity(), Integer.parseInt(s.toString())));
                mGrid.requestLayout();
            } catch (Exception e) {
                
            }
        }
        else if (et2.getEditableText() == s) {
            try {
                mGrid.setVerticalSpacing(AppUtils.dp2px(getActivity(), Integer.parseInt(s.toString())));
                mGrid.requestLayout();
            } catch (Exception e) {
                
            }
        }
    }
}
