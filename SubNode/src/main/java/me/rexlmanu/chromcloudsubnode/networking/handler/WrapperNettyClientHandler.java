package me.rexlmanu.chromcloudsubnode.networking.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.rexlmanu.chromcloudcore.networking.packets.ChromAuthPacket;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.util.logging.Level;

public final class WrapperNettyClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Successfully connected to Node Server.");
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Trying Authentication with Node");
        ctx.channel().writeAndFlush(new ChromAuthPacket(ChromCloudSubnode.getInstance().getDefaultConfig().getAuthToken()));
        ChromCloudSubnode.getInstance().getNettyClient().setConnected(true);
        ReconnectHandler.getReconnectHandler().setTimeToSleep(1000);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.WARNING, "ChromCloudNode disconnected.");
        ChromCloudSubnode.getInstance().getNettyClient().setConnected(false);
        ReconnectHandler.getReconnectHandler().reconnect();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    @Deprecated
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {
    }

}
