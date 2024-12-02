package ru.t1.java.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.TransactionProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;

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