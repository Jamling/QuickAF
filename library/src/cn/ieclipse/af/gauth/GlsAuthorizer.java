/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */

package cn.ieclipse.af.gauth;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Google account authorizer
 * 
 * @author melord_li
 * @version 1.0
 * @since 1.0
 */
@SuppressLint("all")
public class GlsAuthorizer implements Authorizer {
    
    /**
     * Google account type
     */
    public static final String ACCOUNT_TYPE = "com.google";
    
    /**
     * Picasa auth token type
     */
    public static final String PICASA_AUTH_TOKEN_TYPE = "lh2";
    /**
     * Picasa auth features
     */
    private static final String[] PICASA_FEATURES = new String[] { "service_lh2" };
    
    /**
     * Youtube auth token type
     */
    public static final String YOUTUBE_AUTH_TOKEN_TYPE = "youtube";
    /**
     * Youtube auth features
     */
    private static final String[] YOUTUBE_FEATURES = new String[] { "service_youtube" };
    
    /**
     * Plus auth token type
     */
    public static final String PLUS_AUTH_TOKEN_TYPE = "View your Google+ id\nView your Public Google+ data";
    /**
     * Plus auth features
     */
    private static final String[] PLUS_FEATURES = new String[] { "" };
    
    private AccountManager accountManager;
    
    private final String authTokenType;
    
    private final String[] features;
    
    private static class Config {
        private static final String APP_NAME = "GlsAuthorizer";
    }
    
    public GlsAuthorizer(Context context, String authTokenType,
            String[] features) {
        accountManager = AccountManager.get(context);
        
        this.authTokenType = authTokenType;
        this.features = features;
    }
    
    public static class GlsAuthorizerFactory implements AuthorizerFactory {
        public Authorizer getAuthorizer(Context context, String authTokenType) {
            if (PICASA_AUTH_TOKEN_TYPE.equals(authTokenType)) {
                return new GlsAuthorizer(context, PICASA_AUTH_TOKEN_TYPE,
                        PICASA_FEATURES);
            }
            else if (YOUTUBE_AUTH_TOKEN_TYPE.equals(authTokenType)) {
                return new GlsAuthorizer(context, YOUTUBE_AUTH_TOKEN_TYPE,
                        YOUTUBE_FEATURES);
            }
            else if (PLUS_AUTH_TOKEN_TYPE.equals(authTokenType)) {
                return new GlsAuthorizer(context, PLUS_AUTH_TOKEN_TYPE,
                        PLUS_FEATURES);
            }
            else {
                return new GlsAuthorizer(context, authTokenType,
                        new String[] {});
            }
        }
    }
    
    @Override
    public String getAuthToken(String accountName) {
        Log.d(Config.APP_NAME, "Getting " + authTokenType + " authToken for "
                + accountName);
        Account account = getAccount(accountName);
        if (account != null) {
            try {
                return accountManager.blockingGetAuthToken(account,
                        authTokenType, true);
            } catch (OperationCanceledException e) {
                Log.w(Config.APP_NAME, e);
            } catch (IOException e) {
                Log.w(Config.APP_NAME, e);
            } catch (AuthenticatorException e) {
                Log.w(Config.APP_NAME, e);
            }
        }
        return null;
    }
    
    @Override
    public String getFreshAuthToken(String accountName, String authToken) {
        Log.d(Config.APP_NAME, "Refreshing authToken for " + accountName);
        accountManager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
        return getAuthToken(accountName);
    }
    
    @Override
    public void fetchAuthToken(final String accountName, Activity activity,
            final AuthorizationListener<String> listener) {
        Account account = getAccount(accountName);
        if (account != null) {
            accountManager.getAuthToken(account, authTokenType, null, // loginOptions,
                    activity, new AccountManagerCallback<Bundle>() {
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Log.d(Config.APP_NAME, "Got authToken for "
                                        + accountName);
                                Bundle extras = future.getResult();
                                String authToken = extras
                                        .getString(AccountManager.KEY_AUTHTOKEN);
                                listener.onSuccess(authToken);
                            } catch (OperationCanceledException e) {
                                listener.onCanceled();
                            } catch (IOException e) {
                                listener.onError(e);
                            } catch (AuthenticatorException e) {
                                listener.onError(e);
                            }
                        }
                    }, null); // handler
        }
        else {
            listener.onError(new Exception("Could not find account "
                    + accountName));
        }
    }
    
    @Override
    public void fetchAccounts(final AuthorizationListener<String[]> listener) {
        accountManager.getAccountsByTypeAndFeatures(ACCOUNT_TYPE, features,
                new AccountManagerCallback<Account[]>() {
                    public void run(AccountManagerFuture<Account[]> future) {
                        try {
                            Account[] accounts = future.getResult();
                            String[] accountNames = new String[accounts.length];
                            for (int i = 0; i < accounts.length; i++) {
                                accountNames[i] = accounts[i].name;
                            }
                            Log.d(Config.APP_NAME, "Got " + accounts.length
                                    + " accounts");
                            listener.onSuccess(accountNames);
                        } catch (OperationCanceledException e) {
                            listener.onCanceled();
                        } catch (IOException e) {
                            listener.onError(e);
                        } catch (AuthenticatorException e) {
                            listener.onError(e);
                        }
                    }
                }, null); // handler
    }
    
    private Account getAccount(String name) {
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(name)) {
                return account;
            }
        }
        return null;
    }
    
    @Override
    public void addAccount(Activity activity,
            final AuthorizationListener<String> listener) {
        accountManager.addAccount(ACCOUNT_TYPE, authTokenType, features, null, // addAccountOptions,
                activity, new AccountManagerCallback<Bundle>() {
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle extras = future.getResult();
                            String accountName = extras
                                    .getString(AccountManager.KEY_ACCOUNT_NAME);
                            Log.d(Config.APP_NAME, "Added account "
                                    + accountName);
                            listener.onSuccess(accountName);
                        } catch (OperationCanceledException e) {
                            listener.onCanceled();
                        } catch (IOException e) {
                            listener.onError(e);
                        } catch (AuthenticatorException e) {
                            listener.onError(e);
                        }
                    }
                }, null); // handler
    }
    
    @Override
    public void clearAuthToken(String accountType, String authToken) {
        accountManager.invalidateAuthToken(accountType, authToken);
    }
    
}
