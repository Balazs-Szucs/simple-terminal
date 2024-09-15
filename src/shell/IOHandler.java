package shell;

import java.lang.ProcessBuilder.Redirect;

/**
 * Handles input/output and error redirection for processes.
 */
public class IOHandler {

    public static void handleRedirections(ProcessBuilder pb, Command command) {
        if (command.getInputFile() != null) {
            pb.redirectInput(command.getInputFile());
        }

        if (command.getOutputFile() != null) {
            pb.redirectOutput(command.isAppendToOutput() ?
                    Redirect.appendTo(command.getOutputFile()) :
                    Redirect.to(command.getOutputFile()));
        }

        if (command.getErrorFile() != null) {
            pb.redirectError(command.isAppendToError() ?
                    Redirect.appendTo(command.getErrorFile()) :
                    Redirect.to(command.getErrorFile()));
        } else if (command.isRedirectError()) {
            pb.redirectErrorStream(true);
        }
    }
}