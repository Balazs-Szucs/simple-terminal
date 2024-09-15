package shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to parse user commands into Command objects.
 */
public class CommandParser {

    public static Command[] parse(String command) {
        String[] subCommands = command.split("\\|");
        List<Command> commandList = new ArrayList<>();

        for (String subCommand : subCommands) {
            String[] parts = subCommand.trim().split("\\s+");
            Command cmd = new Command(parts[0]);

            if (parts.length > 1) {
                String[] args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, parts.length - 1);
                cmd.setArguments(args);
            }

            commandList.add(cmd);
        }
        return commandList.toArray(new Command[0]);
    }
}