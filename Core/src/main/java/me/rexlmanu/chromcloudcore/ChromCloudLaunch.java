package me.rexlmanu.chromcloudcore;

import me.rexlmanu.chromcloudcore.logger.ChromLogger;

public interface ChromCloudLaunch {
    
    void onStart();

    void onStop();

    void setLogger(ChromLogger chromLogger);

}
