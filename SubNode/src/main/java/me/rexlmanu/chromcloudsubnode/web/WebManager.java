package me.rexlmanu.chromcloudsubnode.web;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.utility.random.RandomUtils;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.web.handler.LogHandler;
import me.rexlmanu.chromcloudsubnode.web.handler.WebBackupHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class WebManager implements DefaultManager {

    private HttpServer httpServer;
    @Getter
    private String token;

    public WebManager() {
    }

    @Override
    public void init() {
        this.token = RandomUtils.generateToken(32);
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(ChromCloudSubnode.getInstance().getDefaultConfig().getWebPort()), 0);
            this.httpServer.createContext("/log/", new LogHandler());
            this.httpServer.createContext("/file/", new WebBackupHandler());
            this.httpServer.setExecutor(null);
            this.httpServer.start();
        } catch (IOException ignored) {
        }
    }
}
