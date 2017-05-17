package cn.ieclipse.af.volley.mock;

import android.text.TextUtils;

import com.android.volley.Request.Method;

import cn.ieclipse.af.volley.IUrl;

/**
 * Description
 *
 * @author Jamling
 */

public class Url implements IUrl {
    public static String BASE = "";
    protected int method;
    protected String url;
    protected String query;

    public Url(String url) {
        this.url = url;
    }

    public Url get() {
        this.method = Method.GET;
        return this;
    }

    public Url post() {
        this.method = Method.POST;
        return this;
    }

    public Url put() {
        this.method = Method.PUT;
        return this;
    }

    public Url delete() {
        this.method = Method.DELETE;
        return this;
    }

    public String getUrl() {
        return BASE + url + getQuery();
    }

    public int getMethod() {
        return method;
    }

    protected String getQuery() {
        if (TextUtils.isEmpty(query)) {
            return "";
        }
        else if (url.indexOf("?") >= 0) {
            return "&" + query;
        }
        else {
            return "?" + query;
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
