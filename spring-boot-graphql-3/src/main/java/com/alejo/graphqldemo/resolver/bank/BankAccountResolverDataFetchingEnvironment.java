package com.alejo.graphqldemo.resolver.bank;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Currency;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BankAccountResolverDataFetchingEnvironment {

    public BankAccount bankAccount(String id, DataFetchingEnvironment environment){
        log.info("Retrieving bank account {} ", id);
        var requestFields =environment.getSelectionSet().getFields().stream()
                .map(SelectedField::getName).collect(Collectors.toUnmodifiableSet());

        System.out.println(requestFields);
        return BankAccount.builder()
                .id(id)
                .currency(Currency.PESOS)
                .build();
    }

}
