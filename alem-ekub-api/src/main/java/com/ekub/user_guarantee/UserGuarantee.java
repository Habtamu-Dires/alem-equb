package com.ekub.user_guarantee;

import com.ekub.common.BaseEntity;
import com.ekub.ekub.Ekub;
import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "user_guarantee")
public class UserGuarantee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID externalId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "guarantor_id")
    private User guarantor;

    @ManyToOne
    @JoinColumn(name = "guaranteed_id")
    private User guaranteed;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

}
