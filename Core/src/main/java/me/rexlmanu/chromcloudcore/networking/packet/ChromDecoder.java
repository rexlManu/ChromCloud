package me.rexlmanu.chromcloudcore.networking.packet;

import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;

import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ChromDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final JsonElement jsonObject = ChromCloudCore.PARSER.parse(byteBuf.readBytes((int) readLong(byteBuf)).toString(StandardCharsets.UTF_8));
        final Packet e = PacketRegistry.readPacket(jsonObject.getAsJsonObject());
        list.add(e);
    }

    private long readLong(final ByteBuf byteBuf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = byteBuf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
        } while ((read & 0b10000000) != 0);
        return result;
    }
}
