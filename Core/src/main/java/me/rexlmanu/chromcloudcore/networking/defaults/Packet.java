package me.rexlmanu.chromcloudcore.networking.defaults;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Packet {

    private JsonElement jsonElement;

    public Packet() {
    }
}
