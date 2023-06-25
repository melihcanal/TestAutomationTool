package com.testautomationtool.web.rest;

import com.testautomationtool.domain.TestExecution;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.domain.User;
import com.testautomationtool.repository.TestExecutionRepository;
import com.testautomationtool.repository.TestScenarioRepository;
import com.testautomationtool.security.SecurityUtils;
import com.testautomationtool.service.UserService;
import com.testautomationtool.util.FileOperations;
import com.testautomationtool.util.ShellCommand;
import com.testautomationtool.web.rest.errors.BadRequestAlertException;
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
    ) {
        log.debug("REST request to update TestExecution : {}, {}", id, testExecution);

        TestExecution execution = testExecutionRepository.findById(id).orElseThrow();
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (!userService.userHasAdminRole(userByLogin) && !execution.getTestScenario().getUser().getLogin().equals(login)) {
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
    ) {
        log.debug("REST request to partial update TestExecution partially : {}, {}", id, testExecution);

        TestExecution execution = testExecutionRepository.findById(id).orElseThrow();
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (!userService.userHasAdminRole(userByLogin) && !execution.getTestScenario().getUser().getLogin().equals(login)) {
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
        TestExecution testExecution = testExecutionRepository.findById(id).orElseThrow();

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (!userService.userHasAdminRole(userByLogin) && !testExecution.getTestScenario().getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return ResponseUtil.wrapOrNotFound(Optional.of(testExecution));
    }

    @DeleteMapping("/test-executions/{id}")
    public ResponseEntity<Void> deleteTestExecution(@PathVariable Long id) {
        log.debug("REST request to delete TestExecution : {}", id);

        TestExecution execution = testExecutionRepository.findById(id).orElseThrow();
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);

        if (!userService.userHasAdminRole(userByLogin) && !execution.getTestScenario().getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        TestScenario testScenario = execution.getTestScenario();
        testScenario.setNumberOfExecution(testScenario.getNumberOfExecution() - 1);
        if (execution.getStatus()) {
            testScenario.setNumberOfPassed(testScenario.getNumberOfPassed() - 1);
        } else {
            testScenario.setNumberOfFailed(testScenario.getNumberOfFailed() - 1);
        }
        testScenarioRepository.save(testScenario);

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

        fileOperations.createStepDefinitionJsonFile(testScenario.getStepDefinitions());

        if (fileOperations.copyJsonFileToExecuteTest()) {
            Thread thread = new Thread(new ShellCommand("mvn.cmd clean verify", testExecutionId));
            thread.start();

            testScenario.setNumberOfExecution(testScenario.getNumberOfExecution() + 1);
            testScenario.setNumberOfPassed(testScenario.getNumberOfPassed() + 1);
            testScenarioRepository.save(testScenario);

            save.setStatus(true);
            save.setMessage("Success");
            save.setReportUrl("/test-execution-report/" + testExecutionId + "/index.html");
            save = testExecutionRepository.save(save);
        } else {
            testScenario.setNumberOfExecution(testScenario.getNumberOfExecution() + 1);
            testScenario.setNumberOfFailed(testScenario.getNumberOfFailed() + 1);
            testScenarioRepository.save(testScenario);

            save.setStatus(false);
            save.setMessage("Failed");
            save = testExecutionRepository.saveAndFlush(save);
            return new ResponseEntity<>(save, HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
