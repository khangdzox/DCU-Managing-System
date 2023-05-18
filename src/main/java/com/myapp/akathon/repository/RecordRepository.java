package com.myapp.akathon.repository;

import com.myapp.akathon.domain.record.Record;
import com.myapp.akathon.domain.record.RecordPrimaryKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends CassandraRepository<Record, RecordPrimaryKey> {
    boolean existsByKeyId(UUID id);

    boolean existsByKeyIdAndKeyDcuId(UUID id, Long dcuId);

    Optional<Record> findByKeyId(UUID id);

    List<Record> findByKeyDcuId(Long dcuId);

    Slice<Record> findByKeyDcuId(Long dcuId, Pageable pageRequest);

    Optional<Record> findByKeyIdAndKeyDcuId(UUID id, Long dcuId);

    void deleteByKeyId(UUID id);
}
