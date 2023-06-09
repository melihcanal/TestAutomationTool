package com.testautomationtool.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.typeadapter.StepDefinitionAdapter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileOperations {

    private final Logger log = LoggerFactory.getLogger(FileOperations.class);

    public FileOperations() {}

    public void removeJsonFile() {
        log.debug("Removing existing step definition file");
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.home").concat("/Downloads/step_definitions.json")));
            Files.deleteIfExists(Paths.get("src/main/resources/browser/step_definitions.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StepDefinition> completeFileOperations() {
        log.debug("Copying step definition file to resource directory");
        try {
            File source = new File(System.getProperty("user.home").concat("/Downloads/step_definitions.json"));
            File dest = new File("src/main/resources/browser/step_definitions.json");
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/browser/step_definitions.json"));
            Gson gson = new GsonBuilder().registerTypeAdapter(StepDefinition.class, new StepDefinitionAdapter()).create();
            return gson.fromJson(reader, new TypeToken<ArrayList<StepDefinition>>() {}.getType());
        } catch (IOException e) {
            log.error("IOException at completeFileOperations ", e);
            return null;
        }
    }

    public void createStepDefinitionJsonFile(List<StepDefinition> stepDefinitionList) {
        String stepDefinitionListJson = JsonConverter.convertStepDefinitionListToJson(stepDefinitionList);

        try (FileWriter fileWriter = new FileWriter("src/main/resources/browser/test_execution_prepare.json")) {
            fileWriter.write(stepDefinitionListJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean copyJsonFileToExecuteTest() {
        log.debug("Copying step definition file to TestRunner directory");
        try {
            File source = new File("src/main/resources/browser/test_execution_prepare.json");
            File dest = new File("../TestRunner/src/test/resources/step_definitions.json");
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("IOException at completeFileOperations ", e);
            return false;
        }
    }

    public void copySerenityReportFile(Long testExecutionId) {
        log.debug("Copying serenity report files to resource directory");
        try {
            File sourceDir = new File("../TestRunner/target/site/serenity");
            File targetDir = new File("src/main/resources/static/test-execution-report/serenity");
            FileUtils.copyDirectory(sourceDir, targetDir);
            boolean result = targetDir.renameTo(new File("src/main/resources/static/test-execution-report/" + testExecutionId));
            if (result) {
                log.debug("Copy operation is successful");
            } else {
                log.debug("Copy operation is failed");
            }
        } catch (IOException exception) {
            log.error("IOException while copying files:", exception);
        }
    }
}
