/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
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


