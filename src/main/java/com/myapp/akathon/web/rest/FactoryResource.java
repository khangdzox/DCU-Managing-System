package com.myapp.akathon.web.rest;

import com.myapp.akathon.domain.Factory;
import com.myapp.akathon.repository.FactoryRepository;
import com.myapp.akathon.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.akathon.domain.Factory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FactoryResource {

    private final Logger log = LoggerFactory.getLogger(FactoryResource.class);

    private static final String ENTITY_NAME = "factory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactoryRepository factoryRepository;

    public FactoryResource(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    /**
     * {@code POST  /factories} : Create a new factory.
     *
     * @param factory the factory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factory, or with status {@code 400 (Bad Request)} if the factory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factories")
    public ResponseEntity<Factory> createFactory(@Valid @RequestBody Factory factory) throws URISyntaxException {
        log.debug("REST request to save Factory : {}", factory);
        if (factory.getId() != null) {
            throw new BadRequestAlertException("A new factory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Factory result = factoryRepository.save(factory);
        return ResponseEntity
            .created(new URI("/api/factories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factories/:id} : Updates an existing factory.
     *
     * @param id the id of the factory to save.
     * @param factory the factory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factory,
     * or with status {@code 400 (Bad Request)} if the factory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factories/{id}")
    public ResponseEntity<Factory> updateFactory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Factory factory
    ) throws URISyntaxException {
        log.debug("REST request to update Factory : {}, {}", id, factory);
        if (factory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Factory result = factoryRepository.save(factory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factories/:id} : Partial updates given fields of an existing factory, field will ignore if it is null
     *
     * @param id the id of the factory to save.
     * @param factory the factory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factory,
     * or with status {@code 400 (Bad Request)} if the factory is not valid,
     * or with status {@code 404 (Not Found)} if the factory is not found,
     * or with status {@code 500 (Internal Server Error)} if the factory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Factory> partialUpdateFactory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Factory factory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factory partially : {}, {}", id, factory);
        if (factory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Factory> result = factoryRepository
            .findById(factory.getId())
            .map(existingFactory -> {
                if (factory.getName() != null) {
                    existingFactory.setName(factory.getName());
                }
                if (factory.getDateCreated() != null) {
                    existingFactory.setDateCreated(factory.getDateCreated());
                }

                return existingFactory;
            })
            .map(factoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factory.getId().toString())
        );
    }

    /**
     * {@code GET  /factories} : get all the factories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factories in body.
     */
    @GetMapping("/factories")
    public List<Factory> getAllFactories() {
        log.debug("REST request to get all Factories");
        return factoryRepository.findAll();
    }

    /**
     * {@code GET  /factories/:id} : get the "id" factory.
     *
     * @param id the id of the factory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factories/{id}")
    public ResponseEntity<Factory> getFactory(@PathVariable Long id) {
        log.debug("REST request to get Factory : {}", id);
        Optional<Factory> factory = factoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factory);
    }

    /**
     * {@code DELETE  /factories/:id} : delete the "id" factory.
     *
     * @param id the id of the factory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factories/{id}")
    public ResponseEntity<Void> deleteFactory(@PathVariable Long id) {
        log.debug("REST request to delete Factory : {}", id);
        factoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
