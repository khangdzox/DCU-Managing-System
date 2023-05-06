package com.myapp.akathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.akathon.domain.record.Record;
import com.myapp.akathon.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Record.class);
        Record record1 = new Record();
        record1.getKey().setId(UUID.randomUUID());
        Record record2 = new Record();
        record2.getKey().setId(record1.getKey().getId());
        assertThat(record1).isEqualTo(record2);
        record2.getKey().setId(UUID.randomUUID());
        assertThat(record1).isNotEqualTo(record2);
        record1.getKey().setId(null);
        assertThat(record1).isNotEqualTo(record2);
    }
}
