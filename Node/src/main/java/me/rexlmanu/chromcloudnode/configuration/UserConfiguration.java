package me.rexlmanu.chromcloudnode.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudcore.utility.random.RandomUtils;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.web.defaults.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class UserConfiguration implements DefaultManager {

    private File file;
    private JsonArray userArray;

    public UserConfiguration() {
        this.file = new File(".chrom", "users.json");
        this.userArray = new JsonArray();
    }

    public Set<User> getLoadedUsers() {
        final HashSet<User> users = new HashSet<>();
        userArray.forEach(jsonElement -> users.add(objectToUser(jsonElement)));
        return users;
    }

    private void save() {
        try {
            if (!file.exists())
                file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(ChromCloudCore.GSON.toJson(this.userArray));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement userToObject(User user) {
        return ChromCloudCore.PARSER.parse(ChromCloudCore.GSON.toJson(user));
    }

    private User objectToUser(JsonElement jsonElement) {
        return ChromCloudCore.GSON.fromJson(jsonElement, User.class);
    }


    private User createDefaultUser() {
        return new User("ChromCloudAdmin", RandomUtils.generateToken(32));
    }

    @Override
    public void init() {
        if (!file.exists()) {
            final User defaultUser = this.createDefaultUser();
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "The default user '" + defaultUser.getUserName() + "' is created with the token '" + defaultUser.getToken() + "'.");
            this.userArray.add(userToObject(defaultUser));
            save();
        }

        try {
            this.userArray = ChromCloudCore.PARSER.parse(new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset())).getAsJsonArray();
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.INFO, "The user configuration loaded successful.");
        } catch (IOException e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "The user configuration could'nt been load. " + e.getMessage());
        }
    }
}
