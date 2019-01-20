package me.rexlmanu.chromcloudsubnode;

import lombok.Getter;
import lombok.Setter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.commands.CommandManager;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults.ChromChannelSender;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudsubnode.configuration.DefaultConfig;
import me.rexlmanu.chromcloudsubnode.networking.client.NettyClient;
import me.rexlmanu.chromcloudsubnode.networking.reader.SubnodePacketReader;
import me.rexlmanu.chromcloudsubnode.server.ServerManager;
import me.rexlmanu.chromcloudsubnode.server.ftp.FTPManager;
import me.rexlmanu.chromcloudsubnode.server.queue.QueueManager;
import me.rexlmanu.chromcloudsubnode.server.version.VersionManager;
import me.rexlmanu.chromcloudsubnode.web.WebManager;

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

    private ServerManager serverManager;
    private QueueManager queueManager;
    private VersionManager versionManager;
    private WebManager webManager;
    private FTPManager ftpManager;
    @Setter
    private ChromChannelSender chromChannelSender;

    public ChromCloudSubnode() {
        this.connected = false;
    }

    @Override
    public void onStart() {
        ChromCloudSubnode.instance = this;

        this.defaultConfig = new DefaultConfig(ChromLogger.getConsoleReader());
        this.nettyClient = new NettyClient();
        this.serverManager = new ServerManager();
        this.queueManager = new QueueManager();
        this.versionManager = new VersionManager();
        this.webManager = new WebManager();
        this.ftpManager = new FTPManager();

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

            this.serverManager.init();
            this.queueManager.init();
            this.webManager.init();
            this.ftpManager.init();
        } catch (IOException e) {
        }
    }

    private void registerReader() {
        PacketRegistry.registerReader(new SubnodePacketReader());
    }

    @Override
    public void onStop() {
        this.ftpManager.onStop();
        ChromCloudSubnode.instance = null;
    }

    @Override
    public void setLogger(ChromLogger chromLogger) {
        this.chromLogger = chromLogger;
    }
}
