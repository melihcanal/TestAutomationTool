package com.testautomationtool.util;

import org.openqa.selenium.JavascriptExecutor;

public class JavascriptInjector extends Thread {

    private final JavascriptExecutor jsExecutor;

    public JavascriptInjector(JavascriptExecutor jsExecutor) {
        this.jsExecutor = jsExecutor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            jsExecutor.executeScript(
                JSFunctions.getSessionVariables +
                JSFunctions.saveJson +
                JSFunctions.createXPathFromElement +
                JSFunctions.getLastInputElement +
                JSFunctions.windowListeners
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
