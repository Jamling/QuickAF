/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */

package cn.ieclipse.af.gauth;

import android.app.Activity;
import android.content.Context;

/**
 * Authorize interface
 * 
 * @author melord_li
 * @since 1.0
 */
public interface Authorizer {
    
    /**
     * Get accounts
     * 
     * @param listener
     *            authorize linstener
     */
    public void fetchAccounts(AuthorizationListener<String[]> listener);
    
    /**
     * Add account.
     * 
     * @param activity
     *            activity instance
     * @param listener
     *            listener
     */
    public void addAccount(Activity activity,
            AuthorizationListener<String> listener);
    
    /**
     * Get assigned account auth token
     * 
     * @param account
     *            your account, e.g. gmail account.
     * @param activity
     *            activity instance
     * @param listener
     *            listener
     */
    public void fetchAuthToken(String account, Activity activity,
            AuthorizationListener<String> listener);
    
    /**
     * Get account auth token.
     * 
     * @param accountName
     *            account name
     * @return auth token
     */
    public String getAuthToken(String accountName);
    
    /**
     * Fresh auth token
     * 
     * @param accountName
     *            account name
     * @param authToken
     *            old auth token
     * @return new auth token
     */
    public String getFreshAuthToken(String accountName, String authToken);
    
    /**
     * Invalidate auth token
     * 
     * @param accountType
     *            account type
     * @param authToken
     *            cached auth token
     */
    public void clearAuthToken(String accountType, String authToken);
    
    /**
     * Authorizer Listener
     * 
     * @author melord_li
     * 
     * @param <T>
     *            Returned Object data struct
     */
    public static interface AuthorizationListener<T> {
        /**
         * Authorize success.
         * 
         * @param result
         *            authorize data struct
         */
        public void onSuccess(T result);
        
        /**
         * Authorize canceled.
         */
        public void onCanceled();
        
        /**
         * Authorize meet an exception
         * 
         * @param e
         *            exception
         */
        public void onError(Exception e);
    }
    
    /**
     * Authorizer Factory, you can use this to new an Authorizer instance. like
     * GlsAuthorizer
     * 
     * @author melord_li
     * 
     */
    public static interface AuthorizerFactory {
        /**
         * Get new authorizer.
         * 
         * @param context
         *            context.
         * @param type
         *            account type
         * @return created authorizer instance
         */
        public Authorizer getAuthorizer(Context context, String type);
    }
    
}
