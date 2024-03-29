package com.testautomationtool.web.rest;

import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.domain.User;
import com.testautomationtool.repository.StepDefinitionRepository;
import com.testautomationtool.repository.TestExecutionRepository;
import com.testautomationtool.repository.TestScenarioRepository;
import com.testautomationtool.security.SecurityUtils;
import com.testautomationtool.service.UserService;
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
public class TestScenarioResource {

    private final Logger log = LoggerFactory.getLogger(TestScenarioResource.class);

    private static final String ENTITY_NAME = "testScenario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestScenarioRepository testScenarioRepository;

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private StepDefinitionRepository stepDefinitionRepository;

    @Autowired
    private UserService userService;

    public TestScenarioResource(TestScenarioRepository testScenarioRepository) {
        this.testScenarioRepository = testScenarioRepository;
    }

    @PostMapping("/test-scenarios")
    public ResponseEntity<TestScenario> createTestScenario(@RequestBody TestScenario testScenario) throws URISyntaxException {
        log.debug("REST request to save TestScenario : {}", testScenario);
        if (testScenario.getId() != null) {
            throw new BadRequestAlertException("A new testScenario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testScenario.setNumberOfExecution(0L);
        testScenario.setNumberOfPassed(0L);
        testScenario.setNumberOfFailed(0L);
        TestScenario result = testScenarioRepository.save(testScenario);
        return ResponseEntity
            .created(new URI("/api/test-scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/test-scenarios/{id}")
    public ResponseEntity<TestScenario> updateTestScenario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestScenario testScenario
    ) {
        log.debug("REST request to update TestScenario : {}, {}", id, testScenario);

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);
        TestScenario scenario = testScenarioRepository.findById(id).orElseThrow();

        if (!userService.userHasAdminRole(userByLogin) && !scenario.getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

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

    @PatchMapping(value = "/test-scenarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestScenario> partialUpdateTestScenario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestScenario testScenario
    ) {
        log.debug("REST request to partial update TestScenario partially : {}, {}", id, testScenario);

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);
        TestScenario scenario = testScenarioRepository.findById(id).orElseThrow();

        if (!userService.userHasAdminRole(userByLogin) && !scenario.getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

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

    @GetMapping("/test-scenarios")
    public ResponseEntity<List<TestScenario>> getAllTestScenarios() {
        log.debug("REST request to get all TestScenarios");
        return new ResponseEntity<>(testScenarioRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test-scenarios/{id}")
    public ResponseEntity<TestScenario> getTestScenario(@PathVariable Long id) {
        log.debug("REST request to get TestScenario : {}", id);
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);
        TestScenario testScenario = testScenarioRepository.findById(id).orElseThrow();

        if (!userService.userHasAdminRole(userByLogin) && !testScenario.getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return ResponseUtil.wrapOrNotFound(Optional.of(testScenario));
    }

    @DeleteMapping("/test-scenarios/{id}")
    public ResponseEntity<Void> deleteTestScenario(@PathVariable Long id) {
        log.debug("REST request to delete TestScenario : {}", id);
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        User userByLogin = userService.getUserByLogin(login).orElse(null);
        TestScenario testScenario = testScenarioRepository.findById(id).orElseThrow();

        if (!userService.userHasAdminRole(userByLogin) && !testScenario.getUser().getLogin().equals(login)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        stepDefinitionRepository.deleteByTestScenario(testScenario);
        testExecutionRepository.deleteByTestScenario(testScenario);
        testScenarioRepository.deleteById(id);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/test-scenarios/user")
    public List<TestScenario> getTestScenariosByCurrentUser() {
        log.debug("REST request to get TestScenarios by current user");
        return testScenarioRepository.findByUserIsCurrentUser();
    }
}
