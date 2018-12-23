package me.rexlmanu.chromcloudnode.web.managers;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.web.defaults.User;

import java.util.Set;
import java.util.logging.Level;

public final class UserManager implements DefaultManager {

    @Getter
    private Set<User> users;

    public UserManager() {
        this.users = Sets.newHashSet();
    }

    @Override
    public void init() {
        if (new ChromCloudNode().getUserConfiguration() != null){
            this.users = new ChromCloudNode().getUserConfiguration().getLoadedUsers();
        }else ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, "UserConfiguration failed.");
    }
}
