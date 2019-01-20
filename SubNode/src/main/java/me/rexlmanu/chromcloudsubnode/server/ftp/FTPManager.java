package me.rexlmanu.chromcloudsubnode.server.ftp;

import lombok.Getter;
import me.rexlmanu.chromcloudcore.manager.interfaces.DefaultManager;
import me.rexlmanu.chromcloudsubnode.ChromCloudSubnode;
import me.rexlmanu.chromcloudsubnode.server.handler.ServerHandler;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class FTPManager implements DefaultManager {

    private FtpServerFactory ftpServerFactory;
    private ListenerFactory listenerFactory;
    private FtpServer ftpServer;

    @Override
    public void init() {
        this.ftpServerFactory = new FtpServerFactory();
        this.listenerFactory = new ListenerFactory();
        int ftpPort = ChromCloudSubnode.getInstance().getDefaultConfig().getFtpPort();
        this.listenerFactory.setPort(ftpPort);
        this.ftpServerFactory.addListener("default", this.listenerFactory.createListener());
        PropertiesUserManagerFactory factory = new PropertiesUserManagerFactory();
        factory.setFile(new File("ignoremebecauseimtrash.properties"));
        factory.setPasswordEncryptor(new PasswordEncryptor() {
            @Override
            public String encrypt(String s) {
                return s;
            }

            @Override
            public boolean matches(String s, String s1) {
                return s.equals(s1);
            }
        });
        Map<String, Ftplet> stringFtpletHashMap = new HashMap<>();
        stringFtpletHashMap.put("miaFtplet", new Ftplet() {

            @Override
            public void init(FtpletContext ftpletContext) {
                System.out.println("init");
            }

            @Override
            public void destroy() {
                System.out.println("destroy");
            }

            @Override
            public FtpletResult beforeCommand(FtpSession session, FtpRequest request) {
                return FtpletResult.DEFAULT;
            }

            @Override
            public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) {
                return FtpletResult.DEFAULT;
            }

            @Override
            public FtpletResult onConnect(FtpSession session) {
                return FtpletResult.DEFAULT;
            }

            @Override
            public FtpletResult onDisconnect(FtpSession session) {
                return FtpletResult.DEFAULT;
            }
        });
        ftpServerFactory.setFtplets(stringFtpletHashMap);


        this.ftpServerFactory.setUserManager(factory.createUserManager());
        this.ftpServer = this.ftpServerFactory.createServer();
        try {
            this.ftpServer.start();
            ChromCloudSubnode.getInstance().getChromLogger().doLog(Level.INFO, String.format("THe ftpserver started on port %s.", ftpPort));
        } catch (FtpException ignored) {
        }

    }

    private void restart() {
        try {
            this.ftpServer.stop();
            this.ftpServer.start();
        } catch (FtpException ignored) {
        }
    }

    private BaseUser createUser(ServerHandler serverHandler) {
        BaseUser baseUser = new BaseUser();
        baseUser.setName("mc-" + serverHandler.getServer().getId());
        baseUser.setPassword(serverHandler.getServer().getServerConfiguration().getFtpPassword());
        baseUser.setEnabled(true);
        baseUser.setHomeDirectory(serverHandler.getTempDirectory().getAbsolutePath());
        baseUser.setAuthorities(Arrays.asList(new WritePermission(), new TransferRatePermission(0, 0), new ConcurrentLoginPermission(0, 0)));
        return baseUser;
    }

    public void onStop() {
        try {
            for (String allUserName : this.ftpServerFactory.getUserManager().getAllUserNames())
                this.ftpServerFactory.getUserManager().delete(allUserName);
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void createUserAndSave(ServerHandler serverHandler) {
        try {
            this.ftpServerFactory.getUserManager().save(this.createUser(serverHandler));
            this.restart();
        } catch (FtpException e) {
        }
    }
}
