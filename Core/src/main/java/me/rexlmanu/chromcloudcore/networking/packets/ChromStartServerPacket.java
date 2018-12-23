package me.rexlmanu.chromcloudcore.networking.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.server.defaults.JarFile;
import me.rexlmanu.chromcloudcore.server.defaults.Server;

public final class ChromStartServerPacket extends Packet {

    @Getter
    private int id;
    private JarFile jarFile;


    public ChromStartServerPacket(Server server) {
        super.setJsonElement(new JsonPrimitive(server.toString().toLowerCase()));
    }

    @Override
    public void setJsonElement(JsonElement jsonElement) {
        super.setJsonElement(jsonElement);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
    }
}
