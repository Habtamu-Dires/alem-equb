package com.ekub.ekub_users;

import com.ekub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EkubUserRepository extends JpaRepository<EkubUser, Integer> {


    @Query("""
            SELECT eu FROM EkubUser eu WHERE eu.ekub.externalId = :ekubId
            AND eu.user.externalId = :userId
            """)
    Optional<EkubUser> findByEkubIdAndUserId(UUID ekubId, String userId);

    @Query("""
            SELECT eu.user
            FROM EkubUser eu
            WHERE eu.ekub.id = :ekubId
            AND NOT EXISTS (
                SELECT 1 FROM Round r
                WHERE r.ekub.id = :ekubId
                AND r.version = :version
                AND r.winner = eu.user
            )
            """)
    List<User> findDrawParticipants(int ekubId, int version);

    @Query(value = """
            SELECT CASE 
            WHEN ( EXISTS ( 
                SELECT 1 FROM EkubUser eu
                WHERE eu.user.externalId = :userId
                AND eu.ekub.externalId = :ekubId
            )) THEN true
            ELSE false
            END  
            """)
    boolean isMemberOfEkub(String userId, UUID ekubId);

    @Query("""
            SELECT CASE
                WHEN (EXISTS (
                    SELECT 1 FROM EkubUser eu
                    WHERE eu.user.externalId = :userId
                    AND eu.ekub.externalId = :ekubId
                ) AND NOT EXISTS (
                    SELECT 1 FROM Round r
                    WHERE r.ekub.externalId = :ekubId
                    AND r.version = :version
                    AND r.winner.externalId = :userId
                )) THEN true
                ELSE false
                END
            """)
    boolean hasNotWonYet(String userId, UUID ekubId, int version);

    @Query("""
            SELECT new com.ekub.ekub_users.MemberDetailResponse(
                u.externalId,
                u.username,
                CONCAT(u.firstName,' ',u.lastName),
                r.externalId,
                r.version,
                r.roundNumber,
                r.paid,
                g.username
            )
            FROM EkubUser eu
            JOIN eu.user u
            LEFT JOIN Round r 
                ON r.winner = u
                AND r.ekub = eu.ekub
                AND r.version = :version
            LEFT JOIN UserGuarantee ug 
                ON ug.round = r
                AND ug.guaranteed = u  
            LEFT JOIN ug.guarantor g
            WHERE eu.ekub.externalId = :ekubId
            ORDER BY u.firstName
            """)
    List<MemberDetailResponse> findMemberDetail(UUID ekubId, int version);

}
