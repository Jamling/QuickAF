package cn.ieclipse.af.demo.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.FragmentActivity;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */

public class DialogsTargetFragment extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_dialog_target;
    }

    @Override
    public CharSequence getTitle() {
        return "模拟交互";
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            finish(Activity.RESULT_OK, null);
        }
        else if (v == btn2) {
            finish(Activity.RESULT_CANCELED, null);
        }
        super.onClick(v);
    }

    private void finish(int code, Intent data) {
        getActivity().setResult(code, data);
        getActivity().finish();
    }

    public static Intent create(Context context) {
        Intent intent = FragmentActivity.create(context, DialogsTargetFragment.class, false);
        return intent;
    }
}
