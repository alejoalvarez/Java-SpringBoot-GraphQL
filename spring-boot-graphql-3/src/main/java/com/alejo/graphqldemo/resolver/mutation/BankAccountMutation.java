package com.alejo.graphqldemo.resolver.mutation;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Currency;
import com.alejo.graphqldemo.domain.input.CreateBankAccountInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankAccountMutation implements GraphQLMutationResolver {

    public BankAccount createBankAccount(CreateBankAccountInput createBankAccountInput){
        log.info("Creating bank account for {}", createBankAccountInput);

        return BankAccount.builder()
                .id("9999")
                .currency(Currency.USD)
                .build();
    }
}
