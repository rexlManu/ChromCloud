package me.rexlmanu.chromcloudcore.commands.sender.console;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;

import java.util.logging.Level;

public final class ConsoleCommandSender implements CommandSender {

    @Override
    public void sendResponse(Command command, Response response) {
        final JsonObject jsonObject = response.build();
        if (jsonObject.keySet().isEmpty()) return;

        ChromLogger.getInstance().doLog(Level.INFO, "Response from " + command.getClass().getSimpleName());

        jsonObject.keySet().forEach(s -> {
            if (!s.equals("error") && !s.equals("message")) {
                final JsonElement jsonElement = jsonObject.get(s);
                if (jsonElement.isJsonPrimitive()) {
                    ChromLogger.getInstance().doLog(Level.INFO, jsonElement.toString());
                } else if (jsonElement.isJsonArray()) {
                    ChromLogger.getInstance().doLog(Level.INFO, s + ": ");
                    jsonElement.getAsJsonArray().forEach(jsonArrayElement -> {
                        ChromLogger.getInstance().doLog(Level.INFO, jsonArrayElement.toString());
                    });
                } else {
                    ChromLogger.getInstance().doLog(Level.INFO, jsonElement.toString());
                }
            }
        });
        if (jsonObject.has("message"))
            ChromLogger.getInstance().doLog(Level.INFO, jsonObject.get("message").getAsString());
        if (jsonObject.has("error"))
            jsonObject.get("error").getAsJsonArray().forEach(jsonElement -> {
                ChromLogger.getInstance().doLog(Level.WARNING, jsonElement.getAsString());
            });

    }
}
