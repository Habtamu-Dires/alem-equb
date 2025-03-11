package com.ekub.user_guarantee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGuaranteeRepository extends JpaRepository<UserGuarantee, Integer> {


    @Query("SELECT ug FROM UserGuarantee ug WHERE ug.externalId = :externalId")
    Optional<UserGuarantee> findByExternalId(UUID externalId);

    @Query("""       
            SELECT COUNT(*) > 0 FROM UserGuarantee ug
            WHERE ug.round.ekub.externalId = :ekubId
            AND ug.round.version = :version
            AND (ug.guarantor.externalId = :userId
                OR ug.guaranteed.externalId = :userId)
            """)
    boolean isGuarantorOrGuaranteed(String userId, UUID ekubId, int version);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM UserGuarantee ug
            WHERE ug.round.id = :roundId
            AND ug.guarantor.externalId = :guarantorId
            AND ug.guaranteed.externalId = :guaranteedId
            """)
    void deleteUserGuarantee(int roundId, String guarantorId, String guaranteedId);

    @Query("""
            SELECT new com.ekub.user_guarantee.UserGuaranteeResponse(
                guarantor.externalId,
                guarantor.username,
                guaranteed.externalId,
                guaranteed.username
            ) 
            FROM UserGuarantee ug
            JOIN ug.guarantor guarantor
            JOIN ug.guaranteed guaranteed
            WHERE ug.round.externalId = :roundId
            """)
    List<UserGuaranteeResponse> findByRound(UUID roundId);
}
