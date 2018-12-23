package me.rexlmanu.chromcloudcore.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;

@Data
@AllArgsConstructor
public abstract class Command {

    private String description;

    public Command() {
        this.description = "No definition";
    }

    protected abstract Response execute(CommandSender commandSender, String[] args);
}
