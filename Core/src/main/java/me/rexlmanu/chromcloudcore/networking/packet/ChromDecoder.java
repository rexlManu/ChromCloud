package me.rexlmanu.chromcloudcore.networking.packet;

import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.networking.registry.PacketRegistry;
import me.rexlmanu.chromcloudcore.utility.packet.Buffer;

import java.util.List;

public final class ChromDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final Buffer buffer = new Buffer(byteBuf);
        final JsonElement jsonObject = ChromCloudCore.PARSER.parse(buffer.readString());
        list.add(PacketRegistry.readPacket(jsonObject.getAsJsonObject()));
    }
}
