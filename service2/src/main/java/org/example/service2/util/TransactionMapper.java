package org.example.service2.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.example.service2.dto.TransactionDto;
import org.example.service2.model.Account;
import org.example.service2.model.Transaction;
import org.example.service2.model.enums.TransactionStatus;
import org.example.service2.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private final AccountRepository accountRepository;

    public Transaction toEntity(TransactionDto dto) {
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        return Transaction.builder()
                .amount(dto.getAmount())
                .time(LocalDateTime.now())
                .account(account)
                .status(TransactionStatus.ACCEPTED)
                .transactionId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public TransactionDto toDto(Transaction entity) {
        return TransactionDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .accountId(entity.getAccount().getId())
                .build();
    }
}
