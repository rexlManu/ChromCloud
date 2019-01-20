package me.rexlmanu.chromcloudnode.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.commands.CommandManager;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.web.WebCommandSender;
import me.rexlmanu.chromcloudcore.utility.web.HttpResponseUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.io.IOException;

public final class WebHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (!httpExchange.getRequestHeaders().containsKey("chromcloud_auth_token")) {
            HttpResponseUtils.response(httpExchange, Response.create().error("Authentication missing").build().toString());
            return;
        }

        final String authToken = httpExchange.getRequestHeaders().getFirst("chromcloud_auth_token");
        if (ChromCloudNode.getInstance().getUserManager().isValid(authToken)) {
            HttpResponseUtils.response(httpExchange, Response.create().error("Authtoken is wrong").build().toString());
            return;
        }

        final StringBuilder rawCommand = new StringBuilder();
//        if (rawCommand.contains("/"))
//            rawCommand = rawCommand.split("/")[0];
        final String commandName = httpExchange.getRequestURI().getPath().replace("/api/v1/", "");

        if (commandName.isEmpty()) {
            HttpResponseUtils.response(httpExchange, Response.create().error("Please type 'help' for the command list.").build().toString());
            return;
        }

        rawCommand.append(commandName + " ");
        final String rawArgs = httpExchange.getRequestHeaders().getFirst("chromcloud_args");

        try {
            ChromCloudCore.PARSER.parse(rawArgs).getAsJsonArray().forEach(jsonElement -> {
                rawCommand.append(jsonElement.getAsString() + " ");
            });
        } catch (Exception e) {
            HttpResponseUtils.response(httpExchange, "Error at args parsing...");
            return;
        }
        if (!CommandManager.dispatchCommand(rawCommand.toString().trim(), new WebCommandSender(httpExchange))) {
            HttpResponseUtils.response(httpExchange, Response.create().error("Please type 'help' for the command list.").build().toString());
        }
    }

}
