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

import java.io.Serializable;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月16日
 */
public class WeatherInfo implements Serializable {

    /**
     * city : 南京
     * pinyin : nanjing
     * citycode : 101190101
     * date : 15-11-16
     * time : 08:00
     * postCode : 210000
     * longitude : 118.769
     * latitude : 32.048
     * altitude : 8
     * weather : 阴
     * temp : 18
     * l_tmp : 14
     * h_tmp : 18
     * WD : 东北风
     * WS : 3-4级(10~17m/h)
     * sunrise : 06:34
     * sunset : 17:05
     */

    public String city;
    public String pinyin;
    public String citycode;
    public String date;
    public String time;
    public String postCode;
    public double longitude;
    public double latitude;
    public String altitude;
    public String weather;
    public String temp;
    public String l_tmp;
    public String h_tmp;
    public String WD;
    public String WS;
    public String sunrise;
    public String sunset;
}
