/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.volley.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * 当字段返回null，定义默认的string返回值为""
 *
 * @author wangjian
 * @date 2016/8/8.
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
