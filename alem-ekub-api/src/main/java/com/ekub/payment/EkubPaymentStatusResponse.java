package com.ekub.payment;

import lombok.Builder;

import java.util.List;

@Builder
public record EkubPaymentStatusResponse(
        String userId,
        String username,
        boolean hasWon,
        List<Boolean> roundPaymentsStatus
) {
}
