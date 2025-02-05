package com.ekub.ekub_users;


import lombok.Builder;

import java.util.UUID;

@Builder
public record MemberDetailResponse(
        String userId,
        String username,
        String fullName,
        UUID winRoundId,
        Integer version,
        Integer roundNumber,
        Boolean isPaid,
        String guarantor
) {}
