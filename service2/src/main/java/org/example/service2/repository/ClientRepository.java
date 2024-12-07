package org.example.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.service2.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    Optional<Client> findById(Long aLong);
}