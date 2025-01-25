package com.ekub.ekub;

import com.ekub.common.BaseEntity;
import com.ekub.ekub_users.EkubUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@Entity
@Table(name = "ekub")
public class Ekub extends BaseEntity {

    @Id
    private UUID id;
    @Column(unique = true)
    private String name;
    private BigDecimal amount;
    private BigDecimal totalAmount;
    private Integer frequencyInDays;
    private String type;
    private LocalDateTime nextDrawDateTime;
    private Integer roundNumber;
    private LocalDateTime startDateTime;
    private boolean active = false;
    @Column(insertable = false)
    private LocalDateTime lastDrawDateTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "ekub", cascade = {CascadeType.REMOVE})
    private List<EkubUser> users;
}
