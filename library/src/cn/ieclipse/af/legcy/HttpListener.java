/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.legcy;

import java.io.InputStream;

/**
 * Listener of {@link HttpThread}, if HttpThread get correct response,
 * {@link HttpListener#onSuccess(int, InputStream)} will called, otherwise
 * {@link HttpListener#onException(int, Exception)} will be called
 * 
 * @see HttpThread
 * @author melord
 * @version 1.0
 * 
 */
public interface HttpListener {
    /**
     * Callback when HttpThread get a correct response(200 &lt;= code &lt; 400).
     * <p>
     * Some common code:
     * </p>
     * <ul>
     * <li>200: http OK</li>
     * <li>302: http redirect</li>
     * <li>206: http part</li>
     * <li>400: http request error</li>
     * <li>403: http forbidden</li>
     * <li>500: http server error</li>
     * </ul>
     *
     * More http response code, please see {@link java.net.HttpURLConnection}
     * @param code
     *            Http response code
     * @param is
     *            InputStream of response
     */
    void onSuccess(int code, InputStream is);
    
    /**
     * Callback when HttpThread get a bad response or timeout
     * 
     * @param code
     *            http response code, if -1 means timeout
     * @param e
     *            exception
     */
    void onException(int code, Exception e);
}
