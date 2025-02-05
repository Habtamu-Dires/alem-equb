package com.ekub.ekub;

import com.ekub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EkubRepository extends JpaRepository<Ekub, UUID>, JpaSpecificationExecutor<Ekub> {

    @Query("SELECT e FROM Ekub e WHERE e.name =:name")
    Optional<Ekub> findByName(String name);

    @Query("SELECT e FROM Ekub e WHERE e.active = True")
    List<Ekub> findActiveEkubs();

    @Query("""
            SELECT e FROM Ekub e
            WHERE e.exclusive = false
            AND e.active = false
            AND NOT EXISTS (
                SELECT 1 FROM EkubUser eu
                WHERE eu.ekub = e
                AND eu.user.id = :userId
            )
            """)
    List<Ekub> findPublicEkub(String userId);

    @Modifying
    @Query(value = "DELETE FROM invitation WHERE ekub_id = :ekubId",nativeQuery = true)
    void deleteInvitationsForEkub(@Param("ekubId") UUID ekubId);

    @Query("SELECT e FROM Ekub e LEFT JOIN FETCH e.invitedUsers WHERE e.id =:ekubId")
    Optional<Ekub> findEkubWithInvitedUsers(@Param("ekubId") UUID ekubId);

    @Query(value = """
            SELECT e FROM Ekub e
            WHERE e IN (
                SELECT ie FROM User u JOIN u.invitedEkubs ie
                WHERE u.id = :userId 
            ) AND e NOT IN (
                SELECT eu.ekub  FROM EkubUser eu 
                WHERE eu.user.id = :userId
            )
            """)
    List<Ekub> findInvitedEkubsYetToJoin(String userId);

    @Query("""
            SELECT new com.ekub.ekub.EkubStatusResponse(
                (SELECT COUNT(*) FROM EkubUser eu WHERE eu.ekub = e),
                (SELECT COUNT(r.winner) FROM Round r WHERE r.ekub = e AND r.version = :version),
                e.startDateTime
            )
            FROM Ekub e
            WHERE e.id = :ekubId
            """)
    EkubStatusResponse findEkubStatus(UUID ekubId, int version);
}
