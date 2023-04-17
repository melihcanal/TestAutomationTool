package com.testautomationtool.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadData {

    public static Map<String, String> readJsFunctions() {
        Map<String, String> browserFunctions = new HashMap<>();
        try {
            File file = new File("src/main/resources/browser/browser_functions.js");
            Scanner scanner = new Scanner(file);
            String function = "";
            String functionName = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("//")) {
                    if (!functionName.equals("")) browserFunctions.put(functionName, function);
                    functionName = line.replace("//", "").trim();
                    function = "";
                } else if (!line.equals("\n") && !line.equals("")) {
                    function = function.concat(line).concat("\n");
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return browserFunctions;
    }
}
