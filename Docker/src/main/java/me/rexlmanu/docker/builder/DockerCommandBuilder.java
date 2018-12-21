package me.rexlmanu.docker.builder;

import me.rexlmanu.docker.builder.interfaces.Builder;

import java.io.File;

public class DockerCommandBuilder implements Builder<String> {

    private StringBuilder stringBuilder;
    private String name;

    public DockerCommandBuilder(String name) {
        this.name = name;
        this.stringBuilder = new StringBuilder();
    }

    public DockerCommandBuilder base() {
        this.stringBuilder.append("docker ").append("run ").append("-d ");
        return this;
    }

    public DockerCommandBuilder path(File file) {
        this.stringBuilder.append("-v ").append(file.getAbsolutePath().replace("\\", "/") + ":/data -it ");
        return this;
    }

    public DockerCommandBuilder eulaAccept(boolean accpted) {
        this.stringBuilder.append("-e ").append(("eula=" + accpted).toUpperCase() + " ");
        return this;
    }

    public DockerCommandBuilder version(String version) {
        this.stringBuilder.append("-e ").append(("VERSION=" + version) + " ");
        return this;
    }

    public DockerCommandBuilder type(String type) {
        this.stringBuilder.append("-e ").append(("TYPE=" + type) + " ");
        return this;
    }

    public DockerCommandBuilder spigotDownloadUrl(String url) {
        this.stringBuilder.append("-e ").append(("SPIGOT_DOWNLOAD_URL=" + url) + " ");
        return this;
    }

    public DockerCommandBuilder bukkitDownloadUrl(String url) {
        this.stringBuilder.append("-e ").append(("BUKKIT_DOWNLOAD_URL=" + url) + " ");
        return this;
    }

    public DockerCommandBuilder ftbServerModpack(String fileName) {
        this.stringBuilder.append("-e ").append("FTB_SERVER_MOD=" + fileName + " ");
        return this;
    }

    public DockerCommandBuilder legacyJavaFixer(boolean legacyJavaFixing) {
        this.stringBuilder.append("-e ").append("FTB_LEGACYJAVAFIXER=" + legacyJavaFixing + " ");
        return this;
    }

    public DockerCommandBuilder forgeInstaller(String installer) {
        this.stringBuilder.append("-e ").append("FORGE_INSTALLER=" + installer + " ");
        return this;
    }

    public DockerCommandBuilder forgeVersion(String forgeVersion) {
        this.stringBuilder.append("-e ").append("FORGEVERSION=" + forgeVersion + " ");
        return this;
    }

    public DockerCommandBuilder port(int port) {
        this.stringBuilder.append("-p ").append(port + ":" + port + " ");
        return this;
    }

    private DockerCommandBuilder name(String name) {
        this.stringBuilder.append("--name ").append(name + " itzg/minecraft-server");
        return this;
    }

    @Override
    public String build() {
        return name(name).stringBuilder.toString();
    }
}
