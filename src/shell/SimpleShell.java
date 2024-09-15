package shell;

import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 * The main class for the SimpleShell application.
 */
public class SimpleShell {

    private static final String PROMPT = "$ ";
    private static final String CMD_EXIT = "exit";

    private static final File currentDirectory = new File(System.getProperty("user.dir"));
    public static void main(String[] args) {
        System.out.println("SimpleShell v0.20");

        final Scanner input = new Scanner(System.in);
        Map<String, String> environmentAddOns = new HashMap<>();
        boolean isExit = false;

        while (!isExit) {
            System.out.print(PROMPT);
            String command = input.nextLine().trim();
            Command[] parsedCommand = CommandParser.parse(command);

            if (parsedCommand.length == 0) {
                continue;
            }

            if (parsedCommand[0].getName().equals(CMD_EXIT)) {
                isExit = true;
            } else {
                CommandExecutor.executeCommands(parsedCommand, environmentAddOns, currentDirectory);
            }
        }
        input.close();
    }
}