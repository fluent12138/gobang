package com.lele.gobang.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author lele
 * @create 2023-01-10 19:17
 */
public class JsonUtils {
    public static JsonElement getJsonElement(Object obj) {
        Gson gson = new Gson();
        return gson.toJsonTree(obj);
    }
}
