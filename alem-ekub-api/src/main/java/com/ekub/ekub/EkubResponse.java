package com.ekub.ekub;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EkubResponse(
        String id,
        String name,
        boolean isActive,
        BigDecimal amount,
        BigDecimal totalAmount,
        Integer frequencyInDays,
        String type,
        Integer roundNumber,
        Integer totalMember,
        BigDecimal winAmount,
        String description,
        LocalDateTime nextDrawDateTime,
        LocalDateTime lastDrawDateTime,
        LocalDateTime startDateTime

) {}
