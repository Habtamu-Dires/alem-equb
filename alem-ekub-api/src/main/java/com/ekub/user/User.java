package com.ekub.user;

import com.ekub.ekub.Ekub;
import com.ekub.ekub_users.EkubUser;
import com.ekub.payment.Payment;
import com.ekub.round.Round;
import com.ekub.user_guarantee.UserGuarantee;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,updatable = false,unique = true)
    private String externalId;

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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<EkubUser> ekubUsers;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    @OneToMany(mappedBy = "winner")
    private List<Round> winRounds;

    @OneToMany(mappedBy = "guarantor")
    private List<UserGuarantee> guarantor;

    @OneToMany(mappedBy = "guaranteed")
    private List<UserGuarantee> guaranteed;


    @ManyToMany
    @JoinTable(name = "invitation",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "ekub_id")}
    )
    private  List<Ekub> invitedEkubs = new ArrayList<>();

    public String fullName(){
        return this.firstName + " " + this.lastName;
    }


    // helper method : add invited ekubs
    public void addInvitedEkub(Ekub ekub){
        this.invitedEkubs.add(ekub);
        ekub.getInvitedUsers().add(this);
    }

    // helper method : remove invited ekubs
    public void removeInvitedEkub(Ekub ekub){
        this.invitedEkubs.remove(ekub);
        ekub.getInvitedUsers().remove(this);
    }

}
