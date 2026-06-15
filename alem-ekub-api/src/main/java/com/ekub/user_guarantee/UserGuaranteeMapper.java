package com.ekub.user_guarantee;

import org.springframework.stereotype.Service;

@Service
public class UserGuaranteeMapper {

    public UserGuaranteeResponse toUserGuaranteeResponse(UserGuarantee userGuarantee){
        return UserGuaranteeResponse.builder()
                .guarantorId(userGuarantee.getGuarantor().getExternalId())
                .guarantor(userGuarantee.getGuarantor().getUsername())
                .guaranteedId(userGuarantee.getGuaranteed().getExternalId())
                .guaranteed(userGuarantee.getGuaranteed().getUsername())
                .build();
    }
}
