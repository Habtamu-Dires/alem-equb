package com.ekub.payment;

import com.ekub.round.RoundMapper;
import com.ekub.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMapper {

    private final UserMapper userMapper;
    private final RoundMapper roundMapper;

    public PaymentResponse toPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId().toString())
                .user(userMapper.toUserResponse(payment.getUser()))
                .round(roundMapper.toRoundResponse(payment.getRound()))
                .amount(payment.getAmount())
                .penaltyAmount(payment.getPenaltyAmount())
                .createdDate(payment.getCreatedDate())
                .build();
    }
}
