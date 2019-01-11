package me.rexlmanu.chromcloudsubnode.server.queue;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.async.AsyncSession;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class QueueManager implements DefaultManager {

    @Getter
    private List<Server> serverQueue;

    private long timeout;

    public QueueManager() {
        this.serverQueue = Lists.newArrayList();
        this.timeout = System.currentTimeMillis();
    }

    @Override
    public void init() {
        AsyncSession.getInstance().scheduleAsync(() -> {
            if (serverQueue.isEmpty()) return;
            if (this.timeout >= System.currentTimeMillis()) return;

            final long timeToStart = System.currentTimeMillis();

            final Server queuedServer = serverQueue.get(0);
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "The server with the id " + queuedServer.getId() + " is processing.");
            if (ChromCloudSubnode.getInstance().getServerManager().startServer(queuedServer)) {
                this.serverQueue.remove(queuedServer);
            } else {
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "The server with the id " + queuedServer.getId() + " failed to start in the queue.");
            }
            this.timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10) + (System.currentTimeMillis() - timeToStart);

        }, 1000L, 1000L, TimeUnit.MILLISECONDS);
    }

    public void addToQueue(Server server) {
        this.serverQueue.add(server);
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "The server with the id " + server.getId() + " is in queue.");
    }
}
