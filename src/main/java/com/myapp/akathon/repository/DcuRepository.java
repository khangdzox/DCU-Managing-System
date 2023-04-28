package com.myapp.akathon.repository;

import com.myapp.akathon.domain.Dcu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dcu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DcuRepository extends JpaRepository<Dcu, Long> {}
