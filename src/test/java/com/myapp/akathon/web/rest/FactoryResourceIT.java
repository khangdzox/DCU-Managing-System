package com.myapp.akathon.web.rest;

import static com.myapp.akathon.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.akathon.IntegrationTest;
import com.myapp.akathon.domain.Factory;
import com.myapp.akathon.repository.FactoryRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link FactoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/factories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactoryMockMvc;

    private Factory factory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factory createEntity(EntityManager em) {
        Factory factory = new Factory().name(DEFAULT_NAME).dateCreated(DEFAULT_DATE_CREATED);
        return factory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factory createUpdatedEntity(EntityManager em) {
        Factory factory = new Factory().name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);
        return factory;
    }

    @BeforeEach
    public void initTest() {
        factory = createEntity(em);
    }

    @Test
    @Transactional
    void createFactory() throws Exception {
        int databaseSizeBeforeCreate = factoryRepository.findAll().size();
        // Create the Factory
        restFactoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factory)))
            .andExpect(status().isCreated());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeCreate + 1);
        Factory testFactory = factoryList.get(factoryList.size() - 1);
        assertThat(testFactory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFactory.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    void createFactoryWithExistingId() throws Exception {
        // Create the Factory with an existing ID
        factory.setId(1L);

        int databaseSizeBeforeCreate = factoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factory)))
            .andExpect(status().isBadRequest());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = factoryRepository.findAll().size();
        // set the field null
        factory.setName(null);

        // Create the Factory, which fails.

        restFactoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factory)))
            .andExpect(status().isBadRequest());

        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactories() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        // Get all the factoryList
        restFactoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))));
    }

    @Test
    @Transactional
    void getFactory() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        // Get the factory
        restFactoryMockMvc
            .perform(get(ENTITY_API_URL_ID, factory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingFactory() throws Exception {
        // Get the factory
        restFactoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactory() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();

        // Update the factory
        Factory updatedFactory = factoryRepository.findById(factory.getId()).get();
        // Disconnect from session so that the updates on updatedFactory are not directly saved in db
        em.detach(updatedFactory);
        updatedFactory.name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);

        restFactoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactory))
            )
            .andExpect(status().isOk());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
        Factory testFactory = factoryList.get(factoryList.size() - 1);
        assertThat(testFactory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFactory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactoryWithPatch() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();

        // Update the factory using partial update
        Factory partialUpdatedFactory = new Factory();
        partialUpdatedFactory.setId(factory.getId());

        partialUpdatedFactory.dateCreated(UPDATED_DATE_CREATED);

        restFactoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactory))
            )
            .andExpect(status().isOk());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
        Factory testFactory = factoryList.get(factoryList.size() - 1);
        assertThat(testFactory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFactory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateFactoryWithPatch() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();

        // Update the factory using partial update
        Factory partialUpdatedFactory = new Factory();
        partialUpdatedFactory.setId(factory.getId());

        partialUpdatedFactory.name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);

        restFactoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactory))
            )
            .andExpect(status().isOk());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
        Factory testFactory = factoryList.get(factoryList.size() - 1);
        assertThat(testFactory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFactory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactory() throws Exception {
        int databaseSizeBeforeUpdate = factoryRepository.findAll().size();
        factory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factory in the database
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactory() throws Exception {
        // Initialize the database
        factoryRepository.saveAndFlush(factory);

        int databaseSizeBeforeDelete = factoryRepository.findAll().size();

        // Delete the factory
        restFactoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, factory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factory> factoryList = factoryRepository.findAll();
        assertThat(factoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
