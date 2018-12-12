package me.rexlmanu.chromcloudcore.networking.defaults.sender;

import io.netty.channel.ChannelHandlerContext;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

public interface ChromSender {

    ChannelHandlerContext getChannelHandlerContext();

    void sendPacket(Packet packet);
}
