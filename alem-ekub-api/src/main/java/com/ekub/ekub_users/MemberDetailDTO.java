package com.ekub.ekub_users;

import com.ekub.user_guarantee.UserGuarantee;

import java.util.UUID;

public record MemberDetailDTO(
        String userId,
        String username,
        String firstName,
        String lastName,
        UUID roundId,
        Integer version,
        Integer roundNumber,
        Boolean isPaid,
        UserGuarantee userGuarantee
) {}
