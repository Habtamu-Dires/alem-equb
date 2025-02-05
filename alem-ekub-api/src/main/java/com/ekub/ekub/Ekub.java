package com.ekub.ekub;

import com.ekub.common.BaseEntity;
import com.ekub.ekub_users.EkubUser;
import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Integer version;
    private BigDecimal amount;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private double penaltyPercentPerDay;
    private Integer frequencyInDays;
    private String type;
    private LocalDateTime nextDrawDateTime;
    private Integer roundNumber;
    private LocalDateTime startDateTime;
    private boolean active = false;
    private boolean exclusive = true;
    private boolean archived = false;
    @Column(insertable = false)
    private LocalDateTime lastDrawDateTime;
    private Integer mpesaAccountNumber;
    private Integer telebirrAccountNumber;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "ekub", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<Round> rounds;

    @OneToMany(mappedBy = "ekub", cascade = {CascadeType.REMOVE,CascadeType.DETACH})
    private List<EkubUser> ekubUsers;


    @ManyToMany(mappedBy = "invitedEkubs")
    private List<User> invitedUsers = new ArrayList<>();

    // helper method
    public void addInvitedUser(User user){
        this.invitedUsers.add(user);
        user.getInvitedEkubs().add(this);
    }
}
