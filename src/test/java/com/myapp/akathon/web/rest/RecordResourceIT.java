package com.myapp.akathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.akathon.IntegrationTest;
import com.myapp.akathon.domain.record.Record;
import com.myapp.akathon.repository.RecordRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link RecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordResourceIT {

    private static final Long DEFAULT_DCU_ID = 1L;
    private static final Long UPDATED_DCU_ID = 2L;

    private static final Float DEFAULT_CURRENT = 1F;
    private static final Float UPDATED_CURRENT = 2F;

    private static final Float DEFAULT_VOLTAGE = 1F;
    private static final Float UPDATED_VOLTAGE = 2F;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecordMockMvc;

    private Record record;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createEntity(EntityManager em) {
        Record record = new Record().dcuId(DEFAULT_DCU_ID).current(DEFAULT_CURRENT).voltage(DEFAULT_VOLTAGE).timestamp(DEFAULT_TIMESTAMP);
        return record;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createUpdatedEntity(EntityManager em) {
        Record record = new Record().dcuId(UPDATED_DCU_ID).current(UPDATED_CURRENT).voltage(UPDATED_VOLTAGE).timestamp(UPDATED_TIMESTAMP);
        return record;
    }

    @BeforeEach
    public void initTest() {
        record = createEntity(em);
    }

    @Test
    @Transactional
    void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();
        // Create the Record
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 1);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getKey().getDcuId()).isEqualTo(DEFAULT_DCU_ID);
        assertThat(testRecord.getCurrent()).isEqualTo(DEFAULT_CURRENT);
        assertThat(testRecord.getVoltage()).isEqualTo(DEFAULT_VOLTAGE);
        assertThat(testRecord.getKey().getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    void createRecordWithExistingId() throws Exception {
        // Create the Record with an existing ID
        record.getKey().setId(UUID.randomUUID());

        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDcuIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.getKey().setDcuId(null);

        // Create the Record, which fails.

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setCurrent(null);

        // Create the Record, which fails.

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVoltageIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setVoltage(null);

        // Create the Record, which fails.

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.getKey().setTimestamp(null);

        // Create the Record, which fails.

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecords() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        // Get all the recordList
        restRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(record.getKey().getId().toString())))
            .andExpect(jsonPath("$.[*].dcuId").value(hasItem(DEFAULT_DCU_ID.intValue())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.doubleValue())))
            .andExpect(jsonPath("$.[*].voltage").value(hasItem(DEFAULT_VOLTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getRecord() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        // Get the record
        restRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, record.getKey().getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(record.getKey().getId().toString()))
            .andExpect(jsonPath("$.dcuId").value(DEFAULT_DCU_ID.intValue()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.doubleValue()))
            .andExpect(jsonPath("$.voltage").value(DEFAULT_VOLTAGE.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRecord() throws Exception {
        // Get the record
        restRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecord() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record
        Record updatedRecord = recordRepository.findByKeyId(record.getKey().getId()).get();
        // Disconnect from session so that the updates on updatedRecord are not directly saved in db
        em.detach(updatedRecord);
        updatedRecord.dcuId(UPDATED_DCU_ID).current(UPDATED_CURRENT).voltage(UPDATED_VOLTAGE).timestamp(UPDATED_TIMESTAMP);

        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecord.getKey().getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getKey().getDcuId()).isEqualTo(UPDATED_DCU_ID);
        assertThat(testRecord.getCurrent()).isEqualTo(UPDATED_CURRENT);
        assertThat(testRecord.getVoltage()).isEqualTo(UPDATED_VOLTAGE);
        assertThat(testRecord.getKey().getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void putNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, record.getKey().getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(record))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(record))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.getKey().setId(record.getKey().getId());

        partialUpdatedRecord.timestamp(UPDATED_TIMESTAMP);

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getKey().getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getKey().getDcuId()).isEqualTo(DEFAULT_DCU_ID);
        assertThat(testRecord.getCurrent()).isEqualTo(DEFAULT_CURRENT);
        assertThat(testRecord.getVoltage()).isEqualTo(DEFAULT_VOLTAGE);
        assertThat(testRecord.getKey().getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void fullUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.getKey().setId(record.getKey().getId());

        partialUpdatedRecord.dcuId(UPDATED_DCU_ID).current(UPDATED_CURRENT).voltage(UPDATED_VOLTAGE).timestamp(UPDATED_TIMESTAMP);

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getKey().getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getKey().getDcuId()).isEqualTo(UPDATED_DCU_ID);
        assertThat(testRecord.getCurrent()).isEqualTo(UPDATED_CURRENT);
        assertThat(testRecord.getVoltage()).isEqualTo(UPDATED_VOLTAGE);
        assertThat(testRecord.getKey().getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void patchNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, record.getKey().getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(record))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(record))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.getKey().setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecord() throws Exception {
        // Initialize the database
        recordRepository.save(record);

        int databaseSizeBeforeDelete = recordRepository.findAll().size();

        // Delete the record
        restRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, record.getKey().getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
