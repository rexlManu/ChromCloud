package me.rexlmanu.chromcloudcore.bootstrap;

import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;

public final class ChromCloudMain {

    private ChromCloudLaunch launch;

    public ChromCloudMain(ChromCloudLaunch launch) {
        this.launch = launch;
        shutdownHook();
        ChromCloudCore.sendHeader();
        start();
    }

    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void start() {
        this.launch.onStart();
    }

    private void stop() {
        this.launch.onStop();
        ChromCloudCore.sendFooter();
    }
}
