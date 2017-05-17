package cn.ieclipse.af.volley.mock;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.test.mock.MockPackageManager;

import java.io.File;

public class MockContext extends android.test.mock.MockContext {
    @Override
    public File getCacheDir() {
        File cache = new File("./");
        cache.mkdirs();
        System.out.println(cache.getAbsoluteFile());
        return cache;
    }

    @Override
    public String getPackageName() {
        return getClass().getPackage().getName();
    }

    @Override
    public PackageManager getPackageManager() {
        return new MyMockPackageManager();
    }

    @Override
    public Looper getMainLooper() {
        return super.getMainLooper();
    }

    public static class MyMockPackageManager extends MockPackageManager {
        @Override
        public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
            MyPackageInfo packageInfo = new MyPackageInfo();
            packageInfo.versionCode = 1;
            return packageInfo;
        }
    }

    public static class MyPackageInfo extends PackageInfo{

    }
}
