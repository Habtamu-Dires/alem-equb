package com.ekub.ekub_users;

import com.ekub.common.BaseEntity;
import com.ekub.ekub.Ekub;
import com.ekub.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@Entity
@Table(name = "ekub_user")
public class EkubUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID externalId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ekub_id")
    private Ekub ekub;

}
