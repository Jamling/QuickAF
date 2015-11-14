/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.legcy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import cn.ieclipse.af.util.IOUtils;

/**
 * Http connection thread. http url or HttpUrlConnection and HttpListener must
 * instant in construtor. while connection successfully and server response 200,
 * this thread will read data to a InputStream, and pass it in
 * {@link HttpListener#onSuccess(int, InputStream)}, otherwise,
 * {@link HttpListener#onException(int, Exception)} callback will be invoked
 * 
 * Sample:
 * 
 * <pre>
 * HttpListener listener = new HttpListener(){
 *  public void onSuccess(InputStream is){
 *      Log.v(TAG,"get response successfully!");
 *      // parse response inputstream to model
 *      Parser p = new SAXParer();
 *      p.parse(is);
 *      ...
 *      IOUtils.closeStream(is);
 *      
 *  }
 *  public void onException(int code, Exception e){
 *      Toast.makeText("There is a problem for network",1).show();
 *  }
 * }
 * HttpThread thread = new HttpThread("http://www.baidu.com",listener);
 * thread.start();
 * </pre>
 * 
 * @author melord
 * @version 1.0
 * 
 * 
 */
public class HttpThread extends Thread {
    
    /**
     * android log tag
     */
    private static final String TAG = "HttpThread";
    /**
     * callback
     */
    private HttpListener mListener;
    /**
     * wrapped http connection
     */
    private HttpURLConnection mConnection;
    /**
     * http request method
     */
    private String mMethod = "GET";
    
    /**
     * max connection retry count
     */
    private int mRetryCount = 0;
    /**
     * current connection retry times
     */
    private int mRetry = 0;
    /**
     * delay time for next connection
     */
    private int mDelay = 5000;
    
    /**
     * post data input stream. for 3.0 or higher, write data to http output
     * stream have more limited
     */
    private InputStream mPostData;
    
    /**
     * Set {@link HttpURLConnection}
     * 
     * @param conn
     *            {@link HttpURLConnection}
     */
    public void setConnection(HttpURLConnection conn) {
        this.mConnection = conn;
    }
    
    /**
     * Set callback to listen connection result
     * 
     * @param listener
     *            {@link HttpListener} callback
     */
    public void setHttpLinstener(HttpListener listener) {
        this.mListener = listener;
    }
    
