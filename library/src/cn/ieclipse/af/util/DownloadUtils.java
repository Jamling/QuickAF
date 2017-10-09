package cn.ieclipse.af.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

/**
 * Description
 *
 * @author Jamling
 * @since 2.1.1
 */

public abstract class DownloadUtils {

    public static boolean isDownloadServiceAvailable(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            //TODO
        }
    }

    public static long download(Context context, String url, RequestCallback callback) {
        DownloadManager dm = (DownloadManager) context.getApplicationContext().getSystemService(
            Context.DOWNLOAD_SERVICE);
        if (dm != null && DownloadUtils.isDownloadServiceAvailable(context)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, FileUtils.getName(url));
            request.setTitle(FileUtils.getBaseName(FileUtils.getName(url)));
            request.allowScanningByMediaScanner();
            if (callback != null) {
                callback.beforeDownload(request);
            }
            return dm.enqueue(request);
        }
        else {

        }
        return -1;
    }

    public static void openUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, null));
    }

    public static Uri getDownloadUri(Context context, long downloadId) {
        DownloadManager dm = (DownloadManager) context.getApplicationContext().getSystemService(
            Context.DOWNLOAD_SERVICE);
        return dm.getUriForDownloadedFile(downloadId);
    }

    public interface RequestCallback {
        void beforeDownload(DownloadManager.Request request);
    }
}
