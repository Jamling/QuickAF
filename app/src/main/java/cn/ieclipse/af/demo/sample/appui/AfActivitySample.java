package cn.ieclipse.af.demo.sample.appui;

import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;

public class AfActivitySample extends SampleBaseActivity {
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_af_activity;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("AfActivity demo");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        btn3.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (btn1 == v){
            setOverlay(!isOverlay());
        }
        else if (btn2 == v){
            setShowTitleBar(!isShowTitleBar());
        }
        else if (btn3 == v){
            setImmersiveMode(!isImmersiveMode());
        }
    }
}
