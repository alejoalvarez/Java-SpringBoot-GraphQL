package com.alejo.graphqldemo.resolver.client;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Client;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

    public Client client(BankAccount bankAccount){
        log.info("Retrieving client information wiht bank account id {} ", bankAccount.getId() );

        return Client.builder()
                .id("111")
                .firstName("Alejo11")
                .lastName("Alvarez11s")
                .middleName("N/A")
                .build();
    }
}
