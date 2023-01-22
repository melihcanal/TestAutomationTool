package com.testautomationtool.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.testautomationtool.IntegrationTest;
import com.testautomationtool.domain.TestExecution;
import com.testautomationtool.repository.TestExecutionRepository;
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
 * Integration tests for the {@link TestExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestExecutionResourceIT {

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_URL = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/test-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestExecutionMockMvc;

    private TestExecution testExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestExecution createEntity(EntityManager em) {
        TestExecution testExecution = new TestExecution()
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE)
            .reportUrl(DEFAULT_REPORT_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return testExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestExecution createUpdatedEntity(EntityManager em) {
        TestExecution testExecution = new TestExecution()
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .reportUrl(UPDATED_REPORT_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return testExecution;
    }

    @BeforeEach
    public void initTest() {
        testExecution = createEntity(em);
    }

    @Test
    @Transactional
    void createTestExecution() throws Exception {
        int databaseSizeBeforeCreate = testExecutionRepository.findAll().size();
        // Create the TestExecution
        restTestExecutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isCreated());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeCreate + 1);
        TestExecution testTestExecution = testExecutionList.get(testExecutionList.size() - 1);
        assertThat(testTestExecution.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTestExecution.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTestExecution.getReportUrl()).isEqualTo(DEFAULT_REPORT_URL);
        assertThat(testTestExecution.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestExecution.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTestExecution.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestExecution.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTestExecutionWithExistingId() throws Exception {
        // Create the TestExecution with an existing ID
        testExecution.setId(1L);

        int databaseSizeBeforeCreate = testExecutionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestExecutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestExecutions() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        // Get all the testExecutionList
        restTestExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].reportUrl").value(hasItem(DEFAULT_REPORT_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTestExecution() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        // Get the testExecution
        restTestExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, testExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testExecution.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.reportUrl").value(DEFAULT_REPORT_URL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTestExecution() throws Exception {
        // Get the testExecution
        restTestExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestExecution() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();

        // Update the testExecution
        TestExecution updatedTestExecution = testExecutionRepository.findById(testExecution.getId()).get();
        // Disconnect from session so that the updates on updatedTestExecution are not directly saved in db
        em.detach(updatedTestExecution);
        updatedTestExecution
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .reportUrl(UPDATED_REPORT_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestExecution.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestExecution))
            )
            .andExpect(status().isOk());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
        TestExecution testTestExecution = testExecutionList.get(testExecutionList.size() - 1);
        assertThat(testTestExecution.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTestExecution.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTestExecution.getReportUrl()).isEqualTo(UPDATED_REPORT_URL);
        assertThat(testTestExecution.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestExecution.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestExecution.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestExecution.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testExecution.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestExecutionWithPatch() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();

        // Update the testExecution using partial update
        TestExecution partialUpdatedTestExecution = new TestExecution();
        partialUpdatedTestExecution.setId(testExecution.getId());

        partialUpdatedTestExecution
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .reportUrl(UPDATED_REPORT_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestExecution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestExecution))
            )
            .andExpect(status().isOk());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
        TestExecution testTestExecution = testExecutionList.get(testExecutionList.size() - 1);
        assertThat(testTestExecution.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTestExecution.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTestExecution.getReportUrl()).isEqualTo(UPDATED_REPORT_URL);
        assertThat(testTestExecution.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestExecution.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestExecution.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestExecution.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTestExecutionWithPatch() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();

        // Update the testExecution using partial update
        TestExecution partialUpdatedTestExecution = new TestExecution();
        partialUpdatedTestExecution.setId(testExecution.getId());

        partialUpdatedTestExecution
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .reportUrl(UPDATED_REPORT_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTestExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestExecution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestExecution))
            )
            .andExpect(status().isOk());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
        TestExecution testTestExecution = testExecutionList.get(testExecutionList.size() - 1);
        assertThat(testTestExecution.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTestExecution.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTestExecution.getReportUrl()).isEqualTo(UPDATED_REPORT_URL);
        assertThat(testTestExecution.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestExecution.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestExecution.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTestExecution.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testExecution.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestExecution() throws Exception {
        int databaseSizeBeforeUpdate = testExecutionRepository.findAll().size();
        testExecution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testExecution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestExecution in the database
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestExecution() throws Exception {
        // Initialize the database
        testExecutionRepository.saveAndFlush(testExecution);

        int databaseSizeBeforeDelete = testExecutionRepository.findAll().size();

        // Delete the testExecution
        restTestExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, testExecution.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestExecution> testExecutionList = testExecutionRepository.findAll();
        assertThat(testExecutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
