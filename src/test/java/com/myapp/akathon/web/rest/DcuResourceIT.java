package com.myapp.akathon.web.rest;

import static com.myapp.akathon.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.akathon.IntegrationTest;
import com.myapp.akathon.domain.Dcu;
import com.myapp.akathon.repository.DcuRepository;
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
 * Integration tests for the {@link DcuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DcuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/dcus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DcuRepository dcuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDcuMockMvc;

    private Dcu dcu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dcu createEntity(EntityManager em) {
        Dcu dcu = new Dcu().name(DEFAULT_NAME).dateCreated(DEFAULT_DATE_CREATED);
        return dcu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dcu createUpdatedEntity(EntityManager em) {
        Dcu dcu = new Dcu().name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);
        return dcu;
    }

    @BeforeEach
    public void initTest() {
        dcu = createEntity(em);
    }

    @Test
    @Transactional
    void createDcu() throws Exception {
        int databaseSizeBeforeCreate = dcuRepository.findAll().size();
        // Create the Dcu
        restDcuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dcu)))
            .andExpect(status().isCreated());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeCreate + 1);
        Dcu testDcu = dcuList.get(dcuList.size() - 1);
        assertThat(testDcu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDcu.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    void createDcuWithExistingId() throws Exception {
        // Create the Dcu with an existing ID
        dcu.setId(1L);

        int databaseSizeBeforeCreate = dcuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDcuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dcu)))
            .andExpect(status().isBadRequest());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dcuRepository.findAll().size();
        // set the field null
        dcu.setName(null);

        // Create the Dcu, which fails.

        restDcuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dcu)))
            .andExpect(status().isBadRequest());

        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDcus() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        // Get all the dcuList
        restDcuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dcu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))));
    }

    @Test
    @Transactional
    void getDcu() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        // Get the dcu
        restDcuMockMvc
            .perform(get(ENTITY_API_URL_ID, dcu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dcu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingDcu() throws Exception {
        // Get the dcu
        restDcuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDcu() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();

        // Update the dcu
        Dcu updatedDcu = dcuRepository.findById(dcu.getId()).get();
        // Disconnect from session so that the updates on updatedDcu are not directly saved in db
        em.detach(updatedDcu);
        updatedDcu.name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);

        restDcuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDcu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDcu))
            )
            .andExpect(status().isOk());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
        Dcu testDcu = dcuList.get(dcuList.size() - 1);
        assertThat(testDcu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDcu.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dcu.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dcu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dcu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dcu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDcuWithPatch() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();

        // Update the dcu using partial update
        Dcu partialUpdatedDcu = new Dcu();
        partialUpdatedDcu.setId(dcu.getId());

        restDcuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDcu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDcu))
            )
            .andExpect(status().isOk());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
        Dcu testDcu = dcuList.get(dcuList.size() - 1);
        assertThat(testDcu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDcu.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateDcuWithPatch() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();

        // Update the dcu using partial update
        Dcu partialUpdatedDcu = new Dcu();
        partialUpdatedDcu.setId(dcu.getId());

        partialUpdatedDcu.name(UPDATED_NAME).dateCreated(UPDATED_DATE_CREATED);

        restDcuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDcu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDcu))
            )
            .andExpect(status().isOk());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
        Dcu testDcu = dcuList.get(dcuList.size() - 1);
        assertThat(testDcu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDcu.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dcu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dcu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dcu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDcu() throws Exception {
        int databaseSizeBeforeUpdate = dcuRepository.findAll().size();
        dcu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDcuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dcu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dcu in the database
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDcu() throws Exception {
        // Initialize the database
        dcuRepository.saveAndFlush(dcu);

        int databaseSizeBeforeDelete = dcuRepository.findAll().size();

        // Delete the dcu
        restDcuMockMvc.perform(delete(ENTITY_API_URL_ID, dcu.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dcu> dcuList = dcuRepository.findAll();
        assertThat(dcuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
