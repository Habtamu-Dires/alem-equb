package com.ekub.payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Builder
@Validated
public record PaymentRequest(
        @NotNull(message = "Payment type id is mandatory")
        PaymentType type,
        @NotNull(message = "User id is mandatory")
        @NotEmpty(message = "User is mandatory")
        String userId,
        @NotEmpty(message = "Round is mandatory")
        @NotNull(message = "Round id is mandatory")
        String roundId,
        @NotNull(message = "Amount is mandatory")
        BigDecimal amount,
        @NotEmpty(message = "Payment Method is mandatory")
        @NotNull(message = "PaymentMethod is mandatory")
        String paymentMethod,
        @NotEmpty(message = "Remark is mandatory")
        @NotNull(message = "Remark is mandatory")
        String remark,
        String toUserId
) {}
