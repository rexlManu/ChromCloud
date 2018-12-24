package me.rexlmanu.chromcloudnode.networking.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import me.rexlmanu.chromcloudcore.networking.defaults.sender.defaults.ChromChannelSender;
import me.rexlmanu.chromcloudcore.networking.enums.SenderType;
import me.rexlmanu.chromcloudcore.networking.handler.PacketHandler;
import me.rexlmanu.chromcloudcore.networking.packet.ChromDecoder;
import me.rexlmanu.chromcloudcore.networking.packet.ChromEncoder;
import me.rexlmanu.chromcloudcore.utility.network.NettyUtils;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.networking.handler.ChannelInboundHandler;
import me.rexlmanu.chromcloudnode.utility.WrapperUtils;

import java.util.logging.Level;


public final class NettyServer extends ChannelInitializer<Channel> {
    private final EventLoopGroup workGroup = NettyUtils.getEventLoopGroup();
    private final EventLoopGroup bossGroup = NettyUtils.getEventLoopGroup();

    public void init(String address, int port) throws Exception {
        assert NettyUtils.getServerSocketChannelClass() != null;
        assert workGroup != null;
        assert bossGroup != null;
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)

                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .option(ChannelOption.AUTO_READ, true)

                .channel(NettyUtils.getServerSocketChannelClass())

                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(this);

        ChannelFuture channelFuture = serverBootstrap.bind(address, port).addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess())
                ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "ChromCloud is binded to " + address + ":" + port);
            else
                ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "Failed to bind @" + address + ":" + port);

        });
    }

    @Override
    protected void initChannel(Channel channel) {
        ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Subnode [" + channel.remoteAddress().toString() + "] connecting...");

        if (!WrapperUtils.isIpValid(NettyUtils.getIpByChannel(channel))) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.WARNING, "Connection from " + channel.remoteAddress().toString() +
                    " refused, IP-Address is not valid!");
            channel.close().addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            return;
        }

        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4), new LengthFieldPrepender(4));
        channel.pipeline().addLast(new ChromEncoder());
        channel.pipeline().addLast(new ChromDecoder());
        channel.pipeline().addLast(new PacketHandler());
        channel.pipeline().addLast(new ChannelInboundHandler());

        ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Subnode connected: [" + channel.remoteAddress().toString().replace("/", "") + "]");
        ChromCloudNode.getInstance().getWrappers().add(new Wrapper(new ChromChannelSender(channel, SenderType.WRAPPER)));
    }
}
