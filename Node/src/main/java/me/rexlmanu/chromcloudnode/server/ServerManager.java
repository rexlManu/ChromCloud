package me.rexlmanu.chromcloudnode.server;

import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.networking.packets.ChromStartServerPacket;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.database.DatabaseHandler;

import java.util.Optional;
import java.util.logging.Level;

public final class ServerManager implements DefaultManager {

    public ServerManager() {

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

    public Server getServerById(int id) {
        Server server = null;
        for (Wrapper wrapper : ChromCloudNode.getInstance().getWrapperManager().getWrappers()) {
            final Optional<Server> firstServer = wrapper.getServers().stream().filter(tempServer -> tempServer.getId() == id).findFirst();
            if (firstServer.isPresent()) {
                server = firstServer.get();
            }
        }
        return server;
    }

}
