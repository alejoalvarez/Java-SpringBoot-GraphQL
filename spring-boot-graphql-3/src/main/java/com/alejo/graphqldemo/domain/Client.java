package com.alejo.graphqldemo.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Client {

    String id;
    String firstName;
    String middleName;
    String lastName;
}
