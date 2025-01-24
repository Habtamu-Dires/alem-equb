package com.ekub.ekub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EkubRepository extends JpaRepository<Ekub, UUID>, JpaSpecificationExecutor<Ekub> {

    @Query("SELECT e FROM Ekub e WHERE e.name =:name")
    Optional<Ekub> findByName(String name);

    @Query("SELECT e FROM Ekub e WHERE e.active = True")
    List<Ekub> findActiveEkubs();
}
