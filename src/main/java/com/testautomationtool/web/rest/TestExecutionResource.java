package com.testautomationtool.web.rest;

import com.testautomationtool.domain.TestExecution;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.domain.User;
import com.testautomationtool.repository.TestExecutionRepository;
import com.testautomationtool.repository.TestScenarioRepository;
import com.testautomationtool.security.SecurityUtils;
import com.testautomationtool.service.UserService;
import com.testautomationtool.util.FileOperations;
import com.testautomationtool.util.JsonConverter;
import com.testautomationtool.util.ShellCommand;
import com.testautomationtool.web.rest.errors.BadRequestAlertException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.testautomationtool.domain.TestExecution}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TestExecutionResource {

    private final Logger log = LoggerFactory.getLogger(TestExecutionResource.class);

    private static final String ENTITY_NAME = "testExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestExecutionRepository testExecutionRepository;

    @Autowired
    private TestScenarioRepository testScenarioRepository;

    @Autowired
    private FileOperations fileOperations;

    @Autowired
    private UserService userService;

    public TestExecutionResource(TestExecutionRepository testExecutionRepository) {
        this.testExecutionRepository = testExecutionRepository;
    }

    @PostMapping("/test-executions")
    public ResponseEntity<TestExecution> createTestExecution(@RequestBody TestExecution testExecution) throws URISyntaxException {
        log.debug("REST request to save TestExecution : {}", testExecution);
        if (testExecution.getId() != null) {
            throw new BadRequestAlertException("A new testExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestExecution result = testExecutionRepository.save(testExecution);
        TestScenario testScenario = testExecution.getTestScenario();
        testScenario.setNumberOfExecution(testScenario.getNumberOfExecution() + 1);
        testScenario.setNumberOfPassed(testScenario.getNumberOfPassed() + (testExecution.getStatus() ? 1 : 0));
        testScenario.setNumberOfFailed(testScenario.getNumberOfFailed() + (testExecution.getStatus() ? 0 : 1));
        testScenarioRepository.save(testScenario);
        return ResponseEntity
            .created(new URI("/api/test-executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/test-executions/{id}")
    public ResponseEntity<TestExecution> updateTestExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestExecution testExecution
    ) throws URISyntaxException {
        log.debug("REST request to update TestExecution : {}, {}", id, testExecution);

        Optional<TestExecution> execution = testExecutionRepository.findById(id);
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (
            !userService.userHasAdminRole(userByLogin) &&
            execution.isPresent() &&
            !execution.get().getTestScenario().getUser().getLogin().equals(login)
        ) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (testExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestExecution result = testExecutionRepository.save(testExecution);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testExecution.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/test-executions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestExecution> partialUpdateTestExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestExecution testExecution
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestExecution partially : {}, {}", id, testExecution);

        Optional<TestExecution> execution = testExecutionRepository.findById(id);
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (
            !userService.userHasAdminRole(userByLogin) &&
            execution.isPresent() &&
            !execution.get().getTestScenario().getUser().getLogin().equals(login)
        ) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (testExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestExecution> result = testExecutionRepository
            .findById(testExecution.getId())
            .map(existingTestExecution -> {
                if (testExecution.getStatus() != null) {
                    existingTestExecution.setStatus(testExecution.getStatus());
                }
                if (testExecution.getMessage() != null) {
                    existingTestExecution.setMessage(testExecution.getMessage());
                }
                if (testExecution.getReportUrl() != null) {
                    existingTestExecution.setReportUrl(testExecution.getReportUrl());
                }
                if (testExecution.getCreatedBy() != null) {
                    existingTestExecution.setCreatedBy(testExecution.getCreatedBy());
                }
                if (testExecution.getCreatedDate() != null) {
                    existingTestExecution.setCreatedDate(testExecution.getCreatedDate());
                }
                if (testExecution.getLastModifiedBy() != null) {
                    existingTestExecution.setLastModifiedBy(testExecution.getLastModifiedBy());
                }
                if (testExecution.getLastModifiedDate() != null) {
                    existingTestExecution.setLastModifiedDate(testExecution.getLastModifiedDate());
                }

                return existingTestExecution;
            })
            .map(testExecutionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testExecution.getId().toString())
        );
    }

    @GetMapping("/test-executions")
    public List<TestExecution> getAllTestExecutions() {
        log.debug("REST request to get all TestExecutions");
        return testExecutionRepository.findAll();
    }

    @PostMapping("/test-executions/find-by-test-scenario")
    public List<TestExecution> getTestExecutionsByTestScenario(@RequestBody TestScenario testScenario) {
        log.debug("REST request to get TestExecution by TestScenario");
        return testExecutionRepository.findByTestScenario(testScenario);
    }

    @GetMapping("/test-executions/{id}")
    public ResponseEntity<TestExecution> getTestExecution(@PathVariable Long id) {
        log.debug("REST request to get TestExecution : {}", id);
        Optional<TestExecution> testExecution = testExecutionRepository.findById(id);

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (
            !userService.userHasAdminRole(userByLogin) &&
            testExecution.isPresent() &&
            !testExecution.get().getTestScenario().getUser().getLogin().equals(login)
        ) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return ResponseUtil.wrapOrNotFound(testExecution);
    }

    @DeleteMapping("/test-executions/{id}")
    public ResponseEntity<Void> deleteTestExecution(@PathVariable Long id) {
        log.debug("REST request to delete TestExecution : {}", id);

        Optional<TestExecution> execution = testExecutionRepository.findById(id);
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (
            !userService.userHasAdminRole(userByLogin) &&
            execution.isPresent() &&
            !execution.get().getTestScenario().getUser().getLogin().equals(login)
        ) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        testExecutionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/test-executions/execute/{id}")
    public ResponseEntity<TestExecution> executeTestScenario(@PathVariable Long id) {
        log.debug("REST request to execute TestScenario : {}", id);
        TestScenario testScenario = testScenarioRepository.findById(id).orElseThrow();
        TestExecution testExecution = new TestExecution();
        testExecution.setTestScenario(testScenario);
        TestExecution save = testExecutionRepository.saveAndFlush(testExecution);
        Long testExecutionId = save.getId();
        String stepDefinitionListJson = JsonConverter.convertStepDefinitionListToJson(testScenario.getStepDefinitions());
        try (FileWriter fileWriter = new FileWriter("src/main/resources/browser/test_execution_prepare.json")) {
            fileWriter.write(stepDefinitionListJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // TODO: return this method
        boolean success = fileOperations.copyJsonFileToExecuteTest();
        if (success) {
            Thread thread = new Thread(new ShellCommand("mvn.cmd clean verify", testExecutionId));
            thread.start();
        } else {
            return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
