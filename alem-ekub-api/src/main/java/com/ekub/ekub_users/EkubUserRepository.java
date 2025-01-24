package com.ekub.ekub_users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EkubUserRepository extends JpaRepository<EkubUser, UUID> {

    @Query("SELECT eu FROM EkubUser eu WHERE eu.ekub.id =:ekubId")
    List<EkubUser> findByEkubId(UUID ekubId);

    @Query("""
            SELECT eu FROM EkubUser eu WHERE eu.ekub.id = :ekubId
            AND eu.user.id = :userId
            """)
    Optional<EkubUser> findByEkubIdAndUserId(UUID ekubId, String userId);

    @Query("""
            SELECT eu FROM EkubUser eu WHERE eu.user.id = :userId
            """)
    List<EkubUser> findByUserId(String userId);
}
