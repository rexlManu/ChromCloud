package me.rexlmanu.chromcloudsubnode;

import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;

@Getter
public final class ChromCloudSubnode implements ChromCloudLaunch {

    @Getter
    private static ChromCloudSubnode instance;

    private ChromLogger chromLogger;

    @Override
    public void onStart() {
        ChromCloudSubnode.instance = this;
    }

    @Override
    public void onStop() {
        ChromCloudSubnode.instance = null;
    }

    @Override
    public void setLogger(ChromLogger chromLogger) {
        this.chromLogger = chromLogger;
    }
}
