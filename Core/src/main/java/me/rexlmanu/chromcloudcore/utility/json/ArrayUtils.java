package me.rexlmanu.chromcloudcore.utility.json;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;

import java.util.List;

public final class ArrayUtils {

    public static JsonArray toJsonArray(List<String> list) {
        final JsonArray jsonElements = new JsonArray();
        list.forEach(jsonElements::add);
        return jsonElements;
    }

    public static List<String> fromJsonArray(JsonArray jsonArray) {
        List<String> strings = Lists.newArrayList();
        jsonArray.forEach(jsonElement -> strings.add(jsonArray.getAsString()));
        return strings;
    }

}
