package com.ekub.round;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
//import java.util.OptionalInt;
import java.util.UUID;

public interface RoundRepository extends JpaRepository<Round, Integer> {


    @Query("SELECT r FROM Round r WHERE r.externalId = :externalId")
    Optional<Round> findByExternalId(UUID externalId);

    @Query("""
            SELECT r FROM Round r
            WHERE r.ekub.externalId = :id
            AND r.version = :version
            """)
    List<Round> findByEkub(UUID id,int version);

    @Query("""
            SELECT new com.ekub.round.LastRoundResponse(
                r.externalId,
                r.version,
                r.roundNumber,
                w.externalId,
                w.username,
                r.paid
            )
            FROM Round r
            JOIN r.ekub e
            LEFT JOIN r.winner w
            WHERE e.externalId = :ekubId
                AND r.version = :version
                AND r.roundNumber =:roundNumber
            """)
    Optional<LastRoundResponse> findLastRound(UUID ekubId, int version , int roundNumber);


    @Query("""
            SELECT r FROM Round r
            WHERE r.ekub.id = :ekubId
            AND r.version = :version
            AND r.roundNumber =:roundNumber
            """)
    Optional<Round> findByEkubAndRoundNo(int ekubId, int version , int roundNumber);

    @Query("""
            SELECT new com.ekub.round.UserPendingPaymentDTO(
                e.name,
                r.version,
                r.externalId,
                r.roundNumber,
                r.endDateTime ,
                e.amount,
                e.penaltyPercentPerDay
            )
            FROM Round r
            JOIN r.ekub e
            JOIN e.ekubUsers eu
            LEFT JOIN Payment p ON p.round.id = r.id AND p.user.externalId = :userId
            WHERE eu.user.externalId = :userId AND p.id IS NULL
            """)
    List<UserPendingPaymentDTO> findUserPendingPayments(String userId);


    @Query("""
            SELECT CASE
            WHEN EXISTS (
                SELECT 1
                FROM Round r
                JOIN r.ekub e
                JOIN e.ekubUsers eu
                LEFT JOIN Payment p ON p.round.id = r.id AND p.user.externalId = :userId
                WHERE eu.user.externalId = :userId 
                AND e.id = :ekubId
                AND p.id IS NULL
            )
            THEN true
            ELSE false
            END
            """)
    boolean hasPendingPayments(String userId, int ekubId);

}
