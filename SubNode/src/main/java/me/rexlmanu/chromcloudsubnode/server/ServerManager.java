package me.rexlmanu.chromcloudsubnode.server;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.networking.packets.ChromServerStartedNotifyPacket;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.server.defaults.ServerConfiguration;
import me.rexlmanu.chromcloudcore.server.defaults.Version;
import me.rexlmanu.chromcloudcore.utility.file.ZipUtils;
import me.rexlmanu.chromcloudcore.utility.string.StringUtils;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.server.handler.ServerHandler;
import me.rexlmanu.docker.builder.DockerCommandBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public final class ServerManager implements DefaultManager {

    @Getter
    private List<ServerHandler> serverHandlers;

    public ServerManager() {
        this.serverHandlers = Lists.newArrayList();
    }

    @Override
    public void init() {
    }

    public boolean startServer(Server server) {
        final ServerHandler serverHandler = new ServerHandler(server);
        final Version version = server.getVersion();
        final ServerConfiguration serverConfiguration = server.getServerConfiguration();
        final boolean serverSetup = serverHandler.serverSetup();
        if (serverSetup) {
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Creating new server with the id " + server.getId() + ".");
            final DockerCommandBuilder builder = new DockerCommandBuilder("mc-" + server.getId()).base().attach().path(serverHandler.getTempDirectory()).eulaAccept(true);
            if (version.isFtbModpack())
                builder.ftbServerModpack(version.getJarDownload());
            if (version.isLegacyJavaFixer())
                builder.legacyJavaFixer(true);
            if (version.getType().toLowerCase().equals("spigot") || version.getType().toLowerCase().equals("bukkit"))
                builder.spigotDownloadUrl(version.getJarDownload());
            serverHandler.setBuildCommand(builder.type(version.getType()).version(version.getVersion()).port(serverConfiguration.getPort()));
        } else {
            serverHandler.getTempDirectory().mkdir();
            if (!serverHandler.getBackupDirectory().exists())
                throw new NullPointerException("Backup Directory is missing...");
            final File[] files = serverHandler.getBackupDirectory().listFiles();
            if (files == null || files.length == 0)
                throw new NullPointerException("Backup is missing...");
            final File backupZip = files[0];
            ZipUtils.extractFolder(backupZip, serverHandler.getTempDirectory().getAbsolutePath());
        }
        final File jarLocation = ChromCloudSubnode.getInstance().getVersionManager().getVersion(version);
        try {
            assert jarLocation != null;
            FileUtils.copyFile(jarLocation, new File(serverHandler.getTempDirectory(), "spigot_server.jar"));
        } catch (IOException ignored) {
        }
        this.serverHandlers.add(serverHandler);
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Server with id " + server.getId() + " started.");
        final File serverProperties = new File(serverHandler.getTempDirectory(), "server.properties");
        try {
            final Properties properties = new Properties();
            if (!serverProperties.exists()) {
                serverProperties.createNewFile();
                properties.load(getClass().getResourceAsStream("/files/server.properties"));
            } else properties.load(new FileInputStream(serverProperties));
            properties.setProperty("server-port", "25565");
            properties.setProperty("max-players", String.valueOf(serverConfiguration.getMaxPlayers()));
            properties.setProperty("rcon.password", "minecraft");
            properties.setProperty("motd", serverConfiguration.getMotd());
            properties.setProperty("enable-rcon", "true");
            properties.store(new FileOutputStream(serverProperties), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverHandler.createServerConsole();
        if (StringUtils.OS_NAME.equalsIgnoreCase("windows"))
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "Please run the application on a linux machine.");
        else if (serverSetup)
            serverHandler.create();
        else
            serverHandler.start();
        serverHandler.getServerConsole().startListen();
        ChromCloudSubnode.getInstance().getChromChannelSender().sendPacket(new ChromServerStartedNotifyPacket(server));
        return true;
    }

    public ServerHandler getServerHandlerById(int serverId) {
        return this.serverHandlers.stream().filter(serverHandler -> serverHandler.getServer().getId() == serverId).findFirst().orElse(null);
    }
}
