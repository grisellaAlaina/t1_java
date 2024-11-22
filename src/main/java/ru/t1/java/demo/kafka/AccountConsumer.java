package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountConsumer {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @KafkaListener(topics = "t1_demo_accounts")
    public void consumeAccountMessage(AccountDto accountDto) {
        log.info("Received message from t1_demo_accounts: {}", accountDto);

        try {
            Account account = accountMapper.toEntity(accountDto);
            Account savedAccount = accountService.save(account);

            log.info("Account saved : {}", savedAccount);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}