package me.rexlmanu.chromcloudcore.networking.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;

public final class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        PacketRegistry.getPacketReaders().forEach(packetReader -> packetReader.onPacket(packet, channelHandlerContext));
    }
}
