package me.rexlmanu.chromcloudcore.server.defaults;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public final class Server {

    private int id;
    private boolean online;
    private long startedAt;
    private List<String> logs;

    private Version version;
    private ServerConfiguration serverConfiguration;

    public Server(int id, Version version, ServerConfiguration serverConfiguration) {
        this.id = id;
        this.version = version;
        this.serverConfiguration = serverConfiguration;
        this.online = false;
        this.startedAt = 0;
        this.logs = Lists.newArrayList();
    }
}
