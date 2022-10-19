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

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.volley.RestError;
import cn.ieclipse.af.volley.VolleyConfig;
import cn.ieclipse.af.volley.VolleyManager;

/**
 * 使用Volley进行网络请求示例。
 * 本类实现的WeatherController的WeatherListener接口
 *
 * @author Jamling
 */
public class WeatherActivity extends BaseActivity implements WeatherController.WeatherListener {

    TextView tv;
    EditText et;
    Spinner spn;
    CityAdapter adapter;
    WeatherController controller;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_volley_weather;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("Weather Sample");
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        // typically, you just config volley in Application.onCreate
        VolleyConfig config = new VolleyConfig.Builder().setBaseResponseClass(WeatherBaseResponse.class).build();
        VolleyManager.init(getApplicationContext(), config);
        spn = (Spinner) findViewById(R.id.spn1);
        adapter = new CityAdapter();
        spn.setAdapter(adapter);
        et = (EditText) findViewById(R.id.et_text);
        tv = (TextView) findViewById(R.id.tv);
    }

    @Override
    protected void initData() {
        super.initData();
        controller = new WeatherController(this);
        String name = et.getText().toString();
        if (TextUtils.isEmpty(name)) {
            name = et.getHint().toString();
        }
        loadCityList(name, true);
    }

    /**
     * 获取城市列表
     * @param name
     * @param needCache
     */
    public void loadCityList(String name, boolean needCache) {
        BaseRequest req = new BaseRequest();
        req.cityname = name;
        controller.loadCityList(req, needCache);
    }

    /**
     * 获取城市天气详情
     */
    public void loadWeather() {
        BaseRequest req = new BaseRequest();
        CityInfo city = (CityInfo)spn.getSelectedItem();
        if (city != null) {
            req.cityname = city.name_cn;
            controller.loadWeather(req);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            String name = et.getText().toString();
            if (TextUtils.isEmpty(name)) {
                name = et.getHint().toString();
            }
            loadCityList(name, false);
        }
        else if (v.getId() == R.id.btn2) {
            loadWeather();
        }
        super.onClick(v);
    }

    @Override
    public void onLoadCityListSuccess(List<CityInfo> out, boolean fromCache) {
        adapter.setDataList(out);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadCityListFailure(RestError error) {
        VolleyUtils.showError(tv, error);
    }

    @Override
    public void onLoadWeatherSuccess(WeatherInfo out, boolean fromCache) {
//        String msg = String.format("city:%s\ntemp:%sC (%s - %s)\nwind:%s(%s)", out.city, out.temp, out.l_tmp, out.h_tmp,
//            out.WD, out.WS);
//        tv.setText(msg);
    }

    @Override
    public void onLoadWeatherError(RestError error) {
        VolleyUtils.showError(tv, error);
    }

    public static Intent go(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
        return intent;
    }

    private class CityAdapter extends AfBaseAdapter<CityInfo> {

        @Override
        public int getLayout() {
            return android.R.layout.simple_dropdown_item_1line;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).name_cn);
        }
    }
}
