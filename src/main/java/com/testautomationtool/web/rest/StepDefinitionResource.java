package com.testautomationtool.web.rest;

import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.domain.request.StepDefinitionRequest;
import com.testautomationtool.repository.StepDefinitionRepository;
import com.testautomationtool.util.FileOperations;
import com.testautomationtool.util.JavascriptInjector;
import com.testautomationtool.util.ReadData;
import com.testautomationtool.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.testautomationtool.domain.StepDefinition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StepDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(StepDefinitionResource.class);

    private static final String ENTITY_NAME = "stepDefinition";

    private final Map<String, String> jsFunctions = ReadData.readJsFunctions();

    private WebDriver webDriver;

    private JavascriptExecutor jsExecutor;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private FileOperations fileOperations;

    private final StepDefinitionRepository stepDefinitionRepository;

    public StepDefinitionResource(StepDefinitionRepository stepDefinitionRepository) {
        this.stepDefinitionRepository = stepDefinitionRepository;
    }

    @PostMapping("/step-definitions")
    public ResponseEntity<StepDefinition> createStepDefinition(@RequestBody StepDefinition stepDefinition) throws URISyntaxException {
        log.debug("REST request to save StepDefinition : {}", stepDefinition);
        if (stepDefinition.getId() != null) {
            throw new BadRequestAlertException("A new stepDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StepDefinition result = stepDefinitionRepository.save(stepDefinition);
        return ResponseEntity
            .created(new URI("/api/step-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping(
        value = "/step-definitions/save-all",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<StepDefinition>> createStepDefinitions(@RequestBody StepDefinitionRequest stepDefinitions)
        throws URISyntaxException {
        log.debug("REST request to save StepDefinition : {}", stepDefinitions);
        if (stepDefinitions.getStepDefinitionList().stream().anyMatch(stepDefinition -> stepDefinition.getId() != null)) {
            throw new BadRequestAlertException("A new stepDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<StepDefinition> result = stepDefinitionRepository.saveAll(stepDefinitions.getStepDefinitionList());
        return ResponseEntity
            .created(new URI("/api/step-definitions/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.toString()))
            .body(result);
    }

    @PutMapping("/step-definitions/{id}")
    public ResponseEntity<StepDefinition> updateStepDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StepDefinition stepDefinition
    ) {
        log.debug("REST request to update StepDefinition : {}, {}", id, stepDefinition);
        if (stepDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stepDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StepDefinition result = stepDefinitionRepository.save(stepDefinition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stepDefinition.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/step-definitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StepDefinition> partialUpdateStepDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StepDefinition stepDefinition
    ) {
        log.debug("REST request to partial update StepDefinition partially : {}, {}", id, stepDefinition);
        if (stepDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stepDefinition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StepDefinition> result = stepDefinitionRepository
            .findById(stepDefinition.getId())
            .map(existingStepDefinition -> {
                if (stepDefinition.getActionType() != null) {
                    existingStepDefinition.setActionType(stepDefinition.getActionType());
                }
                if (stepDefinition.getMessage() != null) {
                    existingStepDefinition.setMessage(stepDefinition.getMessage());
                }
                if (stepDefinition.getXpathOrCssSelector() != null) {
                    existingStepDefinition.setXpathOrCssSelector(stepDefinition.getXpathOrCssSelector());
                }
                if (stepDefinition.getKeyword() != null) {
                    existingStepDefinition.setKeyword(stepDefinition.getKeyword());
                }
                if (stepDefinition.getScrollLeft() != null) {
                    existingStepDefinition.setScrollLeft(stepDefinition.getScrollLeft());
                }
                if (stepDefinition.getScrollTop() != null) {
                    existingStepDefinition.setScrollTop(stepDefinition.getScrollTop());
                }
                if (stepDefinition.getUrl() != null) {
                    existingStepDefinition.setUrl(stepDefinition.getUrl());
                }
                if (stepDefinition.getExpected() != null) {
                    existingStepDefinition.setExpected(stepDefinition.getExpected());
                }
                if (stepDefinition.getCreatedBy() != null) {
                    existingStepDefinition.setCreatedBy(stepDefinition.getCreatedBy());
                }
                if (stepDefinition.getCreatedDate() != null) {
                    existingStepDefinition.setCreatedDate(stepDefinition.getCreatedDate());
                }
                if (stepDefinition.getLastModifiedBy() != null) {
                    existingStepDefinition.setLastModifiedBy(stepDefinition.getLastModifiedBy());
                }
                if (stepDefinition.getLastModifiedDate() != null) {
                    existingStepDefinition.setLastModifiedDate(stepDefinition.getLastModifiedDate());
                }

                return existingStepDefinition;
            })
            .map(stepDefinitionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stepDefinition.getId().toString())
        );
    }

    @GetMapping("/step-definitions")
    public List<StepDefinition> getAllStepDefinitions() {
        log.debug("REST request to get all StepDefinitions");
        return stepDefinitionRepository.findAll();
    }

    @PostMapping("/step-definitions/find-by-test-scenario")
    public List<StepDefinition> getStepDefinitionsByTestScenario(@RequestBody TestScenario testScenario) {
        log.debug("REST request to get StepDefinitions by TestScenario");
        return stepDefinitionRepository.findByTestScenario(testScenario);
    }

    @GetMapping("/step-definitions/{id}")
    public ResponseEntity<StepDefinition> getStepDefinition(@PathVariable Long id) {
        log.debug("REST request to get StepDefinition : {}", id);
        Optional<StepDefinition> stepDefinition = stepDefinitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stepDefinition);
    }

    @DeleteMapping("/step-definitions/{id}")
    public ResponseEntity<Void> deleteStepDefinition(@PathVariable Long id) {
        log.debug("REST request to delete StepDefinition : {}", id);
        stepDefinitionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/step-definitions/record-start")
    public ResponseEntity<Void> startRecordingTestScenario(@RequestParam("url") String url) {
        log.debug("Recording test scenario...");
        fileOperations.removeJsonFile();

        System.setProperty("webdriver.chrome.driver", "src/main/resources/browser/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized --remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        jsExecutor = (JavascriptExecutor) webDriver;
        log.debug("DRIVER OK");
        webDriver.get(url);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        jsExecutor.executeScript(jsFunctions.get("setSessionVariables"));
        JavascriptInjector injector = new JavascriptInjector(jsExecutor, webDriver, jsFunctions);
        injector.start();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/step-definitions/record-stop")
    public ResponseEntity<List<StepDefinition>> stopRecordingTestScenario() {
        log.debug("Stopping to record test scenario...");

        jsExecutor.executeScript(jsFunctions.get("stopRecordingTestScenario"));

        webDriver.close();

        List<StepDefinition> fileContent = fileOperations.completeFileOperations();

        // TODO: record bittikten sonra ekrandan dogrulama adimlari (ekrandaki veriyi kiyaslama) ekle

        return new ResponseEntity<>(fileContent, HttpStatus.OK);
    }
}
