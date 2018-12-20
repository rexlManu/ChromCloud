package me.rexlmanu.chromcloudnode;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.configuration.DefaultConfig;
import me.rexlmanu.chromcloudnode.database.DatabaseManager;
import me.rexlmanu.chromcloudnode.networking.reader.NodePacketReader;
import me.rexlmanu.chromcloudnode.networking.server.NettyServer;

import java.util.List;
import java.util.logging.Level;

@Getter
public final class ChromCloudNode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudNode instance;

    private ChromLogger chromLogger;
    private List<Wrapper> wrappers;
    private DefaultConfig defaultConfig;
    private NettyServer nettyServer;
    private DatabaseManager databaseManager;

    public ChromCloudNode() {
        this.wrappers = Lists.newArrayList();
    }

    @Override
    public void onStart() {
        ChromCloudNode.instance = this;
        this.defaultConfig = new DefaultConfig(ChromLogger.getConsoleReader());
        this.nettyServer = new NettyServer();
        this.databaseManager = new DatabaseManager();

        try {
            this.defaultConfig.init();
            registerReaders();
            if (this.defaultConfig.getMySqlUsername().equals("root")) {
                this.chromLogger.doLog(Level.SEVERE, "Please dont use the root user for the database.");
                return;
            }
            this.databaseManager.init(this.defaultConfig);
            if (this.databaseManager.getMySQL() == null || !this.databaseManager.getMySQL().isConnected()) {
                this.chromLogger.doLog(Level.SEVERE, "The database connection failed.");
                return;
            }
            this.nettyServer.init(this.defaultConfig.getSocketIp(), this.defaultConfig.getSocketPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerReaders() {
        PacketRegistry.registerReader(new NodePacketReader());
    }

    @Override
    public void onStop() {
        ChromCloudNode.instance = null;
    }

    @Override
    public void setLogger(ChromLogger chromLogger) {
        this.chromLogger = chromLogger;
    }

    public Wrapper getWrapperByChannel(io.netty.channel.Channel channel) {
        return this.wrappers.stream().filter(wrapper -> wrapper.getChromChannelSender().getChannel().equals(channel)).findFirst().orElse(null);
    }
}
