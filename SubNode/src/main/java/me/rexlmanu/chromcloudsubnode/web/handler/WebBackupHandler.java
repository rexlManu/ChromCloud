package me.rexlmanu.chromcloudsubnode.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.rexlmanu.chromcloudcore.utility.web.HttpResponseUtils;

import java.io.IOException;

public final class WebBackupHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpResponseUtils.response(httpExchange, "{\"not implemented yet\":true}");
    }
}
