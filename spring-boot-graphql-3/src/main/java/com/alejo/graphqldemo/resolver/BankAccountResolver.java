package com.alejo.graphqldemo.resolver;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankAccountResolver implements GraphQLQueryResolver {

    public BankAccount bankAccount(String id){
        log.info("Retrieving bank account {} ", id);

        return BankAccount.builder()
                .id(id)
                .currency(Currency.PESOS)
                .name("Alejo")
                .build();
    }
}
