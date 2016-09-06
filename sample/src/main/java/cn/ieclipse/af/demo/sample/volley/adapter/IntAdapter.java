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
package cn.ieclipse.af.demo.sample.volley.adapter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 解决int型数据返回float数据的适配
 *
 * @author wangjian
 * @date 2016/8/8.
 */
public class IntAdapter extends TypeAdapter<Number> {

    @Override
    public Number read(JsonReader in) throws IOException {
        int num = 0;
        // 如果字段返回空，默认为0
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            num = 0;
        }
        else {
            try {
                double input = in.nextDouble();//当成double来读取
                num = (int) input;//强制转为int
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
        return num;
    }

    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }
}


