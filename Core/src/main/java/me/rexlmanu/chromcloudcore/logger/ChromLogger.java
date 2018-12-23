package me.rexlmanu.chromcloudcore.logger;

import jline.console.ConsoleReader;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.logger.interfaces.Logger;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * Created by Emmanuel L. on 06.09.2018
 */

@Getter
public class ChromLogger extends java.util.logging.Logger {

    @Getter
    private static ChromLogger instance;

    public static final String ANSI_RESET = "\u001B[0m", ANSI_RED = "\u001B[31m", ANSI_GREEN = "\u001B[32m", ANSI_YELLOW = "\u001B[33m",
            ANSI_PURPLE = "\u001B[35m", ANSI_CYAN = "\u001B[36m", ANSI_WHITE = "\u001B[36m", user = System.getProperty("user.name");

    @Getter
    private static ConsoleReader consoleReader = null;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Getter
    private final List<Logger> handler = new LinkedList<>();

    public ChromLogger() throws IOException, NoSuchFieldException, IllegalAccessException {
        super("ChromCloudLogger", null);

        instance = this;

        Field field = Charset.class.getDeclaredField("defaultCharset");
        field.setAccessible(true);
        field.set(null, StandardCharsets.UTF_8);

        setLevel(Level.ALL);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        final Path cloudPath = Paths.get(".chrom");
        final Path cloudLogsPath = Paths.get(".chrom", "logs");

        if (!Files.exists(cloudPath))
            Files.createDirectory(cloudPath);

        if (!Files.exists(cloudLogsPath))
            Files.createDirectory(cloudLogsPath);

        AnsiConsole.systemInstall();

        consoleReader = new ConsoleReader(System.in, System.out);
        consoleReader.setExpandEvents(false);


        setUseParentHandlers(false);
        File logsDirectory = new File(".chrom/", "logs/");
        FileHandler fileHandler = new FileHandler(logsDirectory.getCanonicalPath() + "/cloud-log-" + getEnvironment() + "-Log", 5242880, 100, false);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        addHandler(fileHandler);
    }

    public void doLog(Level level, String message) {
        printLog(level, message);
    }

    public void doLog(String message) {
        printLog(Level.INFO, message);
    }

    private void printLog(Level level, String message) {
        try {
            super.log(level, message);

            for (Logger logger : handler) {
                logger.handleConsole(message);
            }

            if (level.equals(Level.INFO)) {
                consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE +
                        ANSI_YELLOW + "[" + dateFormat.format(System.currentTimeMillis()) + "@" +
                        level.getName() + "/" + user + "] " + ANSI_RESET + ANSI_WHITE + message + ANSI_RESET);
                consoleReader.drawLine();
                consoleReader.flush();
                return;
            }
            if (level.equals(Level.WARNING)) {
                consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE +
                        ANSI_YELLOW + "[" + dateFormat.format(System.currentTimeMillis()) + "@" +
                        level.getName() + "/" + user + "] " + ANSI_RESET + ANSI_PURPLE + message + ANSI_RESET);
                consoleReader.drawLine();
                consoleReader.flush();
                return;
            }
            if (level.equals(Level.SEVERE)) {
                consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE +
                        ANSI_RED + "[" + dateFormat.format(System.currentTimeMillis()) + "@" +
                        level.getName() + "/" + user + "] " + ANSI_RESET + ANSI_CYAN + message + ANSI_RESET);
                consoleReader.drawLine();
                consoleReader.flush();
                return;
            }
            consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE +
                    ANSI_YELLOW + "[" + dateFormat.format(System.currentTimeMillis()) + "@" +
                    level.getName() + "/" + user + "] " + ANSI_RESET + ANSI_WHITE + message + ANSI_RESET);
            consoleReader.drawLine();
            consoleReader.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        for (Handler handler : this.getHandlers()) {
            handler.close();
        }
        try {
            consoleReader.killLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getEnvironment() {
        try {
            Class.forName("me.rexlmanu.chromcloudnode.ChromCloudNode");
            return "Node";
        } catch (Exception ex) {
            return "Subnode";
        }
    }
}
