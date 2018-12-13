package me.rexlmanu.chromcloudsubnode.networking.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.networking.handler.PacketHandler;
import me.rexlmanu.chromcloudcore.networking.packet.ChromDecoder;
import me.rexlmanu.chromcloudcore.networking.packet.ChromEncoder;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.networking.handler.WrapperNettyClientHandler;

import java.util.logging.Level;

public final class NettyClient {

    public void init(String address, int port) {
        EventLoopGroup group = ChromCloudCore.eventLoopGroup(4);

        try {

            Bootstrap bootstrap = new Bootstrap()
                    .option(ChannelOption.AUTO_READ, true)
                    .group(group)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4), new LengthFieldPrepender(4));
                            channel.pipeline().addLast(new ChromEncoder());
                            channel.pipeline().addLast(new ChromDecoder());
                            channel.pipeline().addLast(new PacketHandler());
                            channel.pipeline().addLast(new WrapperNettyClientHandler());

                        }
                    })
                    .channel(ChromCloudCore.chromCloudClientSocketChannel()
                    );

            Channel channel = bootstrap.connect(address, port).addListener(future -> {
                if (future.isSuccess())
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "ChromCloud is connecting to " + address + ":" + port);
                else
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "Failed to connect @" + address + ":" + port);

            }).sync().channel();
        } catch (Exception ex) {
            ex.printStackTrace();
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, ex.toString());
        }
    }

}
