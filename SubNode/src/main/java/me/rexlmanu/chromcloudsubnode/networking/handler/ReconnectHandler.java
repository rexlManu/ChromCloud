package me.rexlmanu.chromcloudsubnode.networking.handler;

import lombok.Getter;
import lombok.Setter;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.configuration.DefaultConfig;
import me.rexlmanu.chromcloudsubnode.networking.client.NettyClient;

import java.util.logging.Level;

public final class ReconnectHandler implements Runnable {

    public static ReconnectHandler reconnectHandler;

    public static ReconnectHandler getReconnectHandler() {
        if (reconnectHandler == null)
            reconnectHandler = new ReconnectHandler(ChromCloudSubnode.getInstance().getNettyClient());
        return reconnectHandler;
    }

    private NettyClient nettyClient;
    @Getter
    @Setter
    private long timeToSleep;

    private ReconnectHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
        this.timeToSleep = 1000;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!ChromCloudSubnode.getInstance().getNettyClient().isConnected()) {
            final DefaultConfig defaultConfig = ChromCloudSubnode.getInstance().getDefaultConfig();
            this.nettyClient.init(defaultConfig.getSocketIp(), defaultConfig.getSocketPort());
        }
    }

    public void reconnect() {
        this.timeToSleep += 1500;
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Trying to reconnect in " + this.timeToSleep + "ms.");
        run();
    }
}
