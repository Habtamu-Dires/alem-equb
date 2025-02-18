package com.ekub.payment;


import com.ekub.common.BaseEntity;
import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private PaymentType type;
    private BigDecimal amount;
    private String paymentMethod;
    private String remark;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;
}