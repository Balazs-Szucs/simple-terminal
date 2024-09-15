package shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles piping between processes and measures execution time.
 */
public class PipeHandler {

    public static void runAndMeasureProcesses(ProcessBuilder[] processBuilders) throws IOException, InterruptedException {
        long startTime = System.nanoTime();

        Process previousProcess = null;

        for (int i = 0; i < processBuilders.length; i++) {
            ProcessBuilder pb = processBuilders[i];
            Process currentProcess = pb.start();

            if (previousProcess != null) {
                pipeProcesses(previousProcess, currentProcess);
            }

            if (i == processBuilders.length - 1) {
                try (InputStream processOutput = currentProcess.getInputStream()) {
                    processOutput.transferTo(System.out);
                }
            }

            previousProcess = currentProcess;
        }

        if (previousProcess != null) {
            previousProcess.waitFor();
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Command execution took %.2f milliseconds.%n", elapsedTime / 1_000_000.0);
    }
    public static void pipeProcesses(Process fromProcess, Process toProcess) {
        new Thread(() -> {
            try (InputStream from = fromProcess.getInputStream(); OutputStream to = toProcess.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = from.read(buffer)) != -1) {
                    to.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                System.err.println("I/O error during pipe operation: " + e.getMessage());
            }
        }).start();
    }
}