package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

@NoArgsConstructor
public final class ChromServerSendCommandPacket extends Packet {

    @Getter
    private int serverId;
    @Getter
    private String command;

    public ChromServerSendCommandPacket(int serverId, String command) {
        this.serverId = serverId;
        this.command = command;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serverId", this.serverId);
        jsonObject.addProperty("command", this.command);
        super.setJsonElement(jsonObject);
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.serverId = jsonObject.get("serverId").getAsInt();
        this.command = jsonObject.get("command").getAsString();
    }
}
