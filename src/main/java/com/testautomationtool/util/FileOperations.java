package com.testautomationtool.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.testautomationtool.domain.StepDefinition;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileOperations {

    public static void removeJsonFile() {
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.home").concat("/Downloads/step_definitions.json")));
            Files.deleteIfExists(Paths.get("src/main/resources/browser/step_definitions.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<StepDefinition> completeFileOperations() {
        File source = new File(System.getProperty("user.home").concat("/Downloads/step_definitions.json"));
        File dest = new File("src/main/resources/browser/step_definitions.json");

        try {
            Files.copy(source.toPath(), dest.toPath());
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/browser/step_definitions.json"));
            return new Gson().fromJson(reader, new TypeToken<List<StepDefinition>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
