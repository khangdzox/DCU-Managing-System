package com.myapp.akathon.domain.record;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@PrimaryKeyClass
public class RecordPrimaryKey implements Serializable {

    @PrimaryKeyColumn(name = "dcu_id", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long dcuId;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 0, ordering = Ordering.DESCENDING, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Instant timestamp;

    @Indexed
    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;

    public RecordPrimaryKey() {}

    public RecordPrimaryKey(Long dcuId, Instant timestamp) {
        this.id = UUID.randomUUID();
        this.dcuId = dcuId;
        this.timestamp = timestamp;
    }

    public RecordPrimaryKey(UUID id, Long dcuId, Instant timestamp) {
        this.id = id;
        this.dcuId = dcuId;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getDcuId() {
        return dcuId;
    }

    public void setDcuId(Long dcuId) {
        this.dcuId = dcuId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordPrimaryKey)) {
            return false;
        }
        return id != null && id.equals(((RecordPrimaryKey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RecordPrimaryKey{" + "dcuId=" + getDcuId() + ", id=" + getId() + ", timestamp='" + getTimestamp() + "'" + "}";
    }
}
