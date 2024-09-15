package shell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A parsed command that may have its input/output/error redirected.
 */
public class Command {

    private final String name;
    private String[] arguments;

    private String inputFileName;
    private String outputFileName;
    private boolean appendToOutput;

    private String errorFileName;
    private boolean appendToError;
    private boolean redirectError;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String[] getArguments() {
        return arguments != null ? Arrays.copyOf(arguments, arguments.length) : null;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments != null ? Arrays.copyOf(arguments, arguments.length) : null;
    }

    public List<String> getCommandParts() {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(getName());
        if (arguments != null) {
            commandParts.addAll(Arrays.asList(getArguments()));
        }
        return commandParts;
    }

    public File getInputFile() {
        return inputFileName != null ? new File(inputFileName) : null;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public File getOutputFile() {
        return outputFileName != null ? new File(outputFileName) : null;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public boolean isAppendToOutput() {
        return appendToOutput;
    }

    public void setAppendToOutput(boolean appendToOutput) {
        this.appendToOutput = appendToOutput;
    }

    public File getErrorFile() {
        return errorFileName != null ? new File(errorFileName) : null;
    }

    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName;
    }

    public boolean isAppendToError() {
        return appendToError;
    }

    public void setAppendToError(boolean appendToError) {
        this.appendToError = appendToError;
    }

    public boolean isRedirectError() {
        return redirectError;
    }

    public void setRedirectError(boolean redirectError) {
        this.redirectError = redirectError;
    }
}