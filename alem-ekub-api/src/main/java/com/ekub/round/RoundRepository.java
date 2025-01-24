package com.ekub.round;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoundRepository extends JpaRepository<Round, UUID> {

    @Query("SELECT r FROM Round r WHERE r.ekub.id = :id")
    List<Round> findByEkubId(UUID id);

    @Query("""
            SELECT r FROM Round r WHERE r.ekub.id = :ekubId
            AND r.roundNumber =:roundNumber
            """)
    Optional<Round> findByEkubIdAndRoundNo(UUID ekubId, Integer roundNumber);
}
