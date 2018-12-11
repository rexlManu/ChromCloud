package me.rexlmanu.chromcloudsubnode;

import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;

public final class ChromCloudSubnode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudSubnode instance;

    @Override
    public void onStart() {
        ChromCloudSubnode.instance = this;
    }

    @Override
    public void onStop() {
        ChromCloudSubnode.instance = null;
    }
}
