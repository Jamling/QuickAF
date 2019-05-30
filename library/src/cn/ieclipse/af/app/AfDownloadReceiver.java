package cn.ieclipse.af.app;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DownloadUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.SDUtils;

/**
 * The default download receiver for application.
 * You can extend to it and configured in your app manifest
 * <receiver android:name="your.package.DownloadReceiver">
 * <intent-filter>
 * <action android:name="android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED" />
 * <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
 * </intent-filter>
 * </receiver>
 *
 * @author Jamling
 * @since 2.1.1
 */

public class AfDownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Uri uri = DownloadUtils.getDownloadUri(context, downloadId);
            if (match(context, downloadId, uri)) {
                AppUtils.installApk(context, uri);
            }
        }
        else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Intent target = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Whether the download is start by your application
     *
     * @param context    Context
     * @param downloadId download id
     * @param uri        the download destination location
     *
     * @return true if the download is start by your application
     */
    protected boolean match(Context context, long downloadId, Uri uri) {
        if (uri != null && uri.getPath().startsWith(SDUtils.getExternal(context, null).getAbsolutePath())) {
            return true;
        }
        return false;
    }
}
