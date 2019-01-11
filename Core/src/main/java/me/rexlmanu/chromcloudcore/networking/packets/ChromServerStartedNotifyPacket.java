package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;

@NoArgsConstructor
public final class ChromServerStartedNotifyPacket extends Packet {

    @Getter
    private Server server;

    public ChromServerStartedNotifyPacket(Server server) {
        this.server = server;
        super.setJsonElement(ArrayUtils.convertObjectToJson(server));
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        this.server = ArrayUtils.convertJsonToObject(jsonElement, Server.class);
    }

}
