package android.util;

/**
 * Description
 *
 * @author Jamling
 */

public class Log {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    public static  boolean isLoggable(String tag, int level){
        return false;
    }

    public static int i(String tag, String msg) {
        System.out.println(msg);
        return 0;
    }
    public static int w(String tag, String msg) {
        System.out.println(msg);
        return 0;
    }
}
