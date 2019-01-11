package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

@NoArgsConstructor
public final class ChromWrapperUpdatePacket extends Packet {

    @Getter
    private int webPort;
    @Getter
    private String token;

    public ChromWrapperUpdatePacket(int webPort, String token) {
        this.webPort = webPort;
        this.token = token;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("webPort", this.webPort);
        jsonObject.addProperty("token", this.token);
        super.setJsonElement(jsonObject);
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.webPort = jsonObject.get("webPort").getAsInt();
        this.token = jsonObject.get("token").getAsString();
    }
}
