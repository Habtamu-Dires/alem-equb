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
                .type(payment.getType().toString())
                .user(userMapper.toUserResponse(payment.getUser()))
                .toUser(payment.getToUser() != null ?
                        userMapper.toUserResponse(payment.getToUser()) : null)
                .round(roundMapper.toRoundResponse(payment.getRound()))
                .ekubName(payment.getRound().getEkub().getName())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .remark(payment.getRemark())
                .createdDate(payment.getCreatedDate())
                .build();
    }
}
