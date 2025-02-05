package com.ekub.user_guarantee;

import com.ekub.common.BaseEntity;
import com.ekub.ekub.Ekub;
import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private UUID id;

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
