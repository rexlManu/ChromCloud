package me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults;

import io.netty.channel.Channel;
import lombok.Data;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.ChromSender;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.networking.enums.SenderType;

@Data
public final class ChromChannelSender implements ChromSender {

    private Channel channel;
    private AuthType authType;
    private SenderType senderType;

    public ChromChannelSender(Channel channel, SenderType senderType) {
        this.channel = channel;
        this.senderType = senderType;
        this.authType = AuthType.PENDING;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void sendPacket(Packet packet) {
        this.channel.writeAndFlush(packet);
    }
}
