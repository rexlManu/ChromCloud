package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;

@Getter
@NoArgsConstructor
public final class ChromStartServerPacket extends Packet {

    private Server server;

    public ChromStartServerPacket(Server server) {
        super.setJsonElement(ArrayUtils.convertObjectToJson(server));
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        this.server = ArrayUtils.convertJsonToObject(jsonElement, Server.class);
    }
}
