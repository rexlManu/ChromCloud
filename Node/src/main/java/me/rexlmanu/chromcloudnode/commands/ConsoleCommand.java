package me.rexlmanu.chromcloudnode.commands;

import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.web.UrlUtils;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
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
        final Wrapper wrapper = ChromCloudNode.getInstance().getWrapperManager().getWrapperByServer(server);
        if (Objects.isNull(wrapper))
            return Response.create().error("Wrapper not found.");
        final URL url = UrlUtils.getAsUrl(String.format("http://%s:%s/log/%s/%s", wrapper.getChromChannelSender().getChannel().remoteAddress().toString().replace("/", "").split(":")[0], wrapper.getWebPort(), wrapper.getToken(), server.getId()));
        try {
            assert url != null;
            return Response.create().json("log", ChromCloudCore.PARSER.parse(IOUtils.toString(url.openStream(), "utf8")).getAsJsonArray());
        } catch (IOException e) {
            return Response.create().error("Error while requesting").error(e.getMessage());
        }
    }
}
