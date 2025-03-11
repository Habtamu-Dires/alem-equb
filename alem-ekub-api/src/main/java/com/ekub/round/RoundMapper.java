package com.ekub.round;

import com.ekub.user.UserMapper;
import com.ekub.user_guarantee.UserGuaranteeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundMapper {

    private final UserMapper userMapper;
    private final UserGuaranteeMapper userGuaranteeMapper;

    public RoundResponse toRoundResponse(Round round){
        return RoundResponse.builder()
                .id(round.getExternalId().toString())
                .ekubName(round.getEkub().getName())
                .version(round.getVersion())
                .winner(round.getWinner()!=null ? userMapper.toUserResponse(round.getWinner()) : null)
                .roundNumber(round.getRoundNumber())
                .totalAmount(round.getTotalAmount())
                .paid(round.isPaid())
                .userGuarantees(
                        round.getGuarantees()
                        .stream()
                        .map(userGuaranteeMapper::toUserGuaranteeResponse)
                        .toList()
                )
                .createdDate(round.getCreatedDateTime())
                .endDate(round.getEndDateTime())
                .build();
    }

}
