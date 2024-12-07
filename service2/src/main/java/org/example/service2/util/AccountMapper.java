package org.example.service2.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.example.service2.dto.AccountDto;
import org.example.service2.dto.TransactionDto;
import org.example.service2.model.Account;
import org.example.service2.model.Client;
import org.example.service2.model.Transaction;
import org.example.service2.model.enums.AccountStatus;
import org.example.service2.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountMapper {

    private final ClientRepository clientRepository;
    private final TransactionMapper transactionMapper;

    public Account toEntity(AccountDto dto) {
        Client client = clientRepository.findById(dto.getClientId()).orElse(null);

        Account account = Account.builder()
                .client(client)
                .accountType(dto.getAccountType())
                .balance(dto.getBalance())
                .accountId(UUID.randomUUID())
                .status(AccountStatus.OPEN)
                .frozenAmount(BigDecimal.ZERO)
                .build();

        Set<Transaction> transactionSet = new HashSet<>();
        if (dto.getTransactions() != null) {
            for (TransactionDto transactionDTO: dto.getTransactions()) {
                Transaction createdTransaction = Transaction.builder()
                        .account(account)
                        .time(transactionDTO.getTime())
                        .amount(transactionDTO.getAmount())
                        .build();
                transactionSet.add(createdTransaction);
            }
        }
        account.setTransactions(transactionSet);
        transactionSet.forEach(transaction -> transaction.setAccount(account));

        return account;
    }

    public AccountDto toDto(Account entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient().getId())
                .accountType(entity.getAccountType())
                .balance(entity.getBalance())
                .transactions(entity.getTransactions().stream().map(transactionMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
