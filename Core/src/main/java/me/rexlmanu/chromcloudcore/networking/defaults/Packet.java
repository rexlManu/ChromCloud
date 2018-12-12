package me.rexlmanu.chromcloudcore.networking.defaults;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Packet {

    private JsonElement jsonElement;

}
