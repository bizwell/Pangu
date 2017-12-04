package com.joindata.inf.zipkin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtils {

    public static final Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * obj转json
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    /**
     * json转obj
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
