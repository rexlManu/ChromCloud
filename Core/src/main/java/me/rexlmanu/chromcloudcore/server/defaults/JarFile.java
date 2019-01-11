package me.rexlmanu.chromcloudcore.server.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@AllArgsConstructor
@Data
public final class JarFile {

    private String jarName;
    private URL downloadUrl;
}
