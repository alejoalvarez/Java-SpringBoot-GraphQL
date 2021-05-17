package com.alejo.graphqldemo.resolver;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Client;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

    public Client client(BankAccount bankBankAccount){
        log.info("Retrieving client information wiht bank account id {} ", bankBankAccount.getId() );

        return Client.builder()
                .id("123")
                .firstName("Alejo")
                .lastName("Alvarez")
                .middleName("N/A")
                .build();
    }
}
