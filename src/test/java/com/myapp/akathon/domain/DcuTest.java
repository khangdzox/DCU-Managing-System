package com.myapp.akathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.akathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DcuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dcu.class);
        Dcu dcu1 = new Dcu();
        dcu1.setId(1L);
        Dcu dcu2 = new Dcu();
        dcu2.setId(dcu1.getId());
        assertThat(dcu1).isEqualTo(dcu2);
        dcu2.setId(2L);
        assertThat(dcu1).isNotEqualTo(dcu2);
        dcu1.setId(null);
        assertThat(dcu1).isNotEqualTo(dcu2);
    }
}
