package me.rexlmanu.chromcloudcore.networking.registry;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.networking.defaults.Packet;

import java.util.List;
import java.util.logging.Level;

public final class PacketRegistry {

    @Getter
    private static List<PacketReader> packetReaders;

    static {
        packetReaders = Lists.newArrayList();
    }

    public static void registerReader(PacketReader packetReader) {
        PacketRegistry.packetReaders.add(packetReader);
    }

    public static Packet readPacket(JsonObject jsonObject) {
        String packetName = jsonObject.get("packet").getAsString();
        try {
            final Class<?> packetClazz = Class.forName("me.rexlmanu.chromcloudcore.networking.packets." + packetName);
            final Packet packet = (Packet) packetClazz.newInstance();
            packet.setJsonElement(jsonObject.get("content"));
            return packet;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            ChromLogger.getInstance().doLog(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    public interface PacketReader {
        void onPacket(Packet packet, ChannelHandlerContext channelHandlerContext);
    }
}
