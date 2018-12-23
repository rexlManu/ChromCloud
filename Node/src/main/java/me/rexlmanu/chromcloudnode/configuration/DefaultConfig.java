package me.rexlmanu.chromcloudnode.configuration;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import jline.console.ConsoleReader;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.configuration.JsonConfiguration;
import me.rexlmanu.chromcloudcore.utility.json.ArrayUtils;
import me.rexlmanu.chromcloudcore.utility.network.NetworkUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public final class DefaultConfig {

    @Getter
    private String socketIp, mySqlUsername, mySqlPassword, mySqlDatabase,mySqlHostname, authToken;
    @Getter
    private int socketPort, mySqlPort, webSocketPort;

    @Getter
    private List<String> ips;

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

        this.ips = Lists.newArrayList();
        this.ips.add(ip);
        this.socketIp = ip;
        this.mySqlUsername = "MySQL Username";
        this.mySqlPassword = "MySQL Password";
        this.mySqlDatabase = "MySQL Database";
        this.mySqlHostname = "MySQL Hostname";
        this.socketPort = 9100;
        this.mySqlPort = 3306;
        this.webSocketPort = 9200;
        this.authToken = ChromCloudCore.randomString(32);

        final JsonObject object = new JsonObject();
        object.addProperty("socketIp", this.socketIp);
        object.addProperty("mySqlUsername", this.mySqlUsername);
        object.addProperty("mySqlPassword", this.mySqlPassword);
        object.addProperty("mySqlDatabase", this.mySqlDatabase);
        object.addProperty("mySqlHostname", this.mySqlHostname);
        object.addProperty("socketPort", this.socketPort);
        object.addProperty("mySqlPort", this.mySqlPort);
        object.addProperty("webSocketPort", this.webSocketPort);
        object.addProperty("authToken", this.authToken);
        object.add("ips", ArrayUtils.toJsonArray(this.ips));

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

        final JsonObject jsonObject = this.jsonConfiguration.getJsonElement().getAsJsonObject();
        this.socketIp = jsonObject.getAsJsonPrimitive("socketIp").getAsString();
        this.mySqlUsername = jsonObject.getAsJsonPrimitive("mySqlUsername").getAsString();
        this.mySqlPassword = jsonObject.getAsJsonPrimitive("mySqlPassword").getAsString();
        this.mySqlDatabase = jsonObject.getAsJsonPrimitive("mySqlDatabase").getAsString();
        this.mySqlHostname = jsonObject.getAsJsonPrimitive("mySqlHostname").getAsString();
        this.authToken = jsonObject.getAsJsonPrimitive("authToken").getAsString();
        this.socketPort = jsonObject.getAsJsonPrimitive("socketPort").getAsInt();
        this.mySqlPort = jsonObject.getAsJsonPrimitive("mySqlPort").getAsInt();
        this.webSocketPort = jsonObject.getAsJsonPrimitive("webSocketPort").getAsInt();
        this.ips = ArrayUtils.fromJsonArray(jsonObject.get("ips").getAsJsonArray());
    }

    private void createDirectory() {
        new File(".chrom").mkdir();
    }
}
