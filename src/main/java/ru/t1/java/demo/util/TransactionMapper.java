package ru.t1.java.demo.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;

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
