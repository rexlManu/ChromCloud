package me.rexlmanu.chromcloudsubnode.configuration;

import com.google.gson.JsonObject;
import jline.console.ConsoleReader;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.configuration.JsonConfiguration;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class DefaultConfig {

    @Getter
    private String socketIp, authToken;
    @Getter
    private int socketPort;

    private JsonConfiguration jsonConfiguration;
    private File file;
    private ConsoleReader consoleReader;

    public DefaultConfig(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
        createDirectory();
        this.file = new File(".chrom", "config.json");
    }

    public void init() throws IOException {
        String ip = "";
        this.socketPort = 9100;
        this.authToken = "none";
        if (!file.exists()) {
            if (ip.split("\\.").length != 4) {
                String input;
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Please enter the IP adresse from the node server.");
                while ((input = consoleReader.readLine()) != null) {
                    if (input.equals("127.0.0.1") || input.equals("127.0.1.1") || input.split("\\.").length != 4) {
                        ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Your IP Adress is not valid, please enter the IP adresse from the node server..");
                        continue;
                    }
                    ip = input;
                    break;
                }
            }

            String input;
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Please enter the auth token from the node server.");
            while ((input = consoleReader.readLine()) != null) {
                if (input.length() != 32) {
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Your auth token is invalid. The length of the auth token must be 32 chars length.");
                    continue;
                }
                if (input.equals("none")) {
                    ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Your auth token is invalid. Please enter the auth token from the node server.");
                    continue;
                }
                this.authToken = input;
                break;
            }
        }

        this.socketIp = ip;

        final JsonObject object = new JsonObject();
        object.addProperty("socketIp", this.socketIp);
        object.addProperty("socketPort", this.socketPort);
        object.addProperty("authToken", this.authToken);

        this.jsonConfiguration = new JsonConfiguration(object);

        try {
            if (!file.exists()) {
                this.jsonConfiguration.save(file);
                ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Creating default configuration");
            }
            this.jsonConfiguration.load(file);
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, "Loaded default configuration");
        } catch (IOException e) {
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.SEVERE, e.getMessage());
        }

        final JsonObject jsonObject = this.jsonConfiguration.getJsonElement().getAsJsonObject();
        this.socketIp = jsonObject.getAsJsonPrimitive("socketIp").getAsString();
        this.authToken = jsonObject.getAsJsonPrimitive("authToken").getAsString();
        this.socketPort = jsonObject.getAsJsonPrimitive("socketPort").getAsInt();
    }

    private void createDirectory() {
        new File(".chrom").mkdir();
    }
}
