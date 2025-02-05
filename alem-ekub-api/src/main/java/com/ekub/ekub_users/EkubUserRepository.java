package com.ekub.ekub_users;

import com.ekub.ekub.Ekub;
import com.ekub.user.User;
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
            SELECT eu.ekub FROM EkubUser eu 
            WHERE eu.user.id = :userId
            """)
    List<Ekub> findEkubsByUserId(String userId);

    @Query("""
            SELECT eu.user FROM EkubUser eu
            WHERE eu.ekub.id = :ekubId
            """)
    List<User> findEkubUsers(UUID ekubId);


    @Query("""
            SELECT eu.user 
            FROM EkubUser eu
            WHERE eu.ekub.id = :ekubId 
            AND EXISTS (
                SELECT 1 FROM Round r
                WHERE r.ekub.id = :ekubId
                AND r.version = :version
                AND r.winner = eu.user
            )
            """)
    List<User> findEkubWinners(UUID ekubId, int version);

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
    List<User> findDrawParticipants(UUID ekubId, int version);

    @Query(value = """
            SELECT CASE 
            WHEN ( EXISTS ( 
                SELECT 1 FROM EkubUser eu
                WHERE eu.user.id = :userId
                AND eu.ekub.id = :ekubId
            )) THEN true
            ELSE false
            END  
            """)
    boolean isMemberOfEkub(String userId, UUID ekubId);

    @Query("""
            SELECT CASE
                WHEN (EXISTS (
                    SELECT 1 FROM EkubUser eu
                    WHERE eu.user.id = :userId
                    AND eu.ekub.id = :ekubId
                ) AND NOT EXISTS (
                    SELECT 1 FROM Round r
                    WHERE r.ekub.id = :ekubId
                    AND r.version = :version
                    AND r.winner.id = :userId
                )) THEN true
                ELSE false
                END
            """)
    boolean hasNotWonYet(String userId, UUID ekubId, int version);

    @Query("""
            SELECT new com.ekub.ekub_users.MemberDetailDTO(
                u.id,
                u.username,
                u.firstName,
                u.lastName,
                r.id,
                r.version,
                r.roundNumber,
                r.paid,
                ug
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
            WHERE eu.ekub.id = :ekubId
            ORDER BY u.firstName
            """)
    List<MemberDetailDTO> findMemberDetailDTO(UUID ekubId, int version);


}
