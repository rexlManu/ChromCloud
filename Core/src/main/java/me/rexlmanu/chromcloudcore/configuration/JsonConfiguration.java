package me.rexlmanu.chromcloudcore.configuration;

import com.google.gson.JsonElement;
import lombok.Data;
import me.rexlmanu.chromcloudcore.ChromCloudCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Objects;

@Data
public final class JsonConfiguration {

    private JsonElement jsonElement;

    public JsonConfiguration(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    public void save(File file) throws IOException {
        if (Objects.isNull(file)) return;
        if (Objects.isNull(jsonElement)) return;
        if (!file.exists())
            file.createNewFile();

        try (PrintStream printStream = new PrintStream(new FileOutputStream(file))) {
            printStream.print(ChromCloudCore.GSON.toJson(jsonElement));
            printStream.flush();
        }
    }

    public void load(File file) throws IOException {
        if (Objects.isNull(file)) return;
        if (!file.exists()) return;
        this.jsonElement = ChromCloudCore.PARSER.parse(new String(Files.readAllBytes(file.toPath())));
    }

}
