package me.rexlmanu.chromcloudnode.utility;

import me.rexlmanu.chromcloudnode.ChromCloudNode;

public final class WrapperUtils {

    public static boolean isIpValid(String ip) {
        return ChromCloudNode.getInstance().getDefaultConfig().getIps().stream().anyMatch(s -> s.equals(ip));
    }
}
