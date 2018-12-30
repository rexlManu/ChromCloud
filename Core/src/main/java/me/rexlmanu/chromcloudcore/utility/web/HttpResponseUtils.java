package me.rexlmanu.chromcloudcore.utility.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public final class HttpResponseUtils {

    public static void response(HttpExchange httpExchange, String response){
        try {
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
