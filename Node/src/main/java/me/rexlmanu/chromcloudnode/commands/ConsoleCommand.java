package me.rexlmanu.chromcloudnode.commands;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.util.Objects;

public final class ConsoleCommand extends Command {

    public ConsoleCommand() {
        super("Go in one console in side.");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        if (args.length != 2)
            return Response.create().error("The id is missing");
        int id = Integer.parseInt(args[1]);
        if (id == -1) {
            return Response.create().error("The id must be a number.");
        }
        final Server server = ChromCloudNode.getInstance().getServerManager().getServerById(id);
        if (Objects.isNull(server))
            return Response.create().error("Server not found.");
        return Response.create().jsonCallback("log", () -> ArrayUtils.toJsonArray(server.getLogs()));
    }
}
