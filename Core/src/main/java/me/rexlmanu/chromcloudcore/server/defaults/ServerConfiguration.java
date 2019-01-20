package me.rexlmanu.chromcloudcore.server.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ServerConfiguration {

    private int maxPlayers;
    private String motd;
    private ServerMode serverMode;
    private int ram;
    private int port;
    private String ftpPassword;
    private String lastSubnode;

}
