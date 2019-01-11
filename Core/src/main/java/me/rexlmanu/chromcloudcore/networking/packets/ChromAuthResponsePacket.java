package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;

@NoArgsConstructor
public final class ChromAuthResponsePacket extends Packet {

    @Getter
    private AuthType authType;

    public ChromAuthResponsePacket(AuthType authType) {
        this.authType = authType;
        setJsonElement(new JsonPrimitive(authType.toString().toLowerCase()));
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        this.authType = AuthType.valueOf(jsonElement.getAsString().toUpperCase());
    }
}
