package com.testautomationtool.web.rest;

import com.testautomationtool.domain.TestExecution;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.repository.TestExecutionRepository;
import com.testautomationtool.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    public TestExecutionResource(TestExecutionRepository testExecutionRepository) {
        this.testExecutionRepository = testExecutionRepository;
    }

    /**
     * {@code POST  /test-executions} : Create a new testExecution.
     *
     * @param testExecution the testExecution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testExecution, or with status {@code 400 (Bad Request)} if the testExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-executions")
    public ResponseEntity<TestExecution> createTestExecution(@RequestBody TestExecution testExecution) throws URISyntaxException {
        log.debug("REST request to save TestExecution : {}", testExecution);
        if (testExecution.getId() != null) {
            throw new BadRequestAlertException("A new testExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestExecution result = testExecutionRepository.save(testExecution);
        return ResponseEntity
            .created(new URI("/api/test-executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-executions/:id} : Updates an existing testExecution.
     *
     * @param id the id of the testExecution to save.
     * @param testExecution the testExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testExecution,
     * or with status {@code 400 (Bad Request)} if the testExecution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-executions/{id}")
    public ResponseEntity<TestExecution> updateTestExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestExecution testExecution
    ) throws URISyntaxException {
        log.debug("REST request to update TestExecution : {}, {}", id, testExecution);
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

    /**
     * {@code PATCH  /test-executions/:id} : Partial updates given fields of an existing testExecution, field will ignore if it is null
     *
     * @param id the id of the testExecution to save.
     * @param testExecution the testExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testExecution,
     * or with status {@code 400 (Bad Request)} if the testExecution is not valid,
     * or with status {@code 404 (Not Found)} if the testExecution is not found,
     * or with status {@code 500 (Internal Server Error)} if the testExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-executions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestExecution> partialUpdateTestExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestExecution testExecution
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestExecution partially : {}, {}", id, testExecution);
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

    /**
     * {@code GET  /test-executions} : get all the testExecutions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testExecutions in body.
     */
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

    /**
     * {@code GET  /test-executions/:id} : get the "id" testExecution.
     *
     * @param id the id of the testExecution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testExecution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-executions/{id}")
    public ResponseEntity<TestExecution> getTestExecution(@PathVariable Long id) {
        log.debug("REST request to get TestExecution : {}", id);
        Optional<TestExecution> testExecution = testExecutionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testExecution);
    }

    /**
     * {@code DELETE  /test-executions/:id} : delete the "id" testExecution.
     *
     * @param id the id of the testExecution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-executions/{id}")
    public ResponseEntity<Void> deleteTestExecution(@PathVariable Long id) {
        log.debug("REST request to delete TestExecution : {}", id);
        testExecutionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
