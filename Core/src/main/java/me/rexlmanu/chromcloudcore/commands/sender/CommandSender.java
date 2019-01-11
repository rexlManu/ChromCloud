package me.rexlmanu.chromcloudcore.commands.sender;

import me.rexlmanu.chromcloudcore.commands.Command;
import me.rexlmanu.chromcloudcore.commands.response.Response;

public interface CommandSender {

    void sendResponse(Command command, Response response);
}
