package org.example.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.service2.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Override
    public Account save (Account account);
}
