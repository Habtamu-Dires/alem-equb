package com.ekub.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {


    @Query("""
            SELECT new com.ekub.payment.UserRoundPaymentsDTO(
                u.username,
                r.roundNumber,
                p.amount
            )
            FROM Payment p
            JOIN p.user u
            JOIN p.round r
            JOIN r.ekub e
            WHERE e.externalId = :ekubId
            AND r.version = :version
            AND p.type = 'MEMBER_PAYMENT'
            ORDER BY u.username, r.roundNumber
            """)
    List<UserRoundPaymentsDTO> findUserRoundPayments(UUID ekubId, int version);

    @Query("""
            SELECT new com.ekub.payment.PaymentResponse(
                p.externalId,
                p.type,
                u.username,
                tu.username,
                e.name,
                r.roundNumber,
                r.version,
                p.amount,
                p.paymentMethod,
                p.remark,
                p.createdDate
            )
            FROM Payment p
            JOIN p.user u
            LEFT JOIN p.toUser tu
            JOIN p.round r
            JOIN r.ekub e
            WHERE (u.externalId = :externalId
            OR tu.id = :userId)
            """)
    List<PaymentResponse> findUserPayments(String externalId, int userId);
}
