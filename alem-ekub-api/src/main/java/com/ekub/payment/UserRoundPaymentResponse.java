package com.ekub.payment;

import lombok.Builder;

import java.util.Map;

@Builder
public record UserRoundPaymentResponse(
        Map<String, Object> row
) {
}
