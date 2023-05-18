package com.testautomationtool.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.testautomationtool.IntegrationTest;
import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.enumeration.ActionType;
import com.testautomationtool.repository.StepDefinitionRepository;
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
class StepDefinitionResourceIT {

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.CLICK;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.SEND_KEYS;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_XPATH_OR_CSS_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_XPATH_OR_CSS_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCROLL_LEFT = 1;
    private static final Integer UPDATED_SCROLL_LEFT = 2;

    private static final Integer DEFAULT_SCROLL_TOP = 1;
    private static final Integer UPDATED_SCROLL_TOP = 2;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EXPECTED = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/step-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StepDefinitionRepository stepDefinitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStepDefinitionMockMvc;

    private StepDefinition stepDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepDefinition createEntity(EntityManager em) {
        StepDefinition stepDefinition = new StepDefinition()
            .actionType(DEFAULT_ACTION_TYPE)
            .message(DEFAULT_MESSAGE)
            .xpathOrCssSelector(DEFAULT_XPATH_OR_CSS_SELECTOR)
            .keyword(DEFAULT_KEYWORD)
            .scrollLeft(DEFAULT_SCROLL_LEFT)
            .scrollTop(DEFAULT_SCROLL_TOP)
            .url(DEFAULT_URL)
            .expected(DEFAULT_EXPECTED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return stepDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepDefinition createUpdatedEntity(EntityManager em) {
        StepDefinition stepDefinition = new StepDefinition()
            .actionType(UPDATED_ACTION_TYPE)
            .message(UPDATED_MESSAGE)
            .xpathOrCssSelector(UPDATED_XPATH_OR_CSS_SELECTOR)
            .keyword(UPDATED_KEYWORD)
            .scrollLeft(UPDATED_SCROLL_LEFT)
            .scrollTop(UPDATED_SCROLL_TOP)
            .url(UPDATED_URL)
            .expected(UPDATED_EXPECTED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return stepDefinition;
    }

    @BeforeEach
    public void initTest() {
        stepDefinition = createEntity(em);
    }

    @Test
    @Transactional
    void createStepDefinition() throws Exception {
        int databaseSizeBeforeCreate = stepDefinitionRepository.findAll().size();
        // Create the StepDefinition
        restStepDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isCreated());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        StepDefinition testStepDefinition = stepDefinitionList.get(stepDefinitionList.size() - 1);
        assertThat(testStepDefinition.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testStepDefinition.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testStepDefinition.getXpathOrCssSelector()).isEqualTo(DEFAULT_XPATH_OR_CSS_SELECTOR);
        assertThat(testStepDefinition.getKeyword()).isEqualTo(DEFAULT_KEYWORD);
        assertThat(testStepDefinition.getScrollLeft()).isEqualTo(DEFAULT_SCROLL_LEFT);
        assertThat(testStepDefinition.getScrollTop()).isEqualTo(DEFAULT_SCROLL_TOP);
        assertThat(testStepDefinition.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testStepDefinition.getExpected()).isEqualTo(DEFAULT_EXPECTED);
        assertThat(testStepDefinition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStepDefinition.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStepDefinition.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStepDefinition.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStepDefinitionWithExistingId() throws Exception {
        // Create the StepDefinition with an existing ID
        stepDefinition.setId(1L);

        int databaseSizeBeforeCreate = stepDefinitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStepDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStepDefinitions() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        // Get all the stepDefinitionList
        restStepDefinitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stepDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].xpathOrCssSelector").value(hasItem(DEFAULT_XPATH_OR_CSS_SELECTOR)))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].scrollLeft").value(hasItem(DEFAULT_SCROLL_LEFT)))
            .andExpect(jsonPath("$.[*].scrollTop").value(hasItem(DEFAULT_SCROLL_TOP)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].expected").value(hasItem(DEFAULT_EXPECTED)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStepDefinition() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        // Get the stepDefinition
        restStepDefinitionMockMvc
            .perform(get(ENTITY_API_URL_ID, stepDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stepDefinition.getId().intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.xpathOrCssSelector").value(DEFAULT_XPATH_OR_CSS_SELECTOR))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.scrollLeft").value(DEFAULT_SCROLL_LEFT))
            .andExpect(jsonPath("$.scrollTop").value(DEFAULT_SCROLL_TOP))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.expected").value(DEFAULT_EXPECTED))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStepDefinition() throws Exception {
        // Get the stepDefinition
        restStepDefinitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStepDefinition() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();

        // Update the stepDefinition
        StepDefinition updatedStepDefinition = stepDefinitionRepository.findById(stepDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedStepDefinition are not directly saved in db
        em.detach(updatedStepDefinition);
        updatedStepDefinition
            .actionType(UPDATED_ACTION_TYPE)
            .message(UPDATED_MESSAGE)
            .xpathOrCssSelector(UPDATED_XPATH_OR_CSS_SELECTOR)
            .keyword(UPDATED_KEYWORD)
            .scrollLeft(UPDATED_SCROLL_LEFT)
            .scrollTop(UPDATED_SCROLL_TOP)
            .url(UPDATED_URL)
            .expected(UPDATED_EXPECTED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStepDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStepDefinition.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStepDefinition))
            )
            .andExpect(status().isOk());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
        StepDefinition testStepDefinition = stepDefinitionList.get(stepDefinitionList.size() - 1);
        assertThat(testStepDefinition.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testStepDefinition.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testStepDefinition.getXpathOrCssSelector()).isEqualTo(UPDATED_XPATH_OR_CSS_SELECTOR);
        assertThat(testStepDefinition.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testStepDefinition.getScrollLeft()).isEqualTo(UPDATED_SCROLL_LEFT);
        assertThat(testStepDefinition.getScrollTop()).isEqualTo(UPDATED_SCROLL_TOP);
        assertThat(testStepDefinition.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testStepDefinition.getExpected()).isEqualTo(UPDATED_EXPECTED);
        assertThat(testStepDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStepDefinition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStepDefinition.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStepDefinition.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stepDefinition.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStepDefinitionWithPatch() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();

        // Update the stepDefinition using partial update
        StepDefinition partialUpdatedStepDefinition = new StepDefinition();
        partialUpdatedStepDefinition.setId(stepDefinition.getId());

        partialUpdatedStepDefinition
            .actionType(UPDATED_ACTION_TYPE)
            .keyword(UPDATED_KEYWORD)
            .scrollTop(UPDATED_SCROLL_TOP)
            .expected(UPDATED_EXPECTED)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStepDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStepDefinition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStepDefinition))
            )
            .andExpect(status().isOk());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
        StepDefinition testStepDefinition = stepDefinitionList.get(stepDefinitionList.size() - 1);
        assertThat(testStepDefinition.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testStepDefinition.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testStepDefinition.getXpathOrCssSelector()).isEqualTo(DEFAULT_XPATH_OR_CSS_SELECTOR);
        assertThat(testStepDefinition.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testStepDefinition.getScrollLeft()).isEqualTo(DEFAULT_SCROLL_LEFT);
        assertThat(testStepDefinition.getScrollTop()).isEqualTo(UPDATED_SCROLL_TOP);
        assertThat(testStepDefinition.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testStepDefinition.getExpected()).isEqualTo(UPDATED_EXPECTED);
        assertThat(testStepDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStepDefinition.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStepDefinition.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStepDefinition.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStepDefinitionWithPatch() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();

        // Update the stepDefinition using partial update
        StepDefinition partialUpdatedStepDefinition = new StepDefinition();
        partialUpdatedStepDefinition.setId(stepDefinition.getId());

        partialUpdatedStepDefinition
            .actionType(UPDATED_ACTION_TYPE)
            .message(UPDATED_MESSAGE)
            .xpathOrCssSelector(UPDATED_XPATH_OR_CSS_SELECTOR)
            .keyword(UPDATED_KEYWORD)
            .scrollLeft(UPDATED_SCROLL_LEFT)
            .scrollTop(UPDATED_SCROLL_TOP)
            .url(UPDATED_URL)
            .expected(UPDATED_EXPECTED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStepDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStepDefinition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStepDefinition))
            )
            .andExpect(status().isOk());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
        StepDefinition testStepDefinition = stepDefinitionList.get(stepDefinitionList.size() - 1);
        assertThat(testStepDefinition.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testStepDefinition.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testStepDefinition.getXpathOrCssSelector()).isEqualTo(UPDATED_XPATH_OR_CSS_SELECTOR);
        assertThat(testStepDefinition.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testStepDefinition.getScrollLeft()).isEqualTo(UPDATED_SCROLL_LEFT);
        assertThat(testStepDefinition.getScrollTop()).isEqualTo(UPDATED_SCROLL_TOP);
        assertThat(testStepDefinition.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testStepDefinition.getExpected()).isEqualTo(UPDATED_EXPECTED);
        assertThat(testStepDefinition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStepDefinition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStepDefinition.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStepDefinition.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stepDefinition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isBadRequest());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStepDefinition() throws Exception {
        int databaseSizeBeforeUpdate = stepDefinitionRepository.findAll().size();
        stepDefinition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStepDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stepDefinition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StepDefinition in the database
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStepDefinition() throws Exception {
        // Initialize the database
        stepDefinitionRepository.saveAndFlush(stepDefinition);

        int databaseSizeBeforeDelete = stepDefinitionRepository.findAll().size();

        // Delete the stepDefinition
        restStepDefinitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, stepDefinition.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StepDefinition> stepDefinitionList = stepDefinitionRepository.findAll();
        assertThat(stepDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
