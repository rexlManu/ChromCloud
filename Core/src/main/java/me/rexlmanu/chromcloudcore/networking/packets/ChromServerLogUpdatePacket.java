package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;

import java.util.List;

@NoArgsConstructor
public final class ChromServerLogUpdatePacket extends Packet {

    @Getter
    private List<String> lines;
    @Getter
    private int serverId;

    public ChromServerLogUpdatePacket(List<String> lines, int serverId) {
        this.lines = lines;
        this.serverId = serverId;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("lines", ArrayUtils.toJsonArray(ArrayUtils.shortList(this.lines, 30)));
        jsonObject.addProperty("serverId", this.serverId);
        super.setJsonElement(jsonObject);
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.lines = ArrayUtils.fromJsonArray(jsonObject.get("lines").getAsJsonArray());
        this.serverId = jsonObject.get("serverId").getAsInt();
    }
}
