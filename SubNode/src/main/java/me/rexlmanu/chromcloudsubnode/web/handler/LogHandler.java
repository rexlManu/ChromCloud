package me.rexlmanu.chromcloudsubnode.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;
import me.rexlmanu.chromcloudcore.utility.web.HttpResponseUtils;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.io.IOException;

public final class LogHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String replacedPath = httpExchange.getRequestURI().getPath().replace("/log/", "");
        if (!replacedPath.contains("/")) {
            return;
        }
        try {
            final String[] dataSplit = replacedPath.split("/");
            final String token = dataSplit[0];
            if (!ChromCloudSubnode.getInstance().getWebManager().getToken().equals(token)) {
                HttpResponseUtils.response(httpExchange, "Token is not right.");
                return;
            }
            final int serverId = Integer.parseInt(dataSplit[1]);
            HttpResponseUtils.response(httpExchange, ArrayUtils.toJsonArray(ChromCloudSubnode.getInstance().getServerManager().getServerHandlerById(serverId).getServerConsole().getLogs()).toString());
        } catch (Exception e) {
            HttpResponseUtils.response(httpExchange, "error");
        }
    }
}
