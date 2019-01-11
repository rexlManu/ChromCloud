package me.rexlmanu.chromcloudcore.commands.defaults;

import me.rexlmanu.chromcloudcore.commands.ConsoleCommand;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;

import java.io.IOException;

public final class ClearCommand extends ConsoleCommand {

    public ClearCommand() {
        super("Clear the screen of the console");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        try {
            ChromLogger.getConsoleReader().clearScreen();
            return Response.create().empty();
        } catch (IOException e) {
            return Response.create().message("Failed to clear the screen");
        }
    }
}
