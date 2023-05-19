package com.testautomationtool.util;

import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JavascriptInjector extends Thread {

    private volatile boolean running = true;
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
        while (running) {
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
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            TimeUtil.waitForTime(1000);
        }
    }

    public void stopThread() {
        running = false;
    }
}
