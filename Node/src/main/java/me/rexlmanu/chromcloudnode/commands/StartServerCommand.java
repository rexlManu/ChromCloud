package me.rexlmanu.chromcloudnode.commands;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

public final class StartServerCommand extends Command {

    public StartServerCommand() {
        super("Start a server");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        if (args.length != 2)
            return Response.create().error("The id is missing");
        int id = Integer.parseInt(args[1]);
        final boolean success = ChromCloudNode.getInstance().getServerManager().startServer(id);
        if (success)
            return Response.create().message("The server startet successful.");
        else return Response.create().error("The server could not get started.");
    }
}
