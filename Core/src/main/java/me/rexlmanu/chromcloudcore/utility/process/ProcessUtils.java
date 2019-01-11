package me.rexlmanu.chromcloudcore.utility.process;

import java.io.IOException;

public final class ProcessUtils {

    public static Process run(ProcessBuilder builder) {
        try {
            return builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
