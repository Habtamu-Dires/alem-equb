package com.ekub.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.round.id = :roundId")
    List<Payment> findByRoundId(UUID roundId);

    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId")
    List<Payment> findByUserId(String userId);

    @Query("""
            SELECT new com.ekub.payment.UserRoundPaymentsDTO(
                u.username,
                r.roundNumber,
                p.amount
            )
            FROM Payment p
            JOIN p.user u
            JOIN p.round r
            WHERE r.ekub.id = :ekubId
            AND r.version = :version
            AND p.type = 'MEMBER_PAYMENT'
            ORDER BY u.username, r.roundNumber
            """)
    List<UserRoundPaymentsDTO> findUserRoundPayments(UUID ekubId, int version);

}
