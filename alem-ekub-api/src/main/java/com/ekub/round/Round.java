package com.ekub.round;

import com.ekub.ekub.Ekub;
import com.ekub.payment.Payment;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "round")
public class Round {

    @Id
    private UUID id;
    private Integer roundNumber;
    private BigDecimal totalAmount;
    @ManyToOne
    @JoinColumn(name = "ekub_id")
    private Ekub ekub;

    @OneToMany(mappedBy = "round")
    private List<Payment> payments;

    @OneToOne
    private User winner;

    private LocalDateTime createdDate;
    private LocalDateTime endDate;
    private boolean paid;

}
