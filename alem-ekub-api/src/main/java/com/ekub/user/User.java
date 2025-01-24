package com.ekub.user;

import com.ekub.common.BaseEntity;
import com.ekub.ekub_users.EkubUser;
import com.ekub.payment.Payment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "_user")
public class User extends BaseEntity {

    @Id
    private String id;
    @Column(unique = true)
    private String username;
    private Integer nationalId;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true,nullable = false)
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String profession;
    private String profilePicUrl;
    private String idCardImageUrl;
    private boolean enabled;
    private String remark;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guarantor_id")
    private User guarantor;

    @OneToOne(mappedBy = "guarantor",fetch = FetchType.EAGER)
    private User guaranteedUser;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<EkubUser> ekubs;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    public String fullName(){
        return this.firstName + " " + this.lastName;
    }

}
