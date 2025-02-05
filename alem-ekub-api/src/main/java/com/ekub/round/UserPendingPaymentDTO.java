package com.ekub.round;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserPendingPaymentDTO(
        String equbName,
        Integer version,
        UUID roundId,
        int roundNumber,
        LocalDateTime endDateTime,
        BigDecimal amount,
        double penaltyPercentPerDay
) {
}
