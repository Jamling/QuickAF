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

import com.google.gson.Gson;

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

    public void loadNews(NewsRequest req, boolean needCache) {
        ListTask task = new ListTask();
        task.load2List(req, NewsInfo.class, needCache);
    }

    public interface NewsListener {
        void onLoadNewsSuccess(List<NewsInfo> out, boolean fromCache);

        void onLoadNewsFailure(RestError error);
    }

    public static class NewsRequest implements java.io.Serializable {
        public int num = 10;
        public int page = 1;
        public int rand = 1;
        public String word;
        public String src;
    }

    public static class NewsResponse implements java.io.Serializable, IBaseResponse {

        public int code;
        public String msg;
        public List<NewsInfo> newslist;

        @Override

        public String getData() {
            return new Gson().toJson(newslist);
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
    }

    private class ListTask extends RequestObjectTask<NewsRequest, List<NewsInfo>> {

        @Override
        public IUrl getUrl() {
            return new URLConst.AbsoluteUrl("http://apis.baidu.com/txapi/weixin/wxhot").get();
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
                    throw new LogicError(null, String.valueOf(resp.code), resp.msg);
                }
            }
            return false;
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            GsonRequest request = super.buildRequest(url, body);
            request.addHeader("apikey", "e8c043231152d9cbcf30a648382ca4c5");
            return request;
        }
    }
}
