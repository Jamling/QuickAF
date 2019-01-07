package cn.ieclipse.af.demo.sample.utils;

import android.content.Intent;
import android.net.Uri;

import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class NavSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return 0;
    }

    public void goBaiduApp(long latitudes, double longitude) {
        StringBuffer stringBuffer = new StringBuffer("baidumap://map/navi?location=").append(latitudes).append(",")
            .append(longitude).append("&type=TIME");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.baidu.BaiduMap");
        startActivity(intent);
    }

    public void goAmapApp(long latitudes, double longitude) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("yitu8_driver")
            .append("&lat=").append(latitudes).append("&lon=").append(longitude).append("&dev=").append(1).append(
                "&style=").append(0);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }


}
