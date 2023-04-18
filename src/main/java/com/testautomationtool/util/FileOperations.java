package com.testautomationtool.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileOperations {

    public static void removeJsonFile() {
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.home").concat("/Downloads/step_definitions.json")));
            Files.deleteIfExists(Paths.get("src/main/resources/browser/step_definitions.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String completeFileOperations() {
        String fileContent = "";
        File source = new File(System.getProperty("user.home").concat("/Downloads/step_definitions.json"));
        File dest = new File("src/main/resources/browser/step_definitions.json");
        try {
            Files.copy(source.toPath(), dest.toPath());
            fileContent = new String(Files.readAllBytes(Paths.get("src/main/resources/browser/step_definitions.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
