package cn.ieclipse.af.demo.sample.cview;

import android.text.method.ScrollingMovementMethod;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */

public class TextViewSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_cview_text_view;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
