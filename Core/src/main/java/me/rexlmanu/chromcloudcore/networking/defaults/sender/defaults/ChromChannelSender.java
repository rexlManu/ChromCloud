package me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.ChromSender;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.networking.enums.SenderType;

@Data
public final class ChromChannelSender implements ChromSender {

    private ChannelHandlerContext channelHandlerContext;
    private AuthType authType;
    private SenderType senderType;

    public ChromChannelSender(ChannelHandlerContext channelHandlerContext, SenderType senderType) {
        this.channelHandlerContext = channelHandlerContext;
        this.senderType = senderType;
        this.authType = AuthType.PENDING;
    }

    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }

    @Override
    public void sendPacket(Packet packet) {
        this.channelHandlerContext.channel().writeAndFlush(packet);
    }
}
