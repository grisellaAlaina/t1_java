package ru.t1.java.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.TransactionProducer;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProducer transactionProducer;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionProducer transactionProducer) {
        this.transactionRepository = transactionRepository;
        this.transactionProducer = transactionProducer;
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
}