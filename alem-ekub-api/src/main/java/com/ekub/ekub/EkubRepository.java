package com.ekub.ekub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EkubRepository extends JpaRepository<Ekub, Integer>, JpaSpecificationExecutor<Ekub> {

    @Query("SELECT e FROM Ekub e WHERE e.externalId = :externalId")
    Optional<Ekub> findByExternalId(UUID externalId);

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
                AND eu.user.externalId = :userId
            )
            """)
    List<Ekub> findPublicEkubsToJoin(String userId);

    @Query(value = """
            SELECT e FROM Ekub e
            WHERE e IN (
                SELECT ie FROM User u JOIN u.invitedEkubs ie
                WHERE u.externalId = :userId 
            ) AND e NOT IN (
                SELECT eu.ekub  FROM EkubUser eu 
                WHERE eu.user.externalId = :userId
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
            WHERE e.externalId = :ekubId
            """)
    EkubStatusResponse findEkubStatus(UUID ekubId, int version);

    @Query("""
            SELECT e FROM Ekub e
            JOIN FETCH e.ekubUsers eu
            JOIN FETCH eu.user u  
            WHERE u.externalId = :userId
            """)
    List<Ekub> findEkubsByUserId(String userId);

}
