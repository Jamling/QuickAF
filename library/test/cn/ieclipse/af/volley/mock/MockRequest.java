package cn.ieclipse.af.volley.mock;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import cn.ieclipse.af.volley.GsonRequest;
import cn.ieclipse.af.volley.IBaseResponse;

/**
 * Description
 *
 * @author Jamling
 */

public class MockRequest extends GsonRequest {

    private String mockData;

    public MockRequest(int method, String url, String body, Response.Listener<IBaseResponse> responseListener,
                       Response.ErrorListener listener) {
        super(method, url, body, responseListener, listener);
    }

    public void setMockResponse(String responseJson) {
        this.mockData = responseJson;
    }

    @Override
    protected Response<IBaseResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = TextUtils.isEmpty(mockData) ? new String(response.data,
                HttpHeaderParser.parseCharset(response.headers)) : mockData;
            Response<IBaseResponse> ret = Response.success(getData(json.trim(), response),
                parseCacheHeaders(response, ttl));
            this.response = ret;
            return ret;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    public IBaseResponse mockResponse() {
        return getData(mockData, null);
    }
}
