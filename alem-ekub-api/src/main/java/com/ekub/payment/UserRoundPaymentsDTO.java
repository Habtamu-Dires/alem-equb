package com.ekub.payment;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserRoundPaymentsDTO(
        String username,
        int roundNumber,
        BigDecimal paymentAmount
) {}
