package com.testautomationtool.web.rest;

import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.repository.TestScenarioRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.testautomationtool.domain.TestScenario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TestScenarioResource {

    private final Logger log = LoggerFactory.getLogger(TestScenarioResource.class);

    private static final String ENTITY_NAME = "testScenario";

    private final Map<String, String> jsFunctions = ReadData.readJsFunctions();

    private WebDriver webDriver;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestScenarioRepository testScenarioRepository;

    public TestScenarioResource(TestScenarioRepository testScenarioRepository) {
        this.testScenarioRepository = testScenarioRepository;
    }

    /**
     * {@code POST  /test-scenarios} : Create a new testScenario.
     *
     * @param testScenario the testScenario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testScenario, or with status {@code 400 (Bad Request)} if the testScenario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-scenarios")
    public ResponseEntity<TestScenario> createTestScenario(@RequestBody TestScenario testScenario) throws URISyntaxException {
        log.debug("REST request to save TestScenario : {}", testScenario);
        if (testScenario.getId() != null) {
            throw new BadRequestAlertException("A new testScenario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestScenario result = testScenarioRepository.save(testScenario);
        return ResponseEntity
            .created(new URI("/api/test-scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-scenarios/:id} : Updates an existing testScenario.
     *
     * @param id the id of the testScenario to save.
     * @param testScenario the testScenario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testScenario,
     * or with status {@code 400 (Bad Request)} if the testScenario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testScenario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-scenarios/{id}")
    public ResponseEntity<TestScenario> updateTestScenario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestScenario testScenario
    ) throws URISyntaxException {
        log.debug("REST request to update TestScenario : {}, {}", id, testScenario);
        if (testScenario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testScenario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testScenarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestScenario result = testScenarioRepository.save(testScenario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testScenario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-scenarios/:id} : Partial updates given fields of an existing testScenario, field will ignore if it is null
     *
     * @param id the id of the testScenario to save.
     * @param testScenario the testScenario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testScenario,
     * or with status {@code 400 (Bad Request)} if the testScenario is not valid,
     * or with status {@code 404 (Not Found)} if the testScenario is not found,
     * or with status {@code 500 (Internal Server Error)} if the testScenario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-scenarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestScenario> partialUpdateTestScenario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestScenario testScenario
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestScenario partially : {}, {}", id, testScenario);
        if (testScenario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testScenario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testScenarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestScenario> result = testScenarioRepository
            .findById(testScenario.getId())
            .map(existingTestScenario -> {
                if (testScenario.getTitle() != null) {
                    existingTestScenario.setTitle(testScenario.getTitle());
                }
                if (testScenario.getDescription() != null) {
                    existingTestScenario.setDescription(testScenario.getDescription());
                }
                if (testScenario.getNumberOfExecution() != null) {
                    existingTestScenario.setNumberOfExecution(testScenario.getNumberOfExecution());
                }
                if (testScenario.getNumberOfPassed() != null) {
                    existingTestScenario.setNumberOfPassed(testScenario.getNumberOfPassed());
                }
                if (testScenario.getNumberOfFailed() != null) {
                    existingTestScenario.setNumberOfFailed(testScenario.getNumberOfFailed());
                }
                if (testScenario.getCreatedBy() != null) {
                    existingTestScenario.setCreatedBy(testScenario.getCreatedBy());
                }
                if (testScenario.getCreatedDate() != null) {
                    existingTestScenario.setCreatedDate(testScenario.getCreatedDate());
                }
                if (testScenario.getLastModifiedBy() != null) {
                    existingTestScenario.setLastModifiedBy(testScenario.getLastModifiedBy());
                }
                if (testScenario.getLastModifiedDate() != null) {
                    existingTestScenario.setLastModifiedDate(testScenario.getLastModifiedDate());
                }

                return existingTestScenario;
            })
            .map(testScenarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testScenario.getId().toString())
        );
    }

    /**
     * {@code GET  /test-scenarios} : get all the testScenarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testScenarios in body.
     */
    @GetMapping("/test-scenarios")
    public List<TestScenario> getAllTestScenarios() {
        log.debug("REST request to get all TestScenarios");
        return testScenarioRepository.findAll();
    }

    @GetMapping("/test-scenarios/record-start")
    public ResponseEntity<String> startRecordingTestScenario() {
        log.debug("Recording test scenario...");
        FileOperations.removeJsonFile();

        System.setProperty("webdriver.chrome.driver", "src/main/resources/browser/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        System.out.println("DRIVER OK");
        webDriver.get("https://www.google.com");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        js.executeScript(jsFunctions.get("setSessionVariables"));
        JavascriptInjector injector = new JavascriptInjector(js, webDriver, jsFunctions);
        injector.start();

        return new ResponseEntity<String>("testResult", HttpStatus.OK);
    }

    @GetMapping("/test-scenarios/record-stop")
    public ResponseEntity<List<StepDefinition>> stopRecordingTestScenario() {
        log.debug("Stopping to record test scenario...");

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript(jsFunctions.get("stopRecordingTestScenario"));

        webDriver.close();

        List<StepDefinition> fileContent = FileOperations.completeFileOperations();

        // record bittikten sonra ekrandan dogrulama adimlari (ekrandaki veriyi kiyaslama) ekle

        return new ResponseEntity<List<StepDefinition>>(fileContent, HttpStatus.OK);
    }

    /**
     * {@code GET  /test-scenarios/:id} : get the "id" testScenario.
     *
     * @param id the id of the testScenario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testScenario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-scenarios/{id}")
    public ResponseEntity<TestScenario> getTestScenario(@PathVariable Long id) {
        log.debug("REST request to get TestScenario : {}", id);
        Optional<TestScenario> testScenario = testScenarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testScenario);
    }

    /**
     * {@code DELETE  /test-scenarios/:id} : delete the "id" testScenario.
     *
     * @param id the id of the testScenario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-scenarios/{id}")
    public ResponseEntity<Void> deleteTestScenario(@PathVariable Long id) {
        log.debug("REST request to delete TestScenario : {}", id);
        testScenarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
