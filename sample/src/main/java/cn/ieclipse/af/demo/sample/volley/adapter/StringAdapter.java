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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * 当字段返回null，定义默认的string返回值为""
 *
 * @author wangjian
 * @date 2016-08-08.
 */
public class StringAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader reader) {
        String text = "";
        try {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                text = "";//原先是返回Null，这里改为返回空字符串
            }
            else {
                text = reader.nextString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void write(JsonWriter writer, String value) {
        try {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
