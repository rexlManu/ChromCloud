package me.rexlmanu.chromcloudnode.web.managers;

import com.sun.net.httpserver.HttpServer;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.web.handler.WebHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

public final class WebManager implements DefaultManager {

    @Override
    public void init() {
        final int webSocketPort = ChromCloudNode.getInstance().getDefaultConfig().getWebSocketPort();
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(webSocketPort), 0);
            httpServer.createContext("/api/v1", new WebHandler());
            httpServer.setExecutor(null);
            httpServer.start();
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "The webserver started on port " + webSocketPort + ".");
        } catch (IOException e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "The webserver could not start on port " + webSocketPort + ".");
        }
    }

}
