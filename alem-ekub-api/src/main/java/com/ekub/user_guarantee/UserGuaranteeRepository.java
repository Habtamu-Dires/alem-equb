package com.ekub.user_guarantee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserGuaranteeRepository extends JpaRepository<UserGuarantee, UUID> {

    @Query("""       
            SELECT COUNT(*) > 0 FROM UserGuarantee ug
            WHERE ug.round.ekub.id = :ekubId
            AND ug.round.version = :version
            AND (ug.guarantor.id = :userId 
                OR ug.guaranteed.id = :userId ) 
            """)
    boolean isGuarantorOrGuaranteed(String userId, UUID ekubId, int version);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM UserGuarantee ug
            WHERE ug.round.id = :roundId
            AND ug.guarantor.id = :guarantorId
            AND ug.guaranteed.id = :guaranteedId
            """)
    void deleteUserGuarantee(UUID roundId, String guarantorId, String guaranteedId);

    @Query("""
            SELECT new com.ekub.user_guarantee.UserGuaranteeResponse(
                ug.guarantor.id,
                ug.guarantor.username,
                ug.guaranteed.id,
                ug.guaranteed.username
            ) 
            FROM UserGuarantee ug
            WHERE ug.round.id = :roundId
            """)
    List<UserGuaranteeResponse> findByRound(UUID roundId);
}
