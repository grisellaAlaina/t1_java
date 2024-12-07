package org.example.service2.service;

import org.example.service2.aop.Metric;
import org.example.service2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.service2.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account getById(long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);
    }

    @Metric(1000)
    public void doMetric() throws InterruptedException {
        Thread.sleep(3000);
    }

    public Account updateBalance(Long accountId, BigDecimal changeAmount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        BigDecimal newBalance;

        if (changeAmount.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = account.getBalance().subtract(changeAmount.abs());
        } else {
            newBalance = account.getBalance().add(changeAmount);
        }

        account.setBalance(newBalance);

        return accountRepository.save(account);
    }
}