package com.ekub.payment;

import com.ekub.round.RoundResponse;
import com.ekub.user.UserResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        String id,
        String type,
        UserResponse user,
        String ekubName,
        RoundResponse round,
        BigDecimal amount,
        String paymentMethod,
        String remark,
        LocalDateTime createdDate
) {
}
