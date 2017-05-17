/*
 * Copyright (C) 2015-2016 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.af.demo.sample.recycler;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.io.Serializable;
import java.util.List;

import cn.ieclipse.af.demo.common.api.LogicError;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.Controller;
import cn.ieclipse.af.volley.GsonRequest;
import cn.ieclipse.af.volley.IBaseResponse;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * 获取新闻控制器
 *
 * @author Jamling
 */
public class NewsController extends Controller<NewsController.NewsListener> {

    public NewsController(NewsListener listener) {
        setListener(listener);
    }

    private boolean lazy;

    public void setLazyLoad(boolean lazy) {
        this.lazy = lazy;
    }

    public void loadNews(NewsRequest req, boolean needCache) {
        ListTask task = new ListTask();
        // task.load2List(req, NewsInfo.class, needCache);
        // since 2.1.0 use new API
        task.load(req, needCache);
    }

    public interface NewsListener {
        void onLoadNewsSuccess(List<NewsInfo> out, boolean fromCache);

        void onLoadNewsFailure(RestError error);
    }

    public static class NewsRequest implements java.io.Serializable {
        public String key = "f1d4f487a1d5a7e0bf12382c24301d3a";
        public int num = 10;
        public int page = 1;
        public int rand = 1;
        public String word;
        public String src;
    }

    public static class NewsResponse implements IBaseResponse<List<NewsInfo>> {
        // success
        public int code;
        public String msg;
        public List<NewsInfo> newslist;

        // failure
        public int errNum;
        public String errMsg;

        @Override
        public List<NewsInfo> getData() {
            return newslist;
        }
    }

    public static class NewsInfo implements Serializable {

        /**
         * ctime : 2016-03-31
         * title : 奇虎360宣布通过私有化决议
         * description : 互联网头条
         * picUrl : http://t1.qpic.cn/mblogpic/f01a972dbcc1060fd456/2000
         * url : http://mp.weixin.qq
         * .com/s?__biz=MjM5OTMyODA2MA==&idx=1&mid=402594468&sn=5cd644536b472a283cc1d3f5124a0cab
         */

        public String ctime;
        public String title;
        public String description;
        public String picUrl;
        public String url;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NewsInfo) {
                return title.equals(((NewsInfo) obj).title);
            }
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private class ListTask extends RequestObjectTask<NewsRequest, List<NewsInfo>> {

        @Override
        public IUrl getUrl() {
            //return new URLConst.AbsoluteUrl("http://apis.baidu.com/txapi/weixin/wxhot").get();
            return new URLConst.AbsoluteUrl("http://api.huceo.com/wxnew").get();
        }

        @Override
        public void onSuccess(List<NewsInfo> out, boolean fromCache) {
            mListener.onLoadNewsSuccess(out, fromCache);
        }

        @Override
        public void onError(RestError error) {
            mListener.onLoadNewsFailure(error);
        }

        @Override
        public Class<? extends IBaseResponse> getBaseResponseClass() {
            return NewsResponse.class;
        }

        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (response instanceof NewsResponse) {
                NewsResponse resp = (NewsResponse) response;
                if (resp.code != 200) {
                    throw new LogicError(null, resp.errNum, resp.errMsg);
                }
            }
            return false;
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            GsonRequest request = new MyGsonRequest(url.getMethod(), url.getUrl(), body, this, this);
            //request.addHeader("apikey", "e8c043231152d9cbcf30a648382ca4c5");
            return request;
        }
    }

    private class MyGsonRequest extends GsonRequest {

        public MyGsonRequest(int method, String url, String body, Response.Listener<IBaseResponse> responseListener,
                             Response.ErrorListener listener) {
            super(method, url, body, responseListener, listener);
        }

        @Override
        protected Response<IBaseResponse> parseNetworkResponse(NetworkResponse response) {
            if (lazy) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
            }
            return super.parseNetworkResponse(response);
        }
    }
}
