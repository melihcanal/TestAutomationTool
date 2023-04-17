package com.testautomationtool.util;

import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JavascriptInjector extends Thread {

    private final JavascriptExecutor jsExecutor;
    private final WebDriver webDriver;
    private final Map<String, String> jsFunctions;

    public JavascriptInjector(JavascriptExecutor jsExecutor, WebDriver webDriver, Map<String, String> jsFunctions) {
        this.jsExecutor = jsExecutor;
        this.webDriver = webDriver;
        this.jsFunctions = jsFunctions;
        this.jsExecutor.executeScript(
                jsFunctions.get("getSessionVariables") +
                '\n' +
                jsFunctions.get("saveJson") +
                '\n' +
                jsFunctions.get("createXPathFromElement") +
                '\n' +
                jsFunctions.get("getLastInputElement") +
                '\n' +
                jsFunctions.get("getElementByXpath") +
                '\n' +
                jsFunctions.get("windowListeners")
            );
    }

    @Override
    public void run() {
        String prevUrl, currentUrl = webDriver.getCurrentUrl();
        for (int i = 0; i < 300; i++) {
            prevUrl = currentUrl;
            currentUrl = webDriver.getCurrentUrl();
            if (!prevUrl.equals(currentUrl)) {
                jsExecutor.executeScript(
                    jsFunctions.get("getSessionVariables") +
                    '\n' +
                    jsFunctions.get("saveJson") +
                    '\n' +
                    jsFunctions.get("createXPathFromElement") +
                    '\n' +
                    jsFunctions.get("getLastInputElement") +
                    '\n' +
                    jsFunctions.get("getElementByXpath") +
                    '\n' +
                    jsFunctions.get("windowListeners")
                );
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
