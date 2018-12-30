package me.rexlmanu.chromcloudnode;

import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.commands.CommandManager;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.configuration.DefaultConfig;
import me.rexlmanu.chromcloudnode.configuration.UserConfiguration;
import me.rexlmanu.chromcloudnode.database.DatabaseManager;
import me.rexlmanu.chromcloudnode.networking.reader.NodePacketReader;
import me.rexlmanu.chromcloudnode.networking.server.NettyServer;
import me.rexlmanu.chromcloudnode.server.ServerManager;
import me.rexlmanu.chromcloudnode.web.managers.UserManager;
import me.rexlmanu.chromcloudnode.web.managers.WebManager;
import me.rexlmanu.chromcloudnode.wrapper.WrapperManager;

import java.util.logging.Level;

@Getter
public final class ChromCloudNode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudNode instance;

    private ChromLogger chromLogger;
    private DefaultConfig defaultConfig;
    private NettyServer nettyServer;
    private DatabaseManager databaseManager;
    private UserConfiguration userConfiguration;
    private UserManager userManager;
    private WebManager webManager;
    private ServerManager serverManager;
    private WrapperManager wrapperManager;

    @Override
    public void onStart() {
        ChromCloudNode.instance = this;
        this.defaultConfig = new DefaultConfig(ChromLogger.getConsoleReader());
        this.nettyServer = new NettyServer();
        this.databaseManager = new DatabaseManager();
        this.userConfiguration = new UserConfiguration();
        this.userManager = new UserManager();
        this.webManager = new WebManager();
        this.serverManager = new ServerManager();
        this.wrapperManager = new WrapperManager();

        try {
            this.defaultConfig.init();
            registerReaders();
            this.databaseManager.init(this.defaultConfig);
            if (this.databaseManager.getMySQL() == null || !this.databaseManager.getMySQL().isConnected()) {
                this.chromLogger.doLog(Level.SEVERE, "The database connection failed.");
                return;
            }
            this.createTables();
            this.userConfiguration.init();
            CommandManager.init();
            CommandManager.setDefaultPrompt("Node");
            this.userManager.init();
            this.nettyServer.init(this.defaultConfig.getSocketIp(), this.defaultConfig.getSocketPort());
            this.webManager.init();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        this.databaseManager.update("CREATE TABLE IF NOT EXISTS `servers` ( `id` INT NOT NULL AUTO_INCREMENT , `version` VARCHAR(255) NOT NULL DEFAULT 'spigot1.8.8' , `max_players` INT NOT NULL DEFAULT '10' , `motd` VARCHAR(255) NOT NULL DEFAULT 'ChromCloud hosted Gameserver' , `mode` ENUM('performance','time','custom','') NOT NULL DEFAULT 'time' , `ram` INT NOT NULL DEFAULT '512' , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
        this.databaseManager.update("CREATE TABLE IF NOT EXISTS `versions` ( `id` INT NOT NULL AUTO_INCREMENT , `jar_name` VARCHAR(255) NOT NULL , `jar_download` VARCHAR(255) NOT NULL , `ftb_modpack` BOOLEAN NOT NULL DEFAULT FALSE , `legacyjavafixer` BOOLEAN NOT NULL DEFAULT FALSE , `version` VARCHAR(255) NOT NULL , `type` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
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
        return this.wrapperManager.getWrappers().stream().filter(wrapper -> wrapper.getChromChannelSender().getChannel().equals(channel)).findFirst().orElse(null);
    }
}
