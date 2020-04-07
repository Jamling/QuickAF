package cn.ieclipse.af.demo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.ieclipse.af.demo.my.CheckUpdateController;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.util.RegexUtils;
import cn.ieclipse.util.StringUtils;

/**
 * Description
 *
 * @author Jamling
 */

public class DemoUtils {

    public static boolean validateRequire(TextView widget) {
        if (TextUtils.isEmpty(widget.getText())) {
            DialogUtils.showToast(widget.getContext(), widget.getHint());
            return false;
        }
        return true;
    }

    public static boolean validatePhone(TextView mEtPhone) {
        if (TextUtils.isEmpty(mEtPhone.getText())) {
            DialogUtils.showToast(mEtPhone.getContext(), mEtPhone.getHint());
            return false;
        }
        String phone = mEtPhone.getText().toString();
        if (!RegexUtils.isMobilePhoneNumber(phone)) {
            DialogUtils.showToast(mEtPhone.getContext(), R.string.reg_hint_phone);
            return false;
        }
        return true;
    }

    public static boolean validatePwd(TextView widget) {
        if (TextUtils.isEmpty(widget.getText())) {
            DialogUtils.showToast(widget.getContext(), widget.getHint());
            return false;
        }
        else {
            char[] pwds = widget.getText().toString().toCharArray();
            if (pwds.length < 6 || pwds.length > 16) {
                DialogUtils.showToast(widget.getContext(), "密码长度不符合要求（6-16字符之间）");
                return false;
            }
//            else {
//                boolean a = false, b = false, c = false;
//                for (int i = 0; i < pwds.length; i++) {
//                    int ch = pwds[i];
//                    if (ch >= '0' && ch <= '9') {
//                        a = true;
//                    }
//                    else if (ch >= 'A' && ch <= 'Z') {
//                        b = true;
//                    }
//                    else if (ch >= 'a' && ch <= 'z') {
//                        b = true;
//                    }
//                    else {
//                        c = true;
//                    }
//                }
//                int t = 0;
//                if (a) {
//                    t++;
//                }
//                if (b) {
//                    t++;
//                }
//                if (c) {
//                    t++;
//                }
//                if (t < 2) {
//                    DialogUtils.showToast(widget.getContext(), "密码至少包含数字、字母、特殊字符中的两种");
//                    return false;
//                }
//            }
        }
        return true;
    }

//    public static DisplayImageOptions genNetImageOption(int imgRes) {
//        if (imgRes <= 0) {
//            imgRes = R.mipmap.logo;
//        }
//        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(imgRes).showImageOnFail(
//            imgRes).cacheOnDisk(true).build();
//        return options;
//    }

    public static void goToMarket(Context context) {
        if (context == null) {
            return;
        }
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void postUpgrade(final View view, final Context context) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (BuildConfig.FLAVOR.equals("baidu")) {
                        upgrade_baidu(view, context);
                    }
                    else if (BuildConfig.FLAVOR.equals("_360")) {
                        upgrade_360(view, context);
                    }
                    else {
                        upgrade_self(false, context);
                    }
                } catch (Exception e) {
                    // mLogger.e("baidu auto update sdk error : " + e);
                }
            }
        }, 1000);
    }

    private static void upgrade_baidu(final View view, final Context context) {
//        BDAutoUpdateSDK.uiUpdateAction(context, new UICheckUpdateCallback() {
//
//            @Override
//            public void onNoUpdateFound() {
//
//            }
//
//            @Override
//            public void onCheckComplete() {
//
//            }
//        });
    }

    private static void upgrade_360(final View view, final Context context) {
//        UpdateHelper.getInstance().init(context, context.getResources().getColor(R.color.colorPrimary));
//        UpdateHelper.getInstance().setDebugMode(true);
//        long intervalMillis = 10 * 1000L;           //第一次调用startUpdateSilent出现弹窗后，如果10秒内进行第二次调用不会查询更新
//        UpdateHelper.getInstance().autoUpdate(context.getPackageName(), false, intervalMillis);
    }

    public static void upgrade_self(final boolean manual, final Context context,
                                    final CheckUpdateController.UpdateListener listener) {
        CheckUpdateController.CheckRequest req = new CheckUpdateController.CheckRequest();
        req.versionName = AppUtils.getAppVersion(context);
        req.versionCode = String.valueOf(AppUtils.getPackageInfo(context).versionCode);
        CheckUpdateController controller = new CheckUpdateController(listener);
        controller.checkUpdate(req);
    }

    public static void upgrade_self(final boolean manual, final Context context) {
        CheckUpdateController.UpdateListener listener = new CheckUpdateController.DefaultUpdateListener(manual,
            context);
        upgrade_self(manual, context, listener);
    }
}
