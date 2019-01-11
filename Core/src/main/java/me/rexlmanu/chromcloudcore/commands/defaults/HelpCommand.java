package me.rexlmanu.chromcloudcore.commands.defaults;

import com.google.gson.JsonArray;
import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.CommandManager;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;

public final class HelpCommand extends Command {

    public HelpCommand() {
        super("List the commands.");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        return Response.create().jsonCallback("Commands", () -> {
            JsonArray jsonElements = new JsonArray();
            CommandManager.getCommands().forEach((s, command) -> jsonElements.add(s + " - " + command.getDescription()));
            return jsonElements;
        });
    }
}
