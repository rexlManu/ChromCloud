package me.rexlmanu.chromcloudcore.commands.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rexlmanu.chromcloudcore.commands.response.callback.JsonCallback;
import me.rexlmanu.chromcloudcore.utility.java.Builder;

public final class Response implements Builder<JsonObject> {

    public static Response create() {
        return new Response();
    }

    private JsonObject jsonObject;

    private Response() {
        this.jsonObject = new JsonObject();
    }

    public Response empty() {
        return this;
    }

    public Response message(String message) {
        this.jsonObject.addProperty("message", message);
        return this;
    }

    public Response json(String key, JsonElement jsonElement) {
        this.jsonObject.add(key, jsonElement);
        return this;
    }

    public Response error(String errorMessage) {
        if (!this.jsonObject.has("error"))
            this.jsonObject.add("error", new JsonArray());
        final JsonArray error = this.jsonObject.getAsJsonArray("error");
        error.add(errorMessage);
        return this;
    }

    public Response jsonCallback(String key, JsonCallback jsonCallback){
        this.json(key, jsonCallback.callback());
        return this;
    }

    @Override
    public JsonObject build() {
        return this.jsonObject;
    }
}
