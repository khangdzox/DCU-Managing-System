package com.myapp.akathon.repository;

import com.myapp.akathon.domain.Factory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Factory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {}
