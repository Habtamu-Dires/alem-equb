package com.ekub.round;

import com.ekub.user.UserResponse;
import com.ekub.user_guarantee.UserGuaranteeResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public record RoundResponse(
        String id,
        String ekubName,
        int version,
        int roundNumber,
        UserResponse winner,
        BigDecimal totalAmount,
        boolean paid,
        List<UserGuaranteeResponse> userGuarantees ,
        LocalDateTime createdDate,
        LocalDateTime endDate
) {}
