package me.rexlmanu.chromcloudsubnode.server.view;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.networking.packets.ChromServerLogUpdatePacket;
import me.rexlmanu.chromcloudcore.utility.async.AsyncSession;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public final class ServerConsole {

    private int id;

    public ServerConsole(int id) {
        this.id = id;
    }

    public void startListen() {
        AsyncSession.getInstance().scheduleAsync(() ->
                        readLog(logs -> ChromCloudSubnode.getInstance().getChromChannelSender().sendPacket(new ChromServerLogUpdatePacket(logs, this.id))),
                250L, 500L, TimeUnit.MILLISECONDS);
    }

    public void runCommand(String command, Consumer<List<String>> commandResponse) {
        AsyncSession.getInstance().executeAsync(() -> {
            try {
                final Process process = new ProcessBuilder("docker", "exec", "mc-" + this.id, "rcon-cli", command).start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                List<String> commandResponseLogs = Lists.newArrayList();
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null)
                    commandResponseLogs.add(currentLine);
                bufferedReader.close();
                process.destroy();
                commandResponse.accept(commandResponseLogs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void readLog(Consumer<List<String>> logConsumer) {
        AsyncSession.getInstance().executeAsync(() -> {
            try {
                Process process = new ProcessBuilder("docker", "logs", "mc-" + this.id).start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                List<String> logs = Lists.newArrayList();
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null)
                    logs.add(currentLine);
                bufferedReader.close();
                process.destroy();
                logConsumer.accept(logs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
