package me.rexlmanu.chromcloudcore.utility.random;

import me.rexlmanu.chromcloudcore.ChromCloudCore;

public final class RandomUtils {

    public static String generateToken(int length) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(ChromCloudCore.RANDOM[ChromCloudCore.THREAD_LOCAL_RANDOM.nextInt(ChromCloudCore.RANDOM.length)]);
        return builder.toString();
    }
}
