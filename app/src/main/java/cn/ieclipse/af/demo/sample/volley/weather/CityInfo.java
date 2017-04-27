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

import java.io.Serializable;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月16日
 */
public class CityInfo implements Serializable {

    /**
     * province_cn : 江苏
     * district_cn : 南京
     * name_cn : 南京
     * name_en : nanjing
     * area_id : 101190101
     */

    public String province_cn;
    public String district_cn;
    public String name_cn;
    public String name_en;
    public String area_id;
}
