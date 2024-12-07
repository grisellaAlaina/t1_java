package org.example.service2.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service2.dto.TransactionDto;
import org.example.service2.service.TransactionService;
import org.example.service2.util.TransactionMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.example.service2.model.Transaction;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionAcceptConsumer {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @KafkaListener(topics = "t1_demo_transaction_accept")
    public void consumeTransactionMessage(TransactionDto transactionDto) {
        log.info("Received message from t1_demo_transactions: {}", transactionDto);


        try {
            Transaction transaction = transactionMapper.toEntity(transactionDto);
            transaction.setTime(LocalDateTime.now());
            Transaction savedTransaction = transactionService.saveFromKafka(transaction);

            log.info("Transaction saved : {}", savedTransaction);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
