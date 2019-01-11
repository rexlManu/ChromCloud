package me.rexlmanu.chromcloudnode.networking.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.rexlmanu.chromcloudcore.networking.enums.AuthType;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.util.logging.Level;

public final class ChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        final Channel channel = ctx.channel();
        final Wrapper wrapper = ChromCloudNode.getInstance().getWrapperByChannel(channel);
        if (wrapper != null) {
            if (wrapper.getChromChannelSender().getAuthType().equals(AuthType.SUCCESS)) {
                ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Subnode [" + channel.remoteAddress().toString() + "] disconnecting...");
                ChromCloudNode.getInstance().getWrapperManager().disconnect(wrapper, false);
            }
        }
    }

}
