package me.rexlmanu.chromcloudnode.configuration;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class WrapperConfiguration implements DefaultManager {

    private File file;
    private JsonArray jsonWrapper;
    @Getter
    private List<ConfigWrapper> configWrappers;

    public WrapperConfiguration() {
        this.file = new File("wrappers.json");
        this.jsonWrapper = new JsonArray();
        this.configWrappers = Lists.newArrayList();
    }

    private void save() {
        try {
            if (!file.exists())
                file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(ChromCloudCore.GSON.toJson(this.jsonWrapper));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            this.jsonWrapper = ChromCloudCore.PARSER.parse(new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset())).getAsJsonArray();
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "The wrapper configuration loaded successful.");
        } catch (IOException e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "The wrapper configuration could'nt been load. " + e.getMessage());
        }
    }

    public void addWrapper(String ip, String name) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ip", ip);
        jsonObject.addProperty("name", name);
        this.jsonWrapper.add(jsonObject);
    }

    private JsonElement wrapperToObject(ConfigWrapper configWrapper) {
        return ChromCloudCore.PARSER.parse(ChromCloudCore.GSON.toJson(configWrapper));
    }

    private ConfigWrapper objectToWrapper(JsonElement jsonElement) {
        return ChromCloudCore.GSON.fromJson(jsonElement, ConfigWrapper.class);
    }


    public ConfigWrapper getWrapperByIp(String ip) {
        return this.configWrappers.stream().filter(configWrapper -> configWrapper.getIp().equals(ip)).findFirst().orElse(null);
    }

    public boolean isIpValid(String ip) {
        return Objects.nonNull(this.getWrapperByIp(ip));
    }

    @Override
    public void init() {
        if (!file.exists())
            save();
        load();
        this.jsonWrapper.forEach(jsonElement -> this.configWrappers.add(objectToWrapper(jsonElement)));
    }

    @Data
    @AllArgsConstructor
    public static class ConfigWrapper {

        private String ip;
        private String name;
    }
}
