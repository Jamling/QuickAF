package cn.ieclipse.af.demo.sample.volley;

import android.view.View;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseInfo;
import cn.ieclipse.af.demo.common.api.SimpleController;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */

public class SimpleControllerSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_simple_controller;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            SimpleController<MyInfo> controller = new SimpleController<>(new SimpleController.SimpleListener<MyInfo>() {
                @Override
                public void onSuccess(MyInfo myInfo) {
                    tv1.setText(myInfo.toString());
                }

                @Override
                public void onError(RestError error) {
                    toastError(error);
                }
            });
            controller.setUrl(new URLConst.Url("")).load(null);
        }
        else if (v == btn2) {
            SimpleController<List<MyInfo>> controller = new SimpleController<>(
                new SimpleController.SimpleListener<List<MyInfo>>() {
                    @Override
                    public void onSuccess(List<MyInfo> myInfo) {
                        tv1.setText(myInfo.toString());
                    }

                    @Override
                    public void onError(RestError error) {
                        toastError(error);
                    }
                });
            controller.setUrl(new URLConst.Url("")).load2List(null);
        }
        super.onClick(v);
    }

    public static class MyInfo extends BaseInfo {
        public String id;
        public String name;

        @Override
        public void mock() {
            super.mock();
            id = String.valueOf(RandomUtils.genInt(10));
            name = RandomUtils.genGBK(1, 10);
        }

        @Override
        public String toString() {
            return String.format("id=%s,name=%s", id, name);
        }
    }
}
