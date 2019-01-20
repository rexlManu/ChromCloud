package me.rexlmanu.chromcloudcore.server.defaults;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class Version {

    private int id;
    private String jarName;
    private String jarDownload;
    private boolean ftbModpack;
    private boolean legacyJavaFixer;
    private String version;
    private String type;
}
