package cn.ieclipse.af.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.ieclipse.af.common.Logger;

/**
 * @author Jamling
 */
public final class FileUtils {
    private static Logger mLogger = Logger.getLogger(FileUtils.class);

    private FileUtils() {
        
    }
    
    /**
     * Get file name from file path
     *
     * @param path file path
     *
     * @return file name
     */
    public static String getName(String path) {
        File f = new File(path);
        String name = f.getName();
        return name;
    }

    public static String getBaseName(@NonNull String path) {
        int pos = path.lastIndexOf('.');
        if (pos >= 0) {
            return path.substring(0, pos);
        }
        return path;
    }
    
    public static String getExtension(String path) {
        String name = path;//getName(path);
        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            return name.substring(pos + 1);
        }
        return "";
    }
    
    public static String getExtensionFromUrl(String url) {
        if (url != null && url.length() > 0) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            
            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }
            
            int filenamePos = url.lastIndexOf('/');
            String filename = 0 <= filenamePos ? url.substring(filenamePos + 1) : url;
            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() /*
                                     * && Pattern
                                     * .matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+",
                                     * filename)
                                     */) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }
        
        return "";
    }
    
    public static File getCopyDestFile(File file) {
        if (!file.exists()) {
            return file;
        }
        String name = file.getName();
        File parent = file.getParentFile();
        int pos = name.lastIndexOf('.');
        String ext = "";
        String n = name;
        if (pos >= 0) {
            ext = name.substring(pos + 1);
            n = name.substring(0, pos);
            File temp = null;
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                temp = new File(parent, n + "(" + i + ")" + "." + ext);
                if (!temp.exists()) {
                    break;
                }
            }
            return temp;
        }
        else {
            File temp = null;
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                temp = new File(parent, n + "(" + i + ")");
                if (!temp.exists()) {
                    break;
                }
            }
            return temp;
        }
    }

    /**
     * Get internal storage files dir.
     * <p>
     * sample: /data/data/<var>package</var>/files/
     * </p>
     *
     * @param context context
     *
     * @return internal data files dir
     * @see android.content.Context#getFilesDir()
     */
    public static File getInternal(Context context) {
        return context.getFilesDir();
    }

    /**
     * Get external storage files dir.
     * <p>
     * sample: /sdcard/Android/data/<var>package</var>/<var>dir</var>
     * </p>
     *
     * @param context context
     * @param dir     dir name
     *
     * @return internal data files dir
     * @see android.content.Context#getExternalFilesDir(String)
     */
    public static File getExternal(Context context, String dir) {
        return context.getExternalFilesDir(dir);
    }

    public static long getCacheSize(Context context, boolean includeInternal, boolean includeExternal) {
        long size = 0l;
        File dir = null;
        if (includeInternal) {
            dir = context.getCacheDir();
            size += FileUtils.getFileSize(dir);
        }
        if (includeExternal) {
            dir = context.getExternalCacheDir();
            size += FileUtils.getFileSize(dir);
        }
        return size;
    }

    public static void clearCache(Context context, boolean includeInternal, boolean includeExternal) {
        if (context == null) {
            return;
        }
        File dir = null;
        if (includeInternal) {
            dir = context.getCacheDir();
            FileUtils.rmdir(dir, true);
        }
        if (includeExternal) {
            dir = context.getExternalCacheDir();
            FileUtils.rmdir(dir, true);
        }
    }

    public static boolean writeObject(File dir, String name, Object object) {
        if (dir != null && !TextUtils.isEmpty(name)) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    mLogger.w("can't mkdir " + dir);
                    return false;
                }
            }
            File f = new File(dir, name);
            try {
                if (!f.exists()) {
                    if (!f.createNewFile()) {
                        mLogger.w("can't create file " + f.getAbsolutePath());
                        return false;
                    }
                }
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(object);
                IOUtils.closeStream(oos);
                return true;
            } catch (Exception e) {
                mLogger.w("an error occurred when write object to " + f.getAbsolutePath() + " msg:" + e.toString(), e);
                return false;
            }
        }
        return false;
    }

    public static Object readObject(File dir, String name) {
        Object ret = null;
        File f = new File(dir, name);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            ret = ois.readObject();
            IOUtils.closeStream(ois);
        } catch (Exception e) {
            mLogger.w("an error occurred when read object from " + f.getAbsolutePath() + " msg:" + e.toString(), e);
        }
        return ret;
    }

    public static void rmdir(File dir, boolean recursive) {
        if (dir != null && dir.exists()) {
            File[] fs = dir.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (f.isFile()) {
                        f.delete();
                    }
                    else {
                        if (recursive) {
                            rmdir(f, recursive);
                        }
                    }
                }
            }
            dir.delete();
        }
    }

    public static long getFileSize(File f) {
        if (f == null || !f.exists()) {
            return 0l;
        }
        if (f.isFile()) {
            return f.length();
        }
        else {
            long ret = 0l;
            File[] fs = f.listFiles();
            if (fs != null) {
                for (File t : fs) {
                    ret += getFileSize(t);
                }
            }
            return ret;
        }
    }

    public static String formatFileSize(long length) {
        if (length > (1 << 20)) {
            return length / (1 << 20) + "M";
        }
        else if (length > (1 << 10)) {
            return length / (1 << 10) + "K";
        }
        return length + "B";
    }
}
