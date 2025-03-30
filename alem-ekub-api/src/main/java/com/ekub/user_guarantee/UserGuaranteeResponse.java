package com.ekub.user_guarantee;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserGuaranteeResponse(
        String guarantorId,
        String guarantor,
        String guaranteedId,
        String guaranteed
) {
}
