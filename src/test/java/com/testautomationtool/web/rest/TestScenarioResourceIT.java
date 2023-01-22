package com.testautomationtool.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.testautomationtool.IntegrationTest;
import com.testautomationtool.domain.TestScenario;
import com.testautomationtool.repository.TestScenarioRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestScenarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestScenarioResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEST_STEPS = "AAAAAAAAAA";
    private static final String UPDATED_TEST_STEPS = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMBER_OF_EXECUTION = 1L;
    private static final Long UPDATED_NUMBER_OF_EXECUTION = 2L;

    private static final Long DEFAULT_NUMBER_OF_PASSED = 1L;
    private static final Long UPDATED_NUMBER_OF_PASSED = 2L;

    private static final Long DEFAULT_NUMBER_OF_FAILED = 1L;
    private static final Long UPDATED_NUMBER_OF_FAILED = 2L;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/test-scenarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestScenarioRepository testScenarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestScenarioMockMvc;

    private TestScenario testScenario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestScenario createEntity(EntityManager em) {
        TestScenario testScenario = new TestScenario()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .testSteps(DEFAULT_TEST_STEPS)
            .numberOfExecution(DEFAULT_NUMBER_OF_EXECUTION)
            .numberOfPassed(DEFAULT_NUMBER_OF_PASSED)
            .numberOfFailed(DEFAULT_NUMBER_OF_FAILED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return testScenario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestScenario createUpdatedEntity(EntityManager em) {
        TestScenario testScenario = new TestScenario()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .testSteps(UPDATED_TEST_STEPS)
            .numberOfExecution(UPDATED_NUMBER_OF_EXECUTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .numberOfFailed(UPDATED_NUMBER_OF_FAILED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return testScenario;
    }

    @BeforeEach
    public void initTest() {
        testScenario = createEntity(em);
    }

    @Test
    @Transactional
    void createTestScenario() throws Exception {
        int databaseSizeBeforeCreate = testScenarioRepository.findAll().size();
        // Create the TestScenario
        restTestScenarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isCreated());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeCreate + 1);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestScenario.getTestSteps()).isEqualTo(DEFAULT_TEST_STEPS);
        assertThat(testTestScenario.getNumberOfExecution()).isEqualTo(DEFAULT_NUMBER_OF_EXECUTION);
        assertThat(testTestScenario.getNumberOfPassed()).isEqualTo(DEFAULT_NUMBER_OF_PASSED);
        assertThat(testTestScenario.getNumberOfFailed()).isEqualTo(DEFAULT_NUMBER_OF_FAILED);
        assertThat(testTestScenario.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestScenario.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTestScenario.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestScenario.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTestScenarioWithExistingId() throws Exception {
        // Create the TestScenario with an existing ID
        testScenario.setId(1L);

        int databaseSizeBeforeCreate = testScenarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestScenarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestScenarios() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        // Get all the testScenarioList
        restTestScenarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testScenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].testSteps").value(hasItem(DEFAULT_TEST_STEPS.toString())))
            .andExpect(jsonPath("$.[*].numberOfExecution").value(hasItem(DEFAULT_NUMBER_OF_EXECUTION.intValue())))
            .andExpect(jsonPath("$.[*].numberOfPassed").value(hasItem(DEFAULT_NUMBER_OF_PASSED.intValue())))
            .andExpect(jsonPath("$.[*].numberOfFailed").value(hasItem(DEFAULT_NUMBER_OF_FAILED.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTestScenario() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        // Get the testScenario
        restTestScenarioMockMvc
            .perform(get(ENTITY_API_URL_ID, testScenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testScenario.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.testSteps").value(DEFAULT_TEST_STEPS.toString()))
            .andExpect(jsonPath("$.numberOfExecution").value(DEFAULT_NUMBER_OF_EXECUTION.intValue()))
            .andExpect(jsonPath("$.numberOfPassed").value(DEFAULT_NUMBER_OF_PASSED.intValue()))
            .andExpect(jsonPath("$.numberOfFailed").value(DEFAULT_NUMBER_OF_FAILED.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTestScenario() throws Exception {
        // Get the testScenario
        restTestScenarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestScenario() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        // Update the testScenario
        TestScenario updatedTestScenario = testScenarioRepository.findById(testScenario.getId()).get();
        // Disconnect from session so that the updates on updatedTestScenario are not directly saved in db
        em.detach(updatedTestScenario);
        updatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .testSteps(UPDATED_TEST_STEPS)
            .numberOfExecution(UPDATED_NUMBER_OF_EXECUTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .numberOfFailed(UPDATED_NUMBER_OF_FAILED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestScenario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestScenario))
            )
            .andExpect(status().isOk());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestScenario.getTestSteps()).isEqualTo(UPDATED_TEST_STEPS);
        assertThat(testTestScenario.getNumberOfExecution()).isEqualTo(UPDATED_NUMBER_OF_EXECUTION);
        assertThat(testTestScenario.getNumberOfPassed()).isEqualTo(UPDATED_NUMBER_OF_PASSED);
        assertThat(testTestScenario.getNumberOfFailed()).isEqualTo(UPDATED_NUMBER_OF_FAILED);
        assertThat(testTestScenario.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestScenario.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestScenario.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestScenario.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testScenario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestScenarioWithPatch() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        // Update the testScenario using partial update
        TestScenario partialUpdatedTestScenario = new TestScenario();
        partialUpdatedTestScenario.setId(testScenario.getId());

        partialUpdatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfExecution(UPDATED_NUMBER_OF_EXECUTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .createdBy(UPDATED_CREATED_BY);

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestScenario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestScenario))
            )
            .andExpect(status().isOk());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestScenario.getTestSteps()).isEqualTo(DEFAULT_TEST_STEPS);
        assertThat(testTestScenario.getNumberOfExecution()).isEqualTo(UPDATED_NUMBER_OF_EXECUTION);
        assertThat(testTestScenario.getNumberOfPassed()).isEqualTo(UPDATED_NUMBER_OF_PASSED);
        assertThat(testTestScenario.getNumberOfFailed()).isEqualTo(DEFAULT_NUMBER_OF_FAILED);
        assertThat(testTestScenario.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestScenario.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTestScenario.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestScenario.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTestScenarioWithPatch() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        // Update the testScenario using partial update
        TestScenario partialUpdatedTestScenario = new TestScenario();
        partialUpdatedTestScenario.setId(testScenario.getId());

        partialUpdatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .testSteps(UPDATED_TEST_STEPS)
            .numberOfExecution(UPDATED_NUMBER_OF_EXECUTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .numberOfFailed(UPDATED_NUMBER_OF_FAILED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestScenario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestScenario))
            )
            .andExpect(status().isOk());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestScenario.getTestSteps()).isEqualTo(UPDATED_TEST_STEPS);
        assertThat(testTestScenario.getNumberOfExecution()).isEqualTo(UPDATED_NUMBER_OF_EXECUTION);
        assertThat(testTestScenario.getNumberOfPassed()).isEqualTo(UPDATED_NUMBER_OF_PASSED);
        assertThat(testTestScenario.getNumberOfFailed()).isEqualTo(UPDATED_NUMBER_OF_FAILED);
        assertThat(testTestScenario.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestScenario.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestScenario.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestScenario.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testScenario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestScenario in the database
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestScenario() throws Exception {
        // Initialize the database
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeDelete = testScenarioRepository.findAll().size();

        // Delete the testScenario
        restTestScenarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, testScenario.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
