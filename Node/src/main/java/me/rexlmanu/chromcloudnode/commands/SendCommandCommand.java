package me.rexlmanu.chromcloudnode.commands;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.networking.packets.ChromServerSendCommandPacket;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.util.Objects;

public final class SendCommandCommand extends Command {

    public SendCommandCommand() {
        super("Send a command to a server.");
    }

    @Override
    protected Response execute(CommandSender commandSender, String[] args) {
        if (args.length < 2)
            return Response.create().error("The id is missing");
        int id = Integer.parseInt(args[1]);
        if (id == -1) {
            return Response.create().error("The id must be a number.");
        }
        final Server server = ChromCloudNode.getInstance().getServerManager().getServerById(id);
        if (Objects.isNull(server))
            return Response.create().error("Server not found.");
        final Wrapper wrapper = ChromCloudNode.getInstance().getWrapperManager().getWrapperByServer(server);
        if (Objects.isNull(wrapper))
            return Response.create().error("Wrapper not found.");
        final StringBuilder commandBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++)
            commandBuilder.append(args[i]).append(' ');
        final String command = commandBuilder.toString();
        wrapper.getChromChannelSender().sendPacket(new ChromServerSendCommandPacket(server.getId(), command));
        return Response.create().message("Send commmand '" + command + "' to server.");
    }
}
