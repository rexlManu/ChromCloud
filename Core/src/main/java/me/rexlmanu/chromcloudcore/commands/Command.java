package me.rexlmanu.chromcloudcore.commands;

import lombok.Data;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;

@Data
public abstract class Command {

    private String description;
    private String permission;

    public Command() {
        this.description = "No definition";
        this.permission = "";
    }

    public Command(String description, String permission) {
        this.description = description;
        this.permission = permission;
    }

    public Command(String description) {
        this.description = description;
    }

    protected abstract Response execute(CommandSender commandSender, String[] args);
}
