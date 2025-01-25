package com.ekub.ekub;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EkubRequest(
        String id,  // for update
        @NotEmpty(message = "Name is Mandatory")
        @NotNull(message = "Name is Mandatory")
        String name,

        @NotEmpty(message = "Description is Mandatory")
        @NotNull(message = "Description is Mandatory")
        String description,

        BigDecimal totalAmount,

        @NotNull(message = "Draw paymentAmount is Mandatory")
        BigDecimal amount,

        @NotEmpty(message = "Type is Mandatory")
        @NotNull(message = "Type is Mandatory")
        String type,

        @NotNull(message = "Drawn Frequency is Mandatory")
        int frequencyInDays,

        LocalDateTime nextDrawDateTime,

        boolean isActive,

        LocalDateTime startDateTime
) {}
