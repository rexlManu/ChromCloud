package me.rexlmanu.chromcloudcore.utility.web;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {

    public static URL getAsUrl(String rawUrl) {
        try {
            return new URL(rawUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
