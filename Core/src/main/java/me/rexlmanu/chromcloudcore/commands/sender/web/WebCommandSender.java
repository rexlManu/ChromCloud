package me.rexlmanu.chromcloudcore.commands.sender.web;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.utility.web.HttpResponseUtils;

@AllArgsConstructor
public final class WebCommandSender implements CommandSender {

    private HttpExchange httpExchange;

    @Override
    public void sendResponse(Command command, Response response) {
        HttpResponseUtils.response(httpExchange, response.build().toString());
    }
}
