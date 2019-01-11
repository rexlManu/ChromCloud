package me.rexlmanu.docker.builder;

import com.google.common.collect.Lists;
import me.rexlmanu.docker.builder.interfaces.Builder;

import java.io.File;
import java.util.List;

public class DockerCommandBuilder implements Builder<List<String>> {

    private List<String> commands;
    private String name;

    public DockerCommandBuilder(String name) {
        this.name = name;
        this.commands = Lists.newArrayList();
    }

    public DockerCommandBuilder base() {
        this.commands.add("docker");
        this.commands.add("run");
        this.commands.add("-d");
        return this;
    }

    public DockerCommandBuilder path(File file) {
        this.commands.add("-v");
        this.commands.add(file.getAbsolutePath().replace("\\", "/") + ":/data");
        this.commands.add("-it");
        return this;
    }

    public DockerCommandBuilder attach() {
        this.commands.add("-it");
        return this;
    }

    public DockerCommandBuilder eulaAccept(boolean accpted) {
        this.commands.add("-e");
        this.commands.add(("eula=" + accpted).toUpperCase());
        return this;
    }

    public DockerCommandBuilder version(String version) {
        this.commands.add("-e");
        this.commands.add(("VERSION=" + version));
        return this;
    }

    public DockerCommandBuilder type(String type) {
        this.commands.add("-e");
        this.commands.add(("TYPE=" + type));
        return this;
    }

    public DockerCommandBuilder spigotDownloadUrl(String url) {
        this.commands.add("-e");
        this.commands.add(("SPIGOT_DOWNLOAD_URL=" + url));
        return this;
    }

    public DockerCommandBuilder bukkitDownloadUrl(String url) {
        this.commands.add("-e");
        this.commands.add(("BUKKIT_DOWNLOAD_URL=" + url));

        return this;
    }

    public DockerCommandBuilder ftbServerModpack(String fileName) {
        this.commands.add("-e");
        this.commands.add(("FTB_SERVER_MOD=" + fileName));

        return this;
    }

    public DockerCommandBuilder legacyJavaFixer(boolean legacyJavaFixing) {
        this.commands.add("-e");
        this.commands.add("FTB_LEGACYJAVAFIXER=" + legacyJavaFixing);

        return this;
    }

    public DockerCommandBuilder forgeInstaller(String installer) {
        this.commands.add("-e");
        this.commands.add("FORGE_INSTALLER=" + installer);

        return this;
    }

    public DockerCommandBuilder forgeVersion(String forgeVersion) {
        this.commands.add("-e");
        this.commands.add("FORGEVERSION=" + forgeVersion);

        return this;
    }

    public DockerCommandBuilder port(int port) {
        this.commands.add("-p");
        this.commands.add("25565:" + port);

        return this;
    }

    private DockerCommandBuilder name(String name) {
        this.commands.add("--name");
        this.commands.add(name);
        this.commands.add("itzg/minecraft-server");
        return this;
    }

    @Override
    public List<String> build() {
        return name(name).commands;
    }
}
