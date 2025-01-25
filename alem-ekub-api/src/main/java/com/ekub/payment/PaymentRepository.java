package com.ekub.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            ORDER BY u.username, r.roundNumber
            """)
    Page<UserRoundPaymentsDTO> findUserRoundPayments(UUID ekubId, Pageable pageable);



}
