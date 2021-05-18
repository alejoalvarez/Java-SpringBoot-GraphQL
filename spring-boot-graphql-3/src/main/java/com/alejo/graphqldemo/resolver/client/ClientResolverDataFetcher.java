package com.alejo.graphqldemo.resolver.client;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Client;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolverDataFetcher implements GraphQLResolver<BankAccount> {

    // change method client for client2 in ClientResolver for test this functionality and change client2 for client
    public DataFetcherResult<Client> client2(BankAccount bankAccount){
        log.info("Retrieving client information with bank account id {} ", bankAccount.getId() );

        return DataFetcherResult.<Client>newResult()
                .data(Client.builder()
                        .id("222")
                        .firstName("Alejo22")
                        .lastName("Alvarez22")
                        .middleName("N/A")
                        .build())
                //.error(new GenericGraphQLError("Could not get sub-client id")) //is optional
                .build();
    }
}
