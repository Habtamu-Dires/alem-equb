package com.ekub.user;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResponse(
        String id,
        String username,
        String firstname,
        String fullName,
        String lastname,
        String email,
        String phoneNumber,
        String profession,
        String profilePicUrl,
        String idCardImageUrl,
        List<String> guarantors,
        boolean enabled,
        List<String> ekubs,
        List<String> ekubIds,
        String remark
) {}
