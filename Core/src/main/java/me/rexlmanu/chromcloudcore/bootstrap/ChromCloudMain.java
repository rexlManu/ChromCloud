package me.rexlmanu.chromcloudcore.bootstrap;

import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;

import java.io.IOException;

public final class ChromCloudMain {

    private ChromCloudLaunch launch;

    public ChromCloudMain(ChromCloudLaunch launch) {
        this.launch = launch;
        try {
            this.launch.setLogger(new ChromLogger());
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {}
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
