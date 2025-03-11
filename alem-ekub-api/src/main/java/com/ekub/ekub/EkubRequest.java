package com.ekub.ekub;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

        @NotNull(message = "Draw paymentAmount is Mandatory")
        BigDecimal amount,

        @NotEmpty(message = "Type is Mandatory")
        @NotNull(message = "Type is Mandatory")
        String type,

        @NotNull(message = "Drawn Frequency is Mandatory")
        int frequencyInDays,

        BigDecimal totalAmount,

        @NotNull(message = "Drawn Frequency is Mandatory")
        Double penaltyPercentPerDay,
        LocalDateTime nextDrawDateTime,
        LocalDateTime startDateTime,
        boolean isActive,
        boolean isExclusive,
        boolean isArchived
) {}
