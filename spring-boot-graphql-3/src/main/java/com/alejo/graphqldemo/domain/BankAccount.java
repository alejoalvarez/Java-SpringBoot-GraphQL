package com.alejo.graphqldemo.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BankAccount {
    String id;
    Client client;
    Currency currency;
}
