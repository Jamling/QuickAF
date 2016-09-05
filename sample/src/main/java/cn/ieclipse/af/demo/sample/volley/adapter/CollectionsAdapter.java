/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.volley.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 修改CollectionsAdapter，当接口集合返回null，返回默认空数组
 *
 * @author wangjian
 * @date 2016/8/8.
 */
public class CollectionsAdapter implements TypeAdapterFactory {
    
    private final ConstructorConstructor constructorConstructor;
    
    public CollectionsAdapter(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        
        Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }
        
        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
        
        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
            TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter, constructor);
        return result;
    }
    
    private static final class Adapter<E> extends TypeAdapter<Collection<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
        private final ObjectConstructor<? extends Collection<E>> constructor;
        
        public Adapter(Gson context, Type elementType, TypeAdapter<E> elementTypeAdapter,
                       ObjectConstructor<? extends Collection<E>> constructor) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, elementTypeAdapter, elementType);
            this.constructor = constructor;
        }

        @Override
        public Collection<E> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                //这里做了修改，原先是返回null，改为返回空数组
                return constructor.construct();
            }
            
            Collection<E> collection = constructor.construct();
            in.beginArray();
            while (in.hasNext()) {
                E instance = elementTypeAdapter.read(in);
                collection.add(instance);
            }
            in.endArray();
            return collection;
        }

        @Override
        public void write(JsonWriter out, Collection<E> collection) throws IOException {
            if (collection == null) {
                out.nullValue();
                return;
            }
            
            out.beginArray();
            for (E element : collection) {
                elementTypeAdapter.write(out, element);
            }
            out.endArray();
        }
    }
    
}
