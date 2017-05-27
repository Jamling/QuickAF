/*
 * Copyright (C) 2015-2017 QuickAF
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
package cn.ieclipse.af.demo.sample.third;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.pay.alipay.Alipay;
import cn.ieclipse.pay.alipay.OrderInfoUtil2_0;
import cn.ieclipse.pay.alipay.PayResult;
import cn.ieclipse.pay.wxpay.OrderInfoUtil;
import cn.ieclipse.pay.wxpay.Wxpay;

/**
 * Description
 *
 * @author Jamling
 */
public class PayFragment extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_pay;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        setOnClickListener(tv1, tv2, tv3, tv4);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tv3.setEnabled(!TextUtils.isEmpty(s));
        tv4.setEnabled(tv3.isEnabled());
    }

    @Override
    public void onClick(View v) {
        if (v == tv1) {
            doAlipay(null);
        }
        else if (v == tv2) {
            doWxpay(null);
        }
        else if (v == tv3) {
            String info = et1.getText().toString().trim();
            doAlipay(info);
        }
        else if (v == tv4) {
            String info = et1.getText().toString().trim();
            doWxpay(info);
        }
        super.onClick(v);
    }

    private void doAlipay(String orderInfo) {

        final Activity activity = getActivity();
        Alipay alipay = new Alipay(getActivity());
        alipay.setPayListener(new Alipay.PayListener() {
            @Override
            public void onPaySuccess(PayResult payResult) {
                DialogUtils.showToast(activity, "支付成功");
            }

            @Override
            public void onPayWaiting(PayResult payResult) {
                DialogUtils.showToast(activity, "支付结果确认中...");
            }

            @Override
            public void onPayCancel(PayResult payResult) {
                DialogUtils.showToast(activity, "您已取消支付");
            }

            @Override
            public void onPayFailure(PayResult payResult) {
                DialogUtils.showToast(activity, "支付失败\n" + payResult.getMemo());
            }
        });

        if (TextUtils.isEmpty(orderInfo)) {
            // set v1 config
            Alipay.DEBUG = true;
            Alipay.Config.appId = "";
            Alipay.Config.rsa_private = "";
            Alipay.Config.rsa_public = "";
            Alipay.Config.notify_url = new URLConst.Url("app/pay/alipay_notify.do").getUrl();

            String trans_order_id = OrderInfoUtil2_0.genOutTradeNo();
            Map<String, String> map = OrderInfoUtil2_0.buildOrderParamMap(trans_order_id, "测试支付", "测试商品1，测试商品2",
                String.valueOf(0.01f), null);
            orderInfo = OrderInfoUtil2_0.getOrderInfo(map);

            alipay.payV1(orderInfo);
        }
        else {
            alipay.payV2(orderInfo);
        }
    }

    private void doWxpay(String orderInfo) {
        final Activity activity = getActivity();
        Wxpay wxpay = Wxpay.getInstance(activity);
        wxpay.setPayListener(new cn.ieclipse.pay.wxpay.Wxpay.PayListener() {
            @Override
            public void onPaySuccess(BaseResp resp) {
                DialogUtils.showToast(activity, "支付成功：" + resp.errStr);
            }

            @Override
            public void onPayCanceled(BaseResp resp) {
                DialogUtils.showToast(activity, "支付取消");
            }

            @Override
            public void onPayFailure(BaseResp resp) {
                DialogUtils.showToast(activity, "支付失败：" + resp.errStr);
            }
        });
        if (!TextUtils.isEmpty(orderInfo)) {
            PayReq req = OrderInfoUtil.getPayReq(orderInfo);
            wxpay.pay(req);
        }
        else {
            Wxpay.DEBUG = true;
            Wxpay.Config.api_key = "";
            Wxpay.Config.app_id = "";
            Wxpay.Config.mch_id = "";
            Wxpay.Config.notify_url = new URLConst.Url("app/pay/wxpay_notify.do").getUrl();

            Wxpay.DefaultOrderTask task = new Wxpay.DefaultOrderTask(wxpay);
            String trans_order_id = OrderInfoUtil2_0.genOutTradeNo();
            task.setParams(OrderInfoUtil.buildOrderParamMap(trans_order_id, "测试支付", "", "1", null, null, null));
            task.execute();
        }
    }
}
