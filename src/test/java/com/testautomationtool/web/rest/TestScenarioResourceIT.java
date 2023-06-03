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

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestScenarioResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private TestScenarioRepository testScenarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestScenarioMockMvc;

    private TestScenario testScenario;

    public static TestScenario createEntity(EntityManager em) {
        return new TestScenario()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .numberOfExecution(DEFAULT_NUMBER_OF_EXECUTION)
            .numberOfPassed(DEFAULT_NUMBER_OF_PASSED)
            .numberOfFailed(DEFAULT_NUMBER_OF_FAILED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    public static TestScenario createUpdatedEntity(EntityManager em) {
        return new TestScenario()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfExecution(UPDATED_NUMBER_OF_EXECUTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .numberOfFailed(UPDATED_NUMBER_OF_FAILED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        testScenario = createEntity(em);
    }

    @Test
    @Transactional
    void createTestScenario() throws Exception {
        int databaseSizeBeforeCreate = testScenarioRepository.findAll().size();
        restTestScenarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isCreated());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeCreate + 1);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
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
        testScenario.setId(1L);

        int databaseSizeBeforeCreate = testScenarioRepository.findAll().size();

        restTestScenarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestScenarios() throws Exception {
        testScenarioRepository.saveAndFlush(testScenario);

        restTestScenarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testScenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
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
        testScenarioRepository.saveAndFlush(testScenario);

        restTestScenarioMockMvc
            .perform(get(ENTITY_API_URL_ID, testScenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testScenario.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
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
        restTestScenarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestScenario() throws Exception {
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        TestScenario updatedTestScenario = testScenarioRepository.findById(testScenario.getId()).get();

        em.detach(updatedTestScenario);
        updatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
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

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
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

        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testScenario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        restTestScenarioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isMethodNotAllowed());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestScenarioWithPatch() throws Exception {
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        TestScenario partialUpdatedTestScenario = new TestScenario();
        partialUpdatedTestScenario.setId(testScenario.getId());

        partialUpdatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfPassed(UPDATED_NUMBER_OF_PASSED)
            .numberOfFailed(UPDATED_NUMBER_OF_FAILED)
            .createdDate(UPDATED_CREATED_DATE);

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestScenario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestScenario))
            )
            .andExpect(status().isOk());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestScenario.getNumberOfExecution()).isEqualTo(DEFAULT_NUMBER_OF_EXECUTION);
        assertThat(testTestScenario.getNumberOfPassed()).isEqualTo(UPDATED_NUMBER_OF_PASSED);
        assertThat(testTestScenario.getNumberOfFailed()).isEqualTo(UPDATED_NUMBER_OF_FAILED);
        assertThat(testTestScenario.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestScenario.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTestScenario.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTestScenario.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTestScenarioWithPatch() throws Exception {
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();

        TestScenario partialUpdatedTestScenario = new TestScenario();
        partialUpdatedTestScenario.setId(testScenario.getId());

        partialUpdatedTestScenario
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
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

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
        TestScenario testTestScenario = testScenarioList.get(testScenarioList.size() - 1);
        assertThat(testTestScenario.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestScenario.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
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

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testScenario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isBadRequest());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestScenario() throws Exception {
        int databaseSizeBeforeUpdate = testScenarioRepository.findAll().size();
        testScenario.setId(count.incrementAndGet());

        restTestScenarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testScenario))
            )
            .andExpect(status().isMethodNotAllowed());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestScenario() throws Exception {
        testScenarioRepository.saveAndFlush(testScenario);

        int databaseSizeBeforeDelete = testScenarioRepository.findAll().size();

        restTestScenarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, testScenario.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<TestScenario> testScenarioList = testScenarioRepository.findAll();
        assertThat(testScenarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
