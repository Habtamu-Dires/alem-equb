package com.ekub.payment;

import com.ekub.round.RoundResponse;
import com.ekub.user.User;
import com.ekub.user.UserResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentResponse(
        UUID id,
        PaymentType type,
        String username,
        String toUsername,
        String ekubName,
        Integer roundNumber,
        Integer roundVersion,
        BigDecimal amount,
        String paymentMethod,
        String remark,
        LocalDateTime createdDate
) {
}