    /**
     * Construct a http thread with url
     * 
     * @param url
     *            connection url
     * @param listener
     *            {@link HttpListener} callback
     */
    public HttpThread(String url, HttpListener listener) {
        try {
            this.mConnection = (HttpURLConnection) new URL(url)
                    .openConnection();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        this.mListener = listener;
    }
    
    /**
     * Construct a http thread with {@link HttpURLConnection}
     * 
     * @param conn
     *            {@link HttpURLConnection}
     * @param listener
     *            {@link HttpListener} callback
     */
    public HttpThread(HttpURLConnection conn, HttpListener listener) {
        this.mConnection = conn;
        this.mListener = listener;
    }
    
    /**
     * Set http thread connect timeout. see
     * {@link HttpURLConnection#setConnectTimeout(int)}
     * 
     * @param timeout
     *            max connect time
     */
    public void setTimeout(int timeout) {
        if (mConnection != null) {
            this.mConnection.setConnectTimeout(timeout);
        }
    }
    
    /**
     * Set http request method
     * 
     * @param method
     *            request method, GET or POST
     */
    public void setRequestMethod(String method) {
        try {
            mConnection.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new IllegalArgumentException("Unsupported request method");
        }
        this.mMethod = method;
    }
    
    /**
     * set max connect retry count.
     * 
     * @param count
     *            max retry count
     */
    public void setRetryCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("illegal retry count : " + count);
        }
        mRetryCount = count;
    }
    
    /**
     * Get current retry times
     * 
     * @return retry times
     */
    public int getRetry() {
        return mRetry;
    }
    
    /**
     * Post data attempt to write into http OutputStream. Note, this method
     * don't write to socket output stream immediately but do after thread
     * started. After thread ended, the post data also not exists, so, need to
     * post data again when you retry connect.
     * 
     * @param data
     *            data
     * @param start
     *            offset of data
     * @param length
     *            post length of data
     */
    public void post(byte[] data, int start, int length) {
        mPostData = new ByteArrayInputStream(data, start, length);
    }
    
    private void copyConnection() {
        HttpURLConnection copy = null;
        try {
            copy = (HttpURLConnection) mConnection.getURL().openConnection();
            copy.setConnectTimeout(mConnection.getConnectTimeout());
            
            copy.setDoInput(mConnection.getDoInput());
            copy.setAllowUserInteraction(mConnection.getAllowUserInteraction());
            copy.setDoOutput(mConnection.getDoOutput());
            copy.setRequestMethod(mConnection.getRequestMethod());
            // sorry can't set request properties for connected connection
            // Map<String, List<String>> props = mConnection
            // .getRequestProperties();
            // for (String key : props.keySet()) {
            // copy.setRequestProperty(key,
            // mConnection.getRequestProperty(key));
            // }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            copy = null;
        }
        this.mConnection = copy;
    }
    
    @Override
    public void run() {
        int code = -1;
        InputStream is = null;
        InputStream in = null;
        if (mMethod != null) {
            try {
                mConnection.setRequestMethod(mMethod);
            } catch (ProtocolException e) {
                if (mListener != null) {
                    mListener.onException(code, e);
                }
            }
        }
        while (true) {
            try {
                if (mConnection == null) {
                    Log.e(TAG, getInfo() + " connection null");
                    break;
                }
                if ("POST".equalsIgnoreCase(mConnection.getRequestMethod())
                        && mPostData != null) {
                    mConnection.setDoOutput(true);
                    // mConnection.setFixedLengthStreamingMode(mBufferSize);
                    // block
                    OutputStream sos = mConnection.getOutputStream();
                    IOUtils.copyStream(mPostData, sos);
                    IOUtils.closeStream(mPostData);
                    IOUtils.closeStream(sos);
                }
                // mConnection.connect();
                code = mConnection.getResponseCode();
                
                if (code >= HttpURLConnection.HTTP_OK
                        && code < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Log.v(TAG, getInfo() + " connect successfully");
                    int length = mConnection.getContentLength();
                    in = mConnection.getInputStream();
                    is = IOUtils.readSocketStream(in, length);
                    
                    if (mListener != null) {
                        mListener.onSuccess(code, is);
                    }
                }
                else {
                    // try {
                    // in = mConnection.getInputStream();
                    // is = IOUtils.readSocketStream(in, -1);
                    // // byte[] b = IOUtils.read2Byte(is);
                    // // String str = new String(b);
                    // // System.out.println(b);
                    // } catch (Exception e) {
                    //
                    // }
                    
                    String msg = mConnection.getResponseMessage();
                    Log.w(TAG, getInfo() + " msg " + code);
                    if (mListener != null) {
                        mListener.onException(code, new Exception(msg));
                    }
                }
                mConnection.disconnect();
                break;
            } catch (IOException e) {
                if (mListener != null) {
                    mListener.onException(code, e);
                }
                Log.e(TAG, getInfo() + " connection meet an IO exception", e);
                if (mRetry < mRetryCount) {
                    mRetry++;
                    try {
                        Thread.sleep(mDelay);
                    } catch (InterruptedException e1) {
                        
                    }
                    copyConnection();
                    retryPost(this.mConnection);
                }
                else {
                    mConnection.disconnect();
                    break;
                }
            }
            
        }
    }
    
    /**
     * Get created HttpURLConnection
     * 
     * @return HttpURLConnection
     */
    public HttpURLConnection getConnection() {
        return this.mConnection;
    }
    
    /**
     * When set retry count > 1, and the request method is post, need to
     * override this method to post data again.
     * 
     * @param connection
     *            current HttpURLConnection
     * @return whether retry post data or not
     */
    public boolean retryPost(HttpURLConnection connection) {
        return false;
    }
    
    private String getInfo() {
        return String.format("HttpThread %s url=%s", getName(), mConnection
                .getURL().toString());
    }
}
