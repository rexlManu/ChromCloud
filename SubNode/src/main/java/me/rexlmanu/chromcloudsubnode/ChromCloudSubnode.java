package me.rexlmanu.chromcloudsubnode;

import lombok.Getter;
import lombok.Setter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.commands.CommandManager;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudsubnode.configuration.DefaultConfig;
import me.rexlmanu.chromcloudsubnode.networking.client.NettyClient;
import me.rexlmanu.chromcloudsubnode.networking.reader.SubnodePacketReader;

import java.io.IOException;
import java.util.logging.Level;

@Getter
public final class ChromCloudSubnode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudSubnode instance;

    private ChromLogger chromLogger;
    @Setter
    private boolean connected;

    private DefaultConfig defaultConfig;
    private NettyClient nettyClient;

    public ChromCloudSubnode() {
        this.connected = false;
    }

    @Override
    public void onStart() {
        ChromCloudSubnode.instance = this;

        this.defaultConfig = new DefaultConfig(ChromLogger.getConsoleReader());
        this.nettyClient = new NettyClient();

        try {
            this.defaultConfig.init();
            if (this.defaultConfig.getAuthToken().equals("none")) {
                this.chromLogger.doLog(Level.SEVERE, "Please enter the auth token from the node server.");
                return;
            }
            registerReader();
            this.nettyClient.init(this.defaultConfig.getSocketIp(), this.defaultConfig.getSocketPort());
            CommandManager.init();
            CommandManager.setDefaultPrompt("Subnode");
        } catch (IOException e) {
        }
    }

    private void registerReader() {
        PacketRegistry.registerReader(new SubnodePacketReader());
    }

    @Override
    public void onStop() {
        ChromCloudSubnode.instance = null;
    }

    @Override
    public void setLogger(ChromLogger chromLogger) {
        this.chromLogger = chromLogger;
    }
}
