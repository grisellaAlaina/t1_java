package org.example.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.service2.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
