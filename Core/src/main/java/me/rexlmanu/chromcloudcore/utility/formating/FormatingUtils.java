package me.rexlmanu.chromcloudcore.utility.formating;

public final class FormatingUtils {

    public static int castToInt(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return -1;
        }
    }
}
