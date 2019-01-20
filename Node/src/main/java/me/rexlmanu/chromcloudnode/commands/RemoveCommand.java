package me.rexlmanu.chromcloudnode.commands;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

public final class RemoveCommand extends Command {

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        if (args.length != 2)
            return Response.create().error("The id is missing");
        int id = Integer.parseInt(args[1]);
        if (id == -1) {
            return Response.create().error("The id must be a number.");
        }
        final boolean success = ChromCloudNode.getInstance().getServerManager().removeServer(id);
        if (success)
            return Response.create().message("The server removed successful.");
        else return Response.create().error("The server could not get removed.");
    }
}
