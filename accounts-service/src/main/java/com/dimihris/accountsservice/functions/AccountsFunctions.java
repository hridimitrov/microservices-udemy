package com.dimihris.accountsservice.functions;

import com.dimihris.accountsservice.service.AccountsService;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {

    private static final Logger logger = LoggerFactory.getLogger(AccountsFunctions.class);


    @Bean
    public Consumer<Long> updateCommunication(AccountsService accountsService) {
        return accountNumber -> {
            logger.info("Updating communication status for the account number: " + accountNumber.toString());
            accountsService.updateCommunicationStatus(accountNumber);
        };
    }
}
