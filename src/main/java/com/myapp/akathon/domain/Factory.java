package com.myapp.akathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Factory.
 */
@Entity
@Table(name = "factory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factory implements Serializable {

    private static final long serialVersionUID = 1L;

    // @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_created")
    private Instant dateCreated;

    @OneToMany(mappedBy = "factoryName")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factoryName" }, allowSetters = true)
    private Set<Dcu> deviceNames = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "factoryNames" }, allowSetters = true)
    private Company companyName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Factory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Factory dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<Dcu> getDeviceNames() {
        return this.deviceNames;
    }

    public void setDeviceNames(Set<Dcu> dcus) {
        if (this.deviceNames != null) {
            this.deviceNames.forEach(i -> i.setFactoryName(null));
        }
        if (dcus != null) {
            dcus.forEach(i -> i.setFactoryName(this));
        }
        this.deviceNames = dcus;
    }

    public Factory deviceNames(Set<Dcu> dcus) {
        this.setDeviceNames(dcus);
        return this;
    }

    public Factory addDeviceName(Dcu dcu) {
        this.deviceNames.add(dcu);
        dcu.setFactoryName(this);
        return this;
    }

    public Factory removeDeviceName(Dcu dcu) {
        this.deviceNames.remove(dcu);
        dcu.setFactoryName(null);
        return this;
    }

    public Company getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(Company company) {
        this.companyName = company;
    }

    public Factory companyName(Company company) {
        this.setCompanyName(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factory)) {
            return false;
        }
        return id != null && id.equals(((Factory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
