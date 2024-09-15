package shell;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Handles command execution, managing processes and timing.
 */
public class CommandExecutor {

    public static void executeCommands(Command[] commands, Map<String, String> environmentAddOns, File currentDirectory) {
        try {
            ProcessBuilder[] processBuilders = configProcesses(commands, environmentAddOns, currentDirectory);
            PipeHandler.runAndMeasureProcesses(processBuilders);
        } catch (IOException | InterruptedException e) {
            System.err.println("[ERROR] Error running command: " + e.getLocalizedMessage());
        }
    }

    private static ProcessBuilder[] configProcesses(Command[] commands, Map<String, String> envMapAddon, File currentDirectory) {
        ProcessBuilder[] processBuilders = new ProcessBuilder[commands.length];

        for (int i = 0; i < commands.length; i++) {
            Command command = commands[i];
            ProcessBuilder pb = new ProcessBuilder(command.getCommandParts());
            pb.directory(currentDirectory);
            Map<String, String> env = pb.environment();
            env.putAll(envMapAddon);

            IOHandler.handleRedirections(pb, command);
            processBuilders[i] = pb;
        }

        return processBuilders;
    }
}