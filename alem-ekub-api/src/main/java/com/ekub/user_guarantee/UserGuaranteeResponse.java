package com.ekub.user_guarantee;

import lombok.Builder;

@Builder
public record UserGuaranteeResponse(
        String guarantorId,
        String guarantor,
        String guaranteedId,
        String guaranteed
) {
}
