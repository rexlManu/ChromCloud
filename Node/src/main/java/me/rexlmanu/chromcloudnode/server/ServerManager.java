package me.rexlmanu.chromcloudnode.server;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.networking.packets.ChromStartServerPacket;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.database.DatabaseHandler;

import java.util.List;
import java.util.logging.Level;

public final class ServerManager implements DefaultManager {

    @Getter
    private List<Server> servers;

    public ServerManager() {
        this.servers = Lists.newArrayList();
    }

    @Override
    public void init() {

    }

    public boolean startServer(int id) {
        final Server server = DatabaseHandler.getServerById(id);
        if (server == null)
            return false;
        if (server.isOnline())
            return false;
        ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Sending Start packet for server " + server.getId() + " to subnode.");
        ChromCloudNode.getInstance().getWrapperManager().getWrapperWithLowestUse().getChromChannelSender().sendPacket(new ChromStartServerPacket(server));
        return true;
    }

    private Server getServerById(int id) {
        return servers.stream().filter(server -> server.getId() == id).findFirst().orElse(null);
    }

}
