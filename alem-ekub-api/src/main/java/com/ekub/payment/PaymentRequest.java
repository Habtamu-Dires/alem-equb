package com.ekub.payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        @NotNull(message = "User id is mandatory")
        @NotEmpty(message = "User is mandatory")
        String userId,
        @NotEmpty(message = "Round is mandatory")
        @NotNull(message = "Round id is mandatory")
        String roundId,
        @NotNull(message = "Amount is mandatory")
        BigDecimal amount
) {}
