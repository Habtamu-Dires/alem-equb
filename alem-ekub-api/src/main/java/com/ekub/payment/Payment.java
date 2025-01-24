package com.ekub.payment;


import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    private UUID id;
    private BigDecimal amount;
    private BigDecimal penaltyAmount;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}