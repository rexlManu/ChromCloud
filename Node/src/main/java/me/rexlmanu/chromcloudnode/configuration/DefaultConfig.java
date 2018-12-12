package me.rexlmanu.chromcloudnode.configuration;

import com.google.gson.JsonObject;
import jline.console.ConsoleReader;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.configuration.JsonConfiguration;
import me.rexlmanu.chromcloudcore.utility.network.NetworkUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class DefaultConfig {

    @Getter
    private String socketIp, mySqlUsername, mySqlPassword, mySqlDatabase;
    @Getter
    private int socketPort, mySqlPort;

    private JsonConfiguration jsonConfiguration;
    private File file;
    private ConsoleReader consoleReader;

    public DefaultConfig(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
        createDirectory();
        this.file = new File(".chrom", "config.json");
    }

    public void init() throws IOException {
        String ip = NetworkUtils.getAdress();
        if (!file.exists()) {
            if (ip.equals("127.0.0.1") || ip.equals("127.0.1.1") || ip.split("\\.").length != 4) {
                String input;
                ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Your IP Adress is not valid, please enter your IP4 Adress.");
                while ((input = consoleReader.readLine()) != null) {
                    if (input.equals("127.0.0.1") || input.equals("127.0.1.1") || input.split("\\.").length != 4) {
                        ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Your IP Adress is not valid, please enter you IP4 Adress.");
                        continue;
                    }
                    ip = input;
                    break;
                }
            }
        }

        this.socketIp = ip;
        this.mySqlUsername = "root";
        this.mySqlPassword = "MySQL Password";
        this.mySqlDatabase = "MySQL Database";
        this.socketPort = 9100;
        this.mySqlPort = 3306;

        final JsonObject object = new JsonObject();
        object.addProperty("socketIp", socketIp);
        object.addProperty("mySqlUsername", mySqlUsername);
        object.addProperty("mySqlPassword", mySqlPassword);
        object.addProperty("mySqlDatabase", mySqlDatabase);
        object.addProperty("socketPort", socketPort);
        object.addProperty("mySqlPort", mySqlPort);
        this.jsonConfiguration = new JsonConfiguration(object);
        try {
            if (!file.exists()) {
                this.jsonConfiguration.save(file);
                ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Creating default configuration");
            }
            this.jsonConfiguration.load(file);
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "Loaded default configuration");
        } catch (IOException e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, e.getMessage());
        }
    }

    private void createDirectory() {
        new File(".chrom").mkdir();
    }
}
