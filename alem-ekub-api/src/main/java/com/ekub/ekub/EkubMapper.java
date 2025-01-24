package com.ekub.ekub;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EkubMapper {

    public EkubResponse toEkubResponse(Ekub ekub){
        return EkubResponse.builder()
                .id(ekub.getId().toString())
                .isActive(ekub.isActive())
                .name(ekub.getName())
                .amount(ekub.getAmount())
                .totalAmount(ekub.getTotalAmount())
                .frequencyInDays(ekub.getFrequencyInDays())
                .type(ekub.getType())
                .roundNumber(ekub.getRoundNumber())
                .totalMember(ekub.getUsers().size())
                .winAmount(ekub.getAmount().multiply(BigDecimal.valueOf(ekub.getUsers().size())))
                .lastDrawDateTime(ekub.getLastDrawDateTime())
                .nextDrawDateTime(ekub.getNextDrawDateTime())
                .startDateTime(ekub.getStartDateTime())
                .description(ekub.getDescription())
                .build();
    }
}
