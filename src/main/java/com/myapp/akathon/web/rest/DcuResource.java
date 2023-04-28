package com.myapp.akathon.web.rest;

import com.myapp.akathon.domain.Dcu;
import com.myapp.akathon.repository.DcuRepository;
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
 * REST controller for managing {@link com.myapp.akathon.domain.Dcu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DcuResource {

    private final Logger log = LoggerFactory.getLogger(DcuResource.class);

    private static final String ENTITY_NAME = "dcu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DcuRepository dcuRepository;

    public DcuResource(DcuRepository dcuRepository) {
        this.dcuRepository = dcuRepository;
    }

    /**
     * {@code POST  /dcus} : Create a new dcu.
     *
     * @param dcu the dcu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dcu, or with status {@code 400 (Bad Request)} if the dcu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dcus")
    public ResponseEntity<Dcu> createDcu(@Valid @RequestBody Dcu dcu) throws URISyntaxException {
        log.debug("REST request to save Dcu : {}", dcu);
        if (dcu.getId() != null) {
            throw new BadRequestAlertException("A new dcu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dcu result = dcuRepository.save(dcu);
        return ResponseEntity
            .created(new URI("/api/dcus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dcus/:id} : Updates an existing dcu.
     *
     * @param id the id of the dcu to save.
     * @param dcu the dcu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dcu,
     * or with status {@code 400 (Bad Request)} if the dcu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dcu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dcus/{id}")
    public ResponseEntity<Dcu> updateDcu(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Dcu dcu)
        throws URISyntaxException {
        log.debug("REST request to update Dcu : {}, {}", id, dcu);
        if (dcu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dcu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dcuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dcu result = dcuRepository.save(dcu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dcu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dcus/:id} : Partial updates given fields of an existing dcu, field will ignore if it is null
     *
     * @param id the id of the dcu to save.
     * @param dcu the dcu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dcu,
     * or with status {@code 400 (Bad Request)} if the dcu is not valid,
     * or with status {@code 404 (Not Found)} if the dcu is not found,
     * or with status {@code 500 (Internal Server Error)} if the dcu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dcus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dcu> partialUpdateDcu(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Dcu dcu)
        throws URISyntaxException {
        log.debug("REST request to partial update Dcu partially : {}, {}", id, dcu);
        if (dcu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dcu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dcuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dcu> result = dcuRepository
            .findById(dcu.getId())
            .map(existingDcu -> {
                if (dcu.getName() != null) {
                    existingDcu.setName(dcu.getName());
                }
                if (dcu.getDateCreated() != null) {
                    existingDcu.setDateCreated(dcu.getDateCreated());
                }

                return existingDcu;
            })
            .map(dcuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dcu.getId().toString())
        );
    }

    /**
     * {@code GET  /dcus} : get all the dcus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dcus in body.
     */
    @GetMapping("/dcus")
    public List<Dcu> getAllDcus() {
        log.debug("REST request to get all Dcus");
        return dcuRepository.findAll();
    }

    /**
     * {@code GET  /dcus/:id} : get the "id" dcu.
     *
     * @param id the id of the dcu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dcu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dcus/{id}")
    public ResponseEntity<Dcu> getDcu(@PathVariable Long id) {
        log.debug("REST request to get Dcu : {}", id);
        Optional<Dcu> dcu = dcuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dcu);
    }

    /**
     * {@code DELETE  /dcus/:id} : delete the "id" dcu.
     *
     * @param id the id of the dcu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dcus/{id}")
    public ResponseEntity<Void> deleteDcu(@PathVariable Long id) {
        log.debug("REST request to delete Dcu : {}", id);
        dcuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
