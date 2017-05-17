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
package cn.ieclipse.af.demo.sample.volley.weather;

import java.util.List;

import cn.ieclipse.af.demo.common.api.AppController;
import cn.ieclipse.af.demo.common.api.LogicError;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.GsonRequest;
import cn.ieclipse.af.volley.IBaseResponse;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author Jamling
 */
public class WeatherController extends AppController<WeatherController.WeatherListener> {

    public WeatherController(WeatherListener l) {
        super(l);
    }

    public void loadCityList(BaseRequest req, boolean needCache) {
        CityListTask task = new CityListTask();
        task.load2List(req, CityInfo.class, needCache);
    }

    public void loadWeather(BaseRequest req) {
        CityWeatherTask task = new CityWeatherTask();
        task.load(req, WeatherInfo.class, false);
    }
    
    private class CityListTask extends AppBaseTask<BaseRequest, List<CityInfo>> {

        @Override
        public IUrl getUrl() {
            return new URLConst.AbsoluteUrl("http://apis.baidu.com/apistore/weatherservice/citylist").get();
        }
        
        @Override
        public void onSuccess(List<CityInfo> out, boolean fromCache) {
            mListener.onLoadCityListSuccess(out, fromCache);
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLoadCityListFailure(error);
        }

        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (response instanceof WeatherBaseResponse) {
                WeatherBaseResponse resp = (WeatherBaseResponse) response;
                if (resp.errNum != 0) {
                    throw new LogicError(null, resp.errNum, resp.errMsg);
                }
            }
            return false;
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            GsonRequest request = super.buildRequest(url, body);
            request.addHeader("apikey", "e8c043231152d9cbcf30a648382ca4c5");
            return  request;
        }
    }
    
    private class CityWeatherTask extends AppBaseTask<BaseRequest, WeatherInfo> {

        @Override
        public IUrl getUrl() {
            return new URLConst.AbsoluteUrl("http://apis.baidu.com/apistore/weatherservice/cityname").get();
        }
        
        @Override
        public void onSuccess(WeatherInfo out, boolean fromCache) {
            mListener.onLoadWeatherSuccess(out, fromCache);
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLoadWeatherError(error);
        }

        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (response instanceof WeatherBaseResponse) {
                WeatherBaseResponse resp = (WeatherBaseResponse) response;
                if (resp.errNum != 0) {
                    throw new LogicError(null, resp.errNum, resp.errMsg);
                }
            }
            return false;
        }

        @Override
        protected GsonRequest buildRequest(IUrl url, String body) {
            GsonRequest request = super.buildRequest(url, body);
            request.addHeader("apikey", "e8c043231152d9cbcf30a648382ca4c5");
            return  request;
        }
    }
    
    public interface WeatherListener {
        void onLoadCityListSuccess(List<CityInfo> out, boolean fromCache);
        
        void onLoadCityListFailure(RestError error);
        
        void onLoadWeatherSuccess(WeatherInfo out, boolean fromCache);
        
        void onLoadWeatherError(RestError error);
    }
}
