package com.myapp.akathon.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Record.
 */
@Entity
@Table(name = "record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "dcuid", nullable = false)
    private Long dcuid;

    @NotNull
    @Column(name = "current", nullable = false)
    private Float current;

    @NotNull
    @Column(name = "voltage", nullable = false)
    private Float voltage;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Record id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDcuid() {
        return this.dcuid;
    }

    public Record dcuid(Long dcuid) {
        this.setDcuid(dcuid);
        return this;
    }

    public void setDcuid(Long dcuid) {
        this.dcuid = dcuid;
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

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public Record timestamp(ZonedDateTime timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
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
        return id != null && id.equals(((Record) o).id);
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
            "id=" + getId() +
            ", dcuid=" + getDcuid() +
            ", current=" + getCurrent() +
            ", voltage=" + getVoltage() +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
