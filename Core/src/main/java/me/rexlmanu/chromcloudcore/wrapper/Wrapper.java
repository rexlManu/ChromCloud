package me.rexlmanu.chromcloudcore.wrapper;

import lombok.Data;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults.ChromChannelSender;
import me.rexlmanu.chromcloudcore.server.defaults.Server;

import java.util.ArrayList;
import java.util.List;

@Data
public final class Wrapper {

    private ChromChannelSender chromChannelSender;
    private List<Server> servers;

    public Wrapper(ChromChannelSender chromChannelSender) {
        this.chromChannelSender = chromChannelSender;
        this.servers = new ArrayList<>();
    }
}
