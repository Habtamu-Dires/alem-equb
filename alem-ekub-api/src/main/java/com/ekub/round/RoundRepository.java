package com.ekub.round;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT new  com.ekub.round.UserPendingPaymentResponse(
                e.name,
                r.id,
                r.roundNumber,
                r.endDateTime ,
                e.amount
            )
            FROM Round r
            JOIN r.ekub e
            LEFT JOIN Payment p ON p.round.id = r.id AND p.user.id = :userId
            WHERE p.id IS NULL
            """)
    List<UserPendingPaymentResponse> findUserPendingPayments(String userId);
}
