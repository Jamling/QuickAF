/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.util;

import java.io.File;

import android.os.Environment;

/**
 * @author Jamling
 * 
 */
public class SDUtils {
    
    /**
     * 
     */
    public SDUtils() {
        // TODO Auto-generated constructor stub
    }
    
    public static File getSD() {
        return Environment.getExternalStorageDirectory();
    }
}
