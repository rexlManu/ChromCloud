package me.rexlmanu.chromcloudnode;

import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;

public final class ChromCloudNode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudNode instance;

    @Override
    public void onStart() {
        ChromCloudNode.instance = this;
    }

    @Override
    public void onStop() {
        ChromCloudNode.instance = null;
    }
}
