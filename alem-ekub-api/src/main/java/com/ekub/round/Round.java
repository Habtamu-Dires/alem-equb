package com.ekub.round;

import com.ekub.ekub.Ekub;
import com.ekub.payment.Payment;
import com.ekub.user.User;
import com.ekub.user_guarantee.UserGuarantee;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true,updatable = false, nullable = false)
    @Builder.Default
    private UUID externalId = UUID.randomUUID();

    private Integer roundNumber;
    private Integer version;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "ekub_id")
    private Ekub ekub;

    @OneToMany(mappedBy = "round", cascade = {CascadeType.REMOVE,CascadeType.DETACH})
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name="winner_id")
    private User winner;

    private LocalDateTime createdDateTime;
    private LocalDateTime endDateTime;
    private boolean paid = false;

    @OneToMany(mappedBy = "round", cascade = {CascadeType.REMOVE,CascadeType.DETACH})
    public List<UserGuarantee> guarantees;

}
