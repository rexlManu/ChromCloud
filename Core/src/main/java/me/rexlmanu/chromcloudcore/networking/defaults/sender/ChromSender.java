package me.rexlmanu.chromcloudcore.networking.defaults.sender;

import io.netty.channel.Channel;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

public interface ChromSender {

    Channel getChannel();

    void sendPacket(Packet packet);
}
