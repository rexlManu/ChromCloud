package me.rexlmanu.chromcloudcore.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults.ChromChannelSender;

@AllArgsConstructor
@Data
public final class Wrapper {

    private ChromChannelSender chromChannelSender;
}
