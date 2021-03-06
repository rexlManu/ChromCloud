package me.rexlmanu.chromcloudcore.utility.json;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.rexlmanu.chromcloudcore.ChromCloudCore;

import java.util.ArrayList;
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

    public static JsonElement convertObjectToJson(Object object) {
        return ChromCloudCore.PARSER.parse(ChromCloudCore.GSON.toJson(object));
    }

    public static List<String> shortList(List<String> stringList, int shortLength) {
        final ArrayList<String> shortStringList = Lists.newArrayList();
        int finalCount = stringList.size() - shortLength;
        if (finalCount < 0)
            finalCount = 0;
        for (int i = stringList.size() - 1; i >= finalCount; i--) {
            shortStringList.add(stringList.get(i));
        }
        return shortStringList;
    }

    public static <T> T convertJsonToObject(String json, Class<T> classOf) {
        return ChromCloudCore.GSON.fromJson(json, classOf);
    }

    public static <T> T convertJsonToObject(JsonElement jsonElement, Class<T> classOf) {
        return ChromCloudCore.GSON.fromJson(jsonElement, classOf);
    }
}
