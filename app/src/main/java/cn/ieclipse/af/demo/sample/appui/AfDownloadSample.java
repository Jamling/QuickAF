package cn.ieclipse.af.demo.sample.appui;

import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DownloadUtils;

/**
 * Description
 *
 * @author Jamling
 */

public class AfDownloadSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_download;
    }

    @Override
    public void onClick(View v) {
        String url = et1.getText().toString().trim();
        if (v == btn1) {
            long id = DownloadUtils.download(getActivity(), url, null);
            if (id < 0) {
                DownloadUtils.openUrl(getActivity(), url);
            }
        }
        else {
            DownloadUtils.openUrl(getActivity(), url);
        }
        super.onClick(v);
    }
}
