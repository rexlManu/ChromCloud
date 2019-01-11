package me.rexlmanu.chromcloudcore.networking.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

import java.nio.charset.StandardCharsets;

public final class ChromEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("packet", new JsonPrimitive(packet.getClass().getSimpleName()));
        jsonObject.add("content", packet.getJsonElement());
        final byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        this.writeLongs(bytes.length, byteBuf).writeBytes(bytes);
    }

    private ByteBuf writeLongs(long value, ByteBuf byteBuf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0)
                temp |= 0b10000000;
            byteBuf.writeByte(temp);
        } while (value != 0);
        return byteBuf;
    }

}
