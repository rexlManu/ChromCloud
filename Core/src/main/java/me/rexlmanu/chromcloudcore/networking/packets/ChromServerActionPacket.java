package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

@NoArgsConstructor
public final class ChromServerActionPacket extends Packet {

    @Getter
    private int serverId;
    @Getter
    private String action;

    public ChromServerActionPacket(int serverId, String action) {
        this.serverId = serverId;
        this.action = action;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serverId", this.serverId);
        jsonObject.addProperty("action", this.action);
        super.setJsonElement(jsonObject);
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.serverId = jsonObject.get("serverId").getAsInt();
        this.action = jsonObject.get("action").getAsString();
    }
}
