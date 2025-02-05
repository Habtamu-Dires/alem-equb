package com.ekub.ekub;

import java.time.LocalDateTime;

public record EkubStatusResponse(
        long totalMembers,
        long winnersCount,
        LocalDateTime startDate
) {
}
