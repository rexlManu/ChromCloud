package me.rexlmanu.chromcloudcore.commands;

import com.google.common.collect.Maps;
import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.rexlmanu.chromcloudcore.commands.defaults.ClearCommand;
import me.rexlmanu.chromcloudcore.commands.defaults.HelpCommand;
import me.rexlmanu.chromcloudcore.commands.defaults.StopCommand;
import me.rexlmanu.chromcloudcore.commands.response.Response;
import me.rexlmanu.chromcloudcore.commands.sender.CommandSender;
import me.rexlmanu.chromcloudcore.commands.sender.console.ConsoleCommandSender;
import me.rexlmanu.chromcloudcore.logger.ChromLogger;
import me.rexlmanu.chromcloudcore.utility.async.AsyncSession;
import me.rexlmanu.chromcloudcore.utility.string.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

@UtilityClass
public final class CommandManager {

    @Getter
    private Map<String, Command> commands;

    static {
        commands = Maps.newConcurrentMap();
    }

    public void init() {
        AsyncSession.getInstance().executeAsync(CommandManager::runListen);
        registerCommand("help", new HelpCommand(), "commands");
        registerCommand("clear", new ClearCommand(), "clearscreen", "c");
        registerCommand("stop", new StopCommand(), "shutdown", "quit", "kill");
    }

    public void sendHelpCommand() {
        ChromLogger.getInstance().doLog(Level.INFO, "Please type 'help' for the command list.");
    }

    public void setDefaultPrompt(String prefix) {
        ChromLogger.getConsoleReader().setPrompt(StringUtils.USER_NAME + "@" + prefix + ": # ");
    }

    public boolean dispatchCommand(String commandName, CommandSender commandSender) {
        final String[] param = commandName.split(" ");
        if (!(param.length > 0)) return false;
        if (commands.containsKey(param[0].toLowerCase())) {
            final Command command = getCommand(param[0]);
            if (command instanceof ConsoleCommand && !(commandSender instanceof ConsoleCommandSender))
                commandSender.sendResponse(command, Response.create().error("This command is only for the console."));
            else
                commandSender.sendResponse(command, command.execute(commandSender, param));
            return true;
        } else
            return false;
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }

    public void registerCommand(String name, Command command) {
        registerCommand(name, command, new String[]{});
    }

    public void registerCommand(String name, Command command, String... aliases) {
        commands.put(name, command);
        if (aliases != null)
            for (String alias : aliases)
                commands.put(alias, command);

    }

    private void runListen() {
        final ConsoleReader consoleReader = ChromLogger.getConsoleReader();
        try {
            String line = consoleReader.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    if (!dispatchCommand(line, new ConsoleCommandSender()))
                        sendHelpCommand();

                }
                line = consoleReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
