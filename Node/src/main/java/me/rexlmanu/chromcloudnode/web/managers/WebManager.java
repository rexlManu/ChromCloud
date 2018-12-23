package me.rexlmanu.chromcloudnode.web.managers;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

public final class WebManager implements DefaultManager {

    private HttpServer httpServer;

    public WebManager() {

    }

    @Override
    public void init() {
        final int webSocketPort = ChromCloudNode.getInstance().getDefaultConfig().getWebSocketPort();
        try {
            this.httpServer = HttpsServer.create(new InetSocketAddress(webSocketPort), 1);
            this.httpServer.setExecutor(null);
            this.httpServer.start();
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "The webserver started on port " + webSocketPort + ".");
        } catch (IOException e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "The webserver could not start on port " + webSocketPort + ".");
        }
    }
}
