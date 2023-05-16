package com.testautomationtool.util;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellCommand implements Runnable {

    private final Logger log;

    private final String command;

    private final Long testExecutionId;

    private final FileOperations fileOperations = new FileOperations();

    public ShellCommand(String command, Long testExecutionId) {
        this.command = command;
        this.testExecutionId = testExecutionId;
        log = LoggerFactory.getLogger(ShellCommand.class);
    }

    @Override
    public void run() {
        try {
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.directory(new File("../TestRunner"));

            Process process = pb.start();

            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            log.debug("Process exited with code " + exitCode);
            fileOperations.copySerenityReportFile(testExecutionId);
        } catch (IOException | InterruptedException e) {
            log.error("Error while executing console command ", e);
        }
    }
}
