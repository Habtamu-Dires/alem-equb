package com.ekub.user_guarantee;

import org.springframework.stereotype.Service;

@Service
public class UserGuaranteeMapper {

    public UserGuaranteeResponse toUserGuaranteeResponse(UserGuarantee userGuarantee){
        return UserGuaranteeResponse.builder()
                .guarantor(userGuarantee.getGuarantor().getUsername())
                .guaranteed(userGuarantee.getGuaranteed().getUsername())
                .build();
    }
}
