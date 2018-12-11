package me.rexlmanu.chromcloudnode.bootstrap;

import me.rexlmanu.chromcloudcore.bootstrap.ChromCloudMain;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

public final class ChromCloudNodeLaunch {

    public static void main(String[] args) {
        new ChromCloudMain(new ChromCloudNode());
    }
}
