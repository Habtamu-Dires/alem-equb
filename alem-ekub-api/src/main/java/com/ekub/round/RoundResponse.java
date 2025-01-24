package com.ekub.round;

import com.ekub.user.UserResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record RoundResponse(
        String id,
        String ekubName,
        Integer roundNumber,
        UserResponse winner,
        BigDecimal totalAmount,
        boolean paid,
        LocalDateTime createdDate,
        LocalDateTime endDate
) {
}
