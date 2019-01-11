package me.rexlmanu.chromcloudcore.bootstrap;

import me.rexlmanu.chromcloudcore.ChromCloudCore;
import me.rexlmanu.chromcloudcore.ChromCloudLaunch;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;

import java.io.IOException;
import java.util.logging.Level;

public final class ChromCloudMain {

    private ChromCloudLaunch launch;

    public ChromCloudMain(ChromCloudLaunch launch) {

//        Security.setProperty( "ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
//        Security.setProperty( "ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");




        this.launch = launch;
        ChromLogger chromLogger = null;
        try {
            chromLogger = new ChromLogger();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (chromLogger == null) {
            chromLogger.doLog(Level.SEVERE, "The chromlogger cannot initialized.");
            return;
        }
        this.launch.setLogger(chromLogger);

//        if (!StringUtils.OS_ARCH.equalsIgnoreCase("windows") && StringUtils.USER_NAME.equals("root")) {
//            chromLogger.doLog(Level.SEVERE, "Please dont start the chromcloud as root user.");
//            chromLogger.doLog(Level.INFO, "For more informations: https://rexl.eu/2g5iS");
//            return;
//        }
        shutdownHook();
        ChromCloudCore.sendHeader();
        start();
    }

    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void start() {
        this.launch.onStart();
    }

    private void stop() {
        this.launch.onStop();
        ChromCloudCore.sendFooter();
    }
}
