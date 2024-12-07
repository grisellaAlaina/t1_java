package org.example.service2.service;

import org.example.service2.aop.LogDataSourceError;
import org.example.service2.exception.TransactionException;
import org.example.service2.repository.AccountRepository;
import org.example.service2.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.service2.dto.TransactionDto;
import org.example.service2.kafka.TransactionProducer;
import org.example.service2.model.Account;
import org.example.service2.model.Transaction;
import org.example.service2.model.enums.AccountStatus;
import org.example.service2.model.enums.TransactionStatus;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProducer transactionProducer;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              TransactionProducer transactionProducer,
                              AccountRepository accountRepository,
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionProducer = transactionProducer;
        this.accountService = accountService;
    }

    @LogDataSourceError
    @Transactional
    public Transaction save(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (TransactionException e) {
            throw new TransactionException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }


    public void sendTransToKafka(TransactionDto transactionDto) {
        try {
            transactionProducer.send(transactionDto);
        } catch (org.springframework.transaction.TransactionException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Transaction saveFromKafka(Transaction transaction) {
        Account account = transaction.getAccount();
        if (AccountStatus.OPEN == account.getStatus()) {
            transaction.setStatus(TransactionStatus.REQUESTED);
            accountService.updateBalance(account.getId(), transaction.getAmount());
        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
        }
        save(transaction);
        return transaction;
    }
}