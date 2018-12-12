package me.rexlmanu.chromcloudcore.networking.packet;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.utility.packet.Buffer;

public final class ChromEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        final Buffer buffer = new Buffer(byteBuf);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("packet", packet.getClass().getSimpleName());
        jsonObject.add("content", packet.getJsonElement());
        buffer.writeString(jsonObject.toString());
    }
}
