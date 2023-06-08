package com.testautomationtool.service;

import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.util.FileOperations;
import com.testautomationtool.util.JavascriptInjector;
import com.testautomationtool.util.ReadData;
import com.testautomationtool.util.TimeUtil;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebDriverService {

    private final Logger log = LoggerFactory.getLogger(WebDriverService.class);

    @Autowired
    private FileOperations fileOperations;

    private WebDriver webDriver;

    private JavascriptExecutor jsExecutor;

    private JavascriptInjector injector;

    private final Map<String, String> jsFunctions = ReadData.readJsFunctions();

    public WebDriverService() {}

    public void startWebDriver(String url) {
        fileOperations.removeJsonFile();

        System.setProperty("webdriver.chrome.driver", "src/main/resources/browser/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized --remote-allow-origins=*");
        webDriver = new ChromeDriver(options);

        jsExecutor = (JavascriptExecutor) webDriver;
        log.debug("DRIVER OK");
        webDriver.get(url);

        TimeUtil.waitForTime(3000L);
        jsExecutor.executeScript(jsFunctions.get("setSessionVariables"));

        injector = new JavascriptInjector(jsExecutor, webDriver, jsFunctions);
        injector.start();
    }

    public List<StepDefinition> stopWebDriver() {
        jsExecutor.executeScript(jsFunctions.get("stopRecordingTestScenario"));
        injector.stopThread();

        TimeUtil.waitForTime(2000);
        webDriver.quit();

        return fileOperations.completeFileOperations();
    }

    public void cancelWebDriver() {
        injector.stopThread();
        webDriver.quit();
    }
}
