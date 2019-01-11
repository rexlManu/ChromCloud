package me.rexlmanu.chromcloudsubnode.networking.reader;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.networking.packets.*;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.server.handler.ServerHandler;

import java.util.Objects;
import java.util.logging.Level;

public final class SubnodePacketReader implements PacketRegistry.PacketReader {

    @Override
    public void onPacket(Packet packet, ChannelHandlerContext channelHandlerContext) {
        if (packet instanceof ChromAuthResponsePacket) {
            final AuthType authType = ((ChromAuthResponsePacket) packet).getAuthType();
            switch (authType) {
                case DIENED:
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "The authentication is failed.");
                    disconnect(channelHandlerContext);
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.WARNING, "Disconnecting from the node server.");
                    break;
                case SUCCESS:
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "The authentication is successful.");
                    ChromCloudSubnode.getInstance().setConnected(true);
                    ChromCloudSubnode.getInstance().getChromChannelSender().sendPacket(new ChromWrapperUpdatePacket(ChromCloudSubnode.getInstance().getDefaultConfig().getWebPort(), ChromCloudSubnode.getInstance().getWebManager().getToken()));
                    break;
                default:
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.WARNING, "The authentication is pending.");
                    break;
            }
            ChromCloudSubnode.getInstance().getChromChannelSender().setAuthType(authType);
        } else {
            if (!ChromCloudSubnode.getInstance().isConnected()) return;
            if (packet instanceof ChromStartServerPacket) {
                final ChromStartServerPacket chromStartServerPacket = (ChromStartServerPacket) packet;
                final Server server = chromStartServerPacket.getServer();
                ChromCloudSubnode.getInstance().getQueueManager().addToQueue(server);
            } else if (packet instanceof ChromServerSendCommandPacket) {
                final ChromServerSendCommandPacket chromServerSendCommandPacket = (ChromServerSendCommandPacket) packet;
                ChromCloudSubnode.getInstance().getServerManager().getServerHandlerById(chromServerSendCommandPacket.getServerId()).getServerConsole().runCommand(chromServerSendCommandPacket.getCommand(), stringList -> {
                });
            } else if (packet instanceof ChromServerActionPacket) {
                final ChromServerActionPacket chromServerActionPacket = (ChromServerActionPacket) packet;
                final ServerHandler serverHandler = ChromCloudSubnode.getInstance().getServerManager().getServerHandlerById(chromServerActionPacket.getServerId());
                if (Objects.isNull(serverHandler)) return;
                switch (chromServerActionPacket.getAction().toLowerCase()) {
                    case "kill":
                        serverHandler.kill();
                        break;
                    case "stop":
                        serverHandler.stop();
                        break;
                    case "remove":
                        serverHandler.remove();
                        break;
                }
            }
        }
    }

    private void disconnect(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.channel().close().addListener(ChannelFutureListener.CLOSE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
