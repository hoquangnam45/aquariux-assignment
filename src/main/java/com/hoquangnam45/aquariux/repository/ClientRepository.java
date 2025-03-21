package com.hoquangnam45.aquariux.repository;

import com.hoquangnam45.aquariux.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByUsername(String user);
}
