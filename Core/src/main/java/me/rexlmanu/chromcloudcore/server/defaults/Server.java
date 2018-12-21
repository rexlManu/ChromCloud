package me.rexlmanu.chromcloudcore.server.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Server {

    private int id;
    private boolean online;
    private long startedAt;
    private List<String> logs;
    private String jarName;

}
