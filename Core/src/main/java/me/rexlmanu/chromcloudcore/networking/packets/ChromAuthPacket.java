package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

@NoArgsConstructor
public final class ChromAuthPacket extends Packet {

    @Getter
    private String authToken;

    public ChromAuthPacket(String authToken) {
        this.authToken = authToken;
        setJsonElement(new JsonPrimitive(this.authToken));
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        this.authToken = jsonElement.getAsString();
    }
}
