package me.rexlmanu.chromcloudsubnode.networking.reader;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.networking.packets.ChromAuthResponsePacket;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

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
                    break;
                default:
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.WARNING, "The authentication is pending.");
                    break;
            }
        }
    }

    private void disconnect(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.channel().close().addListener(ChannelFutureListener.CLOSE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

    }
}
