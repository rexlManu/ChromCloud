package me.rexlmanu.chromcloudnode.utility;

import me.rexlmanu.chromcloudcore.utility.network.NettyUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

public final class WrapperUtils {

    public static boolean isIpValid(String ip) {
        return ChromCloudNode.getInstance().getWrappers().stream().anyMatch(wrapper -> NettyUtils.getIpByChannel(wrapper.getChromChannelSender().getChannelHandlerContext().channel()).equals(ip));
    }
}
