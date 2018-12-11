package me.rexlmanu.chromcloudsubnode.bootstrap;

import me.rexlmanu.chromcloudcore.bootstrap.ChromCloudMain;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

public final class ChromCloudSubnodeLaunch {

    public static void main(String[] args) {
        new ChromCloudMain(new ChromCloudSubnode());
    }
}
