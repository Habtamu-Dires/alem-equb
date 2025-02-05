package com.ekub.round;

import com.ekub.user.UserMapper;
import com.ekub.user_guarantee.UserGuaranteeRepository;
import com.ekub.user_guarantee.UserGuaranteeResponse;
import com.ekub.user_guarantee.UserGuaranteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoundMapper {

    private final UserMapper userMapper;
    private  final UserGuaranteeRepository userGuaranteeRepository;

    public RoundResponse toRoundResponse(Round round){
        return RoundResponse.builder()
                .id(round.getId().toString())
                .ekubName(round.getEkub().getName())
                .version(round.getVersion())
                .winner(round.getWinner()!=null ? userMapper.toUserResponse(round.getWinner()) : null)
                .roundNumber(round.getRoundNumber())
                .totalAmount(round.getTotalAmount())
                .paid(round.isPaid())
                .userGuarantees(round.getWinner() != null ? getUserGuarantee(round.getId()) : List.of())
                .createdDate(round.getCreatedDateTime())
                .endDate(round.getEndDateTime())
                .build();
    }

    private List<UserGuaranteeResponse> getUserGuarantee(UUID roundId){
        return userGuaranteeRepository.findByRound(roundId);
    }
}
