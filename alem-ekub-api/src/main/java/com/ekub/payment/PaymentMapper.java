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
                .id(payment.getExternalId())
                .type(payment.getType())
                .username(payment.getUser().getUsername())
                .toUsername(payment.getToUser() != null ?
                        payment.getToUser().getUsername() : null)
                .roundNumber(payment.getRound().getRoundNumber())
                .roundVersion(payment.getRound().getVersion())
                .ekubName(payment.getRound().getEkub().getName())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .remark(payment.getRemark())
                .createdDate(payment.getCreatedDate())
                .build();
    }
}
