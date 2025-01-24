package com.ekub.payment;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        String userId,
        String roundId,
        BigDecimal amount,
        BigDecimal penaltyAmount
) {}
