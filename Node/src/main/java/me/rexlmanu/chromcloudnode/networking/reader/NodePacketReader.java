package me.rexlmanu.chromcloudnode.networking.reader;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.networking.packets.*;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.util.Objects;
import java.util.logging.Level;

public final class NodePacketReader implements PacketRegistry.PacketReader {

    @Override
    public void onPacket(Packet packet, ChannelHandlerContext channelHandlerContext) {
        final Channel channel = channelHandlerContext.channel();
        final Wrapper wrapper = ChromCloudNode.getInstance().getWrapperByChannel(channel);
        ChromCloudNode.getInstance().getChromLogger().doLog(Level.ALL, "New packet registed: " + packet.getClass().getSimpleName());
        if (wrapper == null) {
            disconnectUnknownWrapper(channelHandlerContext);
            return;
        }
        if (wrapper.getChromChannelSender().getAuthType().equals(AuthType.DIENED)) {
            disconnectDeniedWrapper(channelHandlerContext);
            return;
        }

        if (wrapper.getChromChannelSender().getAuthType().equals(AuthType.PENDING)) {
            if (packet instanceof ChromAuthPacket) {
                final String authToken = ((ChromAuthPacket) packet).getAuthToken();
                if (ChromCloudNode.getInstance().getDefaultConfig().getAuthToken().equals(authToken)) {
                    wrapper.getChromChannelSender().setAuthType(AuthType.SUCCESS);
                    ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Success authentication from " + channelHandlerContext.channel().remoteAddress().toString() + ".");
                } else {
                    wrapper.getChromChannelSender().setAuthType(AuthType.DIENED);
                    ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "Failed authentication from " + channelHandlerContext.channel().remoteAddress().toString() + ".");
                }
                wrapper.getChromChannelSender().sendPacket(new ChromAuthResponsePacket(wrapper.getChromChannelSender().getAuthType()));
            }
            return;
        }

        if (packet instanceof ChromServerStartedNotifyPacket) {
            final ChromServerStartedNotifyPacket chromServerStartedNotifyPacket = (ChromServerStartedNotifyPacket) packet;
            wrapper.registerServer(chromServerStartedNotifyPacket.getServer());
        } else if (packet instanceof ChromServerLogUpdatePacket) {
            final ChromServerLogUpdatePacket chromServerLogUpdatePacket = (ChromServerLogUpdatePacket) packet;
            final Server server = ChromCloudNode.getInstance().getServerManager().getServerById(chromServerLogUpdatePacket.getServerId());
            if (!Objects.isNull(server))
                server.setLogs(chromServerLogUpdatePacket.getLines());
        } else if (packet instanceof ChromWrapperUpdatePacket) {
            final ChromWrapperUpdatePacket chromWrapperUpdatePacket = (ChromWrapperUpdatePacket) packet;
            wrapper.setWebPort(chromWrapperUpdatePacket.getWebPort());
            wrapper.setToken(chromWrapperUpdatePacket.getToken());
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "HttpServer Information arrived from " + channelHandlerContext.channel().remoteAddress().toString() + ".");
        }
    }

    private void disconnectUnknownWrapper(ChannelHandlerContext channelHandlerContext) {
        ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "Packet from unknown channel " + channelHandlerContext.channel().remoteAddress().toString() + ".");
        channelHandlerContext.channel().close().addListener(ChannelFutureListener.CLOSE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    private void disconnectDeniedWrapper(ChannelHandlerContext channelHandlerContext) {
        ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "Packet from denied channel " + channelHandlerContext.channel().remoteAddress().toString() + ".");
        channelHandlerContext.channel().close().addListener(ChannelFutureListener.CLOSE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
