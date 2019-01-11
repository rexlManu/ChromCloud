package me.rexlmanu.chromcloudcore.commands.defaults;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.utility.async.AsyncSession;

import java.util.concurrent.TimeUnit;

public final class StopCommand extends Command {

    public StopCommand() {
        super("Stop the chromcloud.");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        AsyncSession.getInstance().scheduleAsync(() -> System.exit(1), 100L, TimeUnit.MILLISECONDS);
        return Response.create().message("Stopping ChromCloud in 100ms.");
    }
}
