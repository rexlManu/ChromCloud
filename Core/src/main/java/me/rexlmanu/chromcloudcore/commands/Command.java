package me.rexlmanu.chromcloudcore.commands;

import lombok.Data;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;

@Data
public abstract class Command {

    private String description;

    public Command() {
        this.description = "No definition";
    }

    public Command(String description) {
        this.description = description;
    }

    protected abstract Response execute(CommandSender commandSender, String[] args);
}
