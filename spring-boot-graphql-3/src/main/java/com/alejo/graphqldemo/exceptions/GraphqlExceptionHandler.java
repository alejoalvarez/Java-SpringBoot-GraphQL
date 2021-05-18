package com.alejo.graphqldemo.exceptions;

import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class GraphqlExceptionHandler {

    @ExceptionHandler(GraphQLException.class)
    public ThrowableGraphQLError handler(GraphQLException e){
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ThrowableGraphQLError handler(RuntimeException e){
        return new ThrowableGraphQLError(e, "Internal Server Error");
    }

}
