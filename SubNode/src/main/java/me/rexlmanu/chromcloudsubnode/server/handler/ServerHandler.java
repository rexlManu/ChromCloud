package me.rexlmanu.chromcloudsubnode.server.handler;

import lombok.Getter;
import lombok.Setter;
import me.rexlmanu.chromcloudcore.server.defaults.Server;
import me.rexlmanu.chromcloudcore.utility.async.AsyncSession;
import me.rexlmanu.chromcloudcore.utility.file.ZipUtils;
import me.rexlmanu.chromcloudcore.utility.process.ProcessUtils;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.server.view.ServerConsole;
import me.rexlmanu.docker.builder.DockerCommandBuilder;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Getter
public final class ServerHandler {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private Server server;
    private File serverDirectory;
    private File backupDirectory;
    private File tempDirectory;
    @Setter
    private DockerCommandBuilder buildCommand;
    private Process process;
    private ServerConsole serverConsole;

    public ServerHandler(Server server) {
        this.server = server;
        this.serverDirectory = new File("ChromCloud/servers/" + this.server.getId());
        this.backupDirectory = new File(this.serverDirectory, "/backups/");
        this.tempDirectory = new File(this.serverDirectory, "/temp/");
    }

    public boolean serverSetup() {
        final File globalServerDirectory = new File("ChromCloud/servers/");
        if (!globalServerDirectory.exists())
            globalServerDirectory.mkdir();
        if (serverDirectory.exists()) return false;
        this.serverDirectory.mkdir();
        this.backupDirectory.mkdir();
        this.tempDirectory.mkdir();
        return true;
    }

    public void handle(Process process) {
        this.process = process;
        AsyncSession.getInstance().executeAsync(() -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                final String startId = bufferedReader.readLine();
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Server created and started with the docker id " + startId + ".");
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        AsyncSession.getInstance().executeAsync(() -> {
            ProcessUtils.run(new ProcessBuilder("docker", "stop", "mc-" + this.server.getId()));
        });
    }

    public void kill() {
        AsyncSession.getInstance().executeAsync(() -> {
            ProcessUtils.run(new ProcessBuilder("docker", "kill", "mc-" + this.server.getId()));
        });
    }

    public void start() {
        AsyncSession.getInstance().executeAsync(() -> {
            ProcessUtils.run(new ProcessBuilder("docker", "start", "mc-" + this.server.getId()));
        });
    }

    public void remove() {
        AsyncSession.getInstance().executeAsync(() -> {
            stop();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            ProcessUtils.run(new ProcessBuilder("docker", "rm", "mc-" + this.server.getId()));
            try {
                ZipUtils.zip(this.tempDirectory, new File(this.backupDirectory, SIMPLE_DATE_FORMAT.format(new Date()) + ".zip"));
                FileUtils.deleteDirectory(this.tempDirectory);
            } catch (IOException e) {
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, "Backuping the server failed.");
                e.printStackTrace();
            }
        });
    }

    public void create() {
        AsyncSession.getInstance().executeAsync(() -> {
        });
        StringBuilder rawCommand = new StringBuilder();
        final List<String> build = this.buildCommand.build();
        for (String s : build)
            rawCommand.append(s).append(",");
        this.handle(ProcessUtils.run(new ProcessBuilder(rawCommand.toString().split(",")).redirectErrorStream(true).directory(this.tempDirectory)));
    }

    public void createServerConsole() {
        this.serverConsole = new ServerConsole(this.server.getId());
    }

    private String getIpAdress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
}
