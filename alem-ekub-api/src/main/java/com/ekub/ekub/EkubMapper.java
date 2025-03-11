package com.ekub.ekub;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EkubMapper {

    public EkubResponse toEkubResponse(Ekub ekub){
        return EkubResponse.builder()
                .id(ekub.getExternalId().toString())
                .isActive(ekub.isActive())
                .isExclusive(ekub.isExclusive())
                .name(ekub.getName())
                .version(ekub.getVersion())
                .amount(ekub.getAmount())
                .penaltyPercentPerDay(ekub.getPenaltyPercentPerDay())
                .totalAmount(ekub.getTotalAmount())
                .frequencyInDays(ekub.getFrequencyInDays())
                .type(ekub.getType())
                .roundNumber(ekub.getRoundNumber())
                .totalMember(ekub.getEkubUsers().size())
                .winAmount(ekub.getAmount().multiply(BigDecimal.valueOf(ekub.getEkubUsers().size())))
                .lastDrawDateTime(ekub.getLastDrawDateTime())
                .nextDrawDateTime(ekub.getNextDrawDateTime())
                .startDateTime(ekub.getStartDateTime())
                .description(ekub.getDescription())
                .isArchived(ekub.isArchived())
                .build();
    }
}
