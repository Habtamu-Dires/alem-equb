package com.ekub.round;

import com.ekub.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundMapper {

    private UserMapper userMapper;

    public RoundResponse toRoundResponse(Round round){
        return RoundResponse.builder()
                .id(round.getId().toString())
                .ekubName(round.getEkub().getName())
                .winner(round.getWinner()!=null ? userMapper.toUserResponse(round.getWinner()) : null)
                .roundNumber(round.getRoundNumber())
                .totalAmount(round.getTotalAmount())
                .paid(round.isPaid())
                .createdDate(round.getCreatedDate())
                .endDate(round.getEndDate())
                .build();
    }
}
