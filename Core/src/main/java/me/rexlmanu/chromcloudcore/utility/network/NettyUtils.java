package me.rexlmanu.chromcloudcore.utility.network;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.rexlmanu.chromcloudcore.ChromCloudCore;

public final class NettyUtils {

    public static EventLoopGroup getEventLoopGroup() {
        switch (getTransportType()) {
            case NIO:
                return new NioEventLoopGroup();
            case EPOLL:
                return new EpollEventLoopGroup();
            default:
                return null;
        }
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        switch (getTransportType()) {
            case EPOLL:
                return EpollServerSocketChannel.class;
            case NIO:
                return NioServerSocketChannel.class;
            default:
                return null;
        }
    }

    private static TransportType getTransportType() {
        if (ChromCloudCore.EPOLL) return TransportType.EPOLL;
        return TransportType.NIO;
    }

    private enum TransportType {
        EPOLL,
        NIO;
    }

    public static String getIpByChannel(Channel channel){
        return channel.remoteAddress().toString().split(":")[0].replace("/", "");
    }

}
