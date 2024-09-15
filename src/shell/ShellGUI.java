package shell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple GUI for executing shell commands using PipeHandler.
 */
public class ShellGUI extends JFrame {

    private final JTextArea outputArea;  // Text area to display command output
    private final JTextField commandField;  // Text field to enter commands

    public ShellGUI() {
        setTitle("Simple Shell");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);  // Output area should be read-only
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        commandField = new JTextField();
        controlPanel.add(commandField, BorderLayout.CENTER);

        JButton executeButton = new JButton("Execute");
        JButton clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(executeButton);
        buttonPanel.add(clearButton);
        controlPanel.add(buttonPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        executeButton.addActionListener(new ExecuteCommandAction());
        clearButton.addActionListener(new ClearOutputAction());
        commandField.addKeyListener(new EnterKeyListener());
    }

    private class ExecuteCommandAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            executeCommand();
        }
    }

    private class EnterKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                executeCommand();
            }
        }
    }

    private void executeCommand() {
        String command = commandField.getText().trim();

        if (command.isEmpty()) {
            outputArea.append("Please enter a command.\n");
            return;
        }

        outputArea.append("$ " + command + "\n");

        try {
            Command[] commands = CommandParser.parse(command);
            ProcessBuilder[] pbs = convertCommandsToProcessBuilders(commands);
            runAndDisplayProcesses(pbs);
        } catch (IOException | InterruptedException ex) {
            outputArea.append("[ERROR] Failed to execute command: " + ex.getMessage() + "\n");
        }
    }

    private ProcessBuilder[] convertCommandsToProcessBuilders(Command[] commands) {
        List<ProcessBuilder> processBuilders = new ArrayList<>();
        for (Command command : commands) {
            ProcessBuilder pb = new ProcessBuilder(command.getCommandParts());
            IOHandler.handleRedirections(pb, command);
            processBuilders.add(pb);
        }
        return processBuilders.toArray(new ProcessBuilder[0]);
    }

    private void runAndDisplayProcesses(ProcessBuilder[] processBuilders) throws IOException, InterruptedException {
        Process previousProcess = null;

        for (int i = 0; i < processBuilders.length; i++) {
            ProcessBuilder pb = processBuilders[i];
            Process currentProcess = pb.start();

            if (previousProcess != null) {
                PipeHandler.pipeProcesses(previousProcess, currentProcess);
            }

            if (i == processBuilders.length - 1) {
                try (InputStream processOutput = currentProcess.getInputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = processOutput.read(buffer)) != -1) {
                        outputArea.append(new String(buffer, 0, bytesRead));
                    }
                }
            }

            previousProcess = currentProcess;
        }

        if (previousProcess != null) {
            previousProcess.waitFor();
        }
    }

    private class ClearOutputAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            commandField.setText("");  // Clear command field
            outputArea.setText("");  // Clear output area
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShellGUI shellGUI = new ShellGUI();
            shellGUI.setVisible(true);
        });
    }
}