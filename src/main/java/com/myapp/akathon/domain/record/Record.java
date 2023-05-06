package com.myapp.akathon.domain.record;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.cassandra.core.mapping.*;

/**
 * A Record.
 */
@Table(value = "record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private RecordPrimaryKey key;

    @NotNull
    @Column("current")
    @CassandraType(type = CassandraType.Name.DECIMAL)
    private Float current;

    @NotNull
    @Column("voltage")
    @CassandraType(type = CassandraType.Name.DECIMAL)
    private Float voltage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RecordPrimaryKey getKey() {
        return key;
    }

    public Record key(RecordPrimaryKey key) {
        this.setKey(key);
        return this;
    }

    public void setKey(RecordPrimaryKey key) {
        this.key = key;
    }

    public Record id(UUID id) {
        this.getKey().setId(id);
        return this;
    }

    public Record dcuId(Long dcuId) {
        this.getKey().setDcuId(dcuId);
        return this;
    }

    public Record timestamp(Instant timestamp) {
        this.getKey().setTimestamp(timestamp);
        return this;
    }

    public Float getCurrent() {
        return this.current;
    }

    public Record current(Float current) {
        this.setCurrent(current);
        return this;
    }

    public void setCurrent(Float current) {
        this.current = current;
    }

    public Float getVoltage() {
        return this.voltage;
    }

    public Record voltage(Float voltage) {
        this.setVoltage(voltage);
        return this;
    }

    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        return key != null && key.equals(((Record) o).key);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Record{" +
            "primaryKey=" + getKey() +
            ", current=" + getCurrent() +
            ", voltage=" + getVoltage() +
            "}";
    }
}
