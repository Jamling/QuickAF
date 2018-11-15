package cn.ieclipse.af.demo.sample.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.PermissionUtils;

/**
 * Description
 *
 * @author Jamling
 */

public class PermissionUtilsSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_permissions;
    }

    @Override
    public void onClick(View v) {
        if (btn1 == v) {
            DialogUtils.showToast(getContext(), "has permission ? " + PermissionUtils
                .hasPermissions(getContext(), Manifest.permission.READ_PHONE_STATE));
        }
        else if (btn2 == v) {
            PermissionUtils.requestPermissions(PermissionUtilsSample.this, 0x01, chk1.isChecked() ? callback : null,
                Manifest.permission.READ_PHONE_STATE);
        }
        super.onClick(v);
    }

    private PermissionUtils.PermissionExplainCallback callback = new PermissionUtils.PermissionExplainCallback() {
        @Override
        public void onRationale(final String permission) {
            DialogUtils.showAlert(getActivity(), 0, "Request Runtime Permission",
                "Here is my explain for " + permission + "\n Do you want to request it continue?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.requestPermissions(PermissionUtilsSample.this, 0x01, permission);
                    }
                }, DialogUtils.defaultOnClick());
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = PermissionUtils.isGraned(permissions, grantResults);
        DialogUtils.showToast(getContext(), "onRequestPermissionsResult all granted ? " + granted);
    }
}
