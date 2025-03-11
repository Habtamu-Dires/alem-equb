package com.ekub.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.externalId = :externalId")
    Optional<User> findByExternalId(String externalId);

    @Query(value = """
           SELECT u FROM User u
           WHERE u IN (
            SELECT iu FROM Ekub e 
            JOIN e.invitedUsers iu 
            WHERE e.externalId = :ekubId
           )
           AND u NOT IN (
            SELECT eu.user FROM EkubUser eu
            WHERE eu.ekub.externalId = :ekubId
           )
            """)
    List<User> findUsersInvitedInEkubAndNotJoined(UUID ekubId);

}
