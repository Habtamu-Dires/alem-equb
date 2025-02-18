package com.ekub.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,String> , JpaSpecificationExecutor<User> {

    @Query(value = """
           SELECT u FROM User u 
           WHERE u IN (
            SELECT iu FROM Ekub e JOIN e.invitedUsers iu 
            WHERE e.id = :ekubId
           )
           AND u NOT IN (
            SELECT eu.user FROM EkubUser eu
            WHERE eu.ekub.id = :ekubId
           )
            """)
    List<User> findUsersInvitedInEkubAndNotJoined(UUID ekubId);

}
