/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.demo.volley.weather;

import java.util.List;

import cn.ieclipse.af.demo.volley.AppController;
import cn.ieclipse.af.demo.volley.URLConst;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月16日
 */
public class WeatherController extends AppController<WeatherController.WeatherListener> {

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
            return URLConst.CITY_LIST;
        }
        
        @Override
        public void onSuccess(List<CityInfo> out, boolean fromCache) {
            mListener.onLoadCityListSuccess(out, fromCache);
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLoadCityListFailure(error);
        }
    }
    
    private class CityWeatherTask extends AppBaseTask<BaseRequest, WeatherInfo> {

        @Override
        public IUrl getUrl() {
            return URLConst.CITY_NAME;
        }
        
        @Override
        public void onSuccess(WeatherInfo out, boolean fromCache) {
            mListener.onLoadWeatherSuccess(out, fromCache);
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLoadWeatherError(error);
        }
    }
    
    public interface WeatherListener {
        void onLoadCityListSuccess(List<CityInfo> out, boolean fromCache);
        
        void onLoadCityListFailure(RestError error);
        
        void onLoadWeatherSuccess(WeatherInfo out, boolean fromCache);
        
        void onLoadWeatherError(RestError error);
    }
}
