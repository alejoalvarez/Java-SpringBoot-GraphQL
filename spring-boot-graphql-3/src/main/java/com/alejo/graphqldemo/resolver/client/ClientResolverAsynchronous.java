package com.alejo.graphqldemo.resolver.client;

import com.alejo.graphqldemo.domain.BankAccount;
import com.alejo.graphqldemo.domain.Client;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class ClientResolverAsynchronous implements GraphQLResolver<BankAccount> {

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    // change method client for client1 in ClientResolver and client2 in ClientResolverDataFetcher for test this functionality
    public CompletableFuture<Client> client3(BankAccount bankAccount){
        log.info("Retrieving client information with bank account id {} ", bankAccount.getId() );

        return CompletableFuture.supplyAsync(
                () -> {
                    return Client.builder()
                            .id("333")
                            .lastName("Alejo33")
                            .firstName("Alvarez33")
                            .build();
                },
                executorService);
    }
}
