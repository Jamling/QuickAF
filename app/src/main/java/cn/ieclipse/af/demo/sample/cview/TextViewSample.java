package cn.ieclipse.af.demo.sample.cview;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.view.TextViewFixTouchConsume;
import cn.ieclipse.util.RandomUtils;

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

        tv2.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        String at = String.format(AtTagHandler.AT_FORMAT, "@" + RandomUtils.genGBK(5),
            String.valueOf(RandomUtils.genInt(100)));
        String at2 = String.format(AtTagHandler.AT_FORMAT, "@" + RandomUtils.genGBK(15),
            String.valueOf(RandomUtils.genInt(100)));
        String source = String.format("转发了%s的微博，//%s真搞笑", at, at2);
        tv2.setText(Html.fromHtml(source, null, new AtTagHandler(getContext())));
    }
}
