# Table of Contents

## Schema

Overview of the graphql-java-tools library which allows you to use the graphql schema language to build a graphql schema. This library takes a schema first approach and allows us to bring our own java object (matching the schema types) to fill in the implementation. This approach allows us to have complete control over the schema and have a good overview of what types are available.

Graphql tools by default will look for all **/*.graphqls files on the classpath and it will automatically combine and compile them into one graphql schema at application runtime. This is available at localhost:port/graphql/schema.json.

We then create our first graphql schema file with root type query.
Then we create a matching GraphqlQueryResolver which will match the root queries.
We then start graphql playground to view and play with our first graphql query.

When you create your schema is it crucial that you follow some best practices.

With all APIs, it is extremely important to have descriptive names. But with the nature of the graphql schema and evolution, one would argue that it is even more crucial. 

Simply, create a naming convention and place it in a doc

Wrapper types will allow your schema to freely evolve, makes changes in a backwards compatible way and we do not need to worry about our clients migrating to new types.

For example, If you have a field "userId", when you decide you require more user information, you will typically need to create a new type that contains the userId and your new fields. Here we need to deprecate userId and wait until all clients migrate over to the new type. Then clean-up userId. If you created a user type from the start, you could freely add new fields without planning backwards compatible changes.

It is important to note, this is not a hard rule. Please be pragmatic 

## Playground

GraphQL playground is an graphql IDE that allows you to interact with your live graphql service. Using playground you can:

 - Explore the documentation
 - Explore the schema
 - Create typesafe queries. This can be useful for creating your integration test request payloads.
 - Perform real graphql queries over subscription and http. 
 - Pre-load common graphql queries into tabs
 - Add authorization headers to your graphql queries
 - graphql variables, this can be useful to avoid manually manipulating the graphql query input parameters

Once you add playground-spring-boot-starter to the classpath, GraphQL Playground becomes accessible at root /playground. It uses an embedded GraphQL Playground React. GraphQL playground can be disabled by setting the property graphql.playground.enabled: false

Here I demonstrate how I like to pre-load common graphql queries, headers and variables into playground tabs. This really speeds up development by removing the need to save common queries locally, configure them and it allows you share queries across your team.

## Voyage schema visualizer

GraphQL voyager allows you to visualize your schema and explore the graphql API as an interactive graph.

With graphql-voyager you can visually explore your GraphQL API as an interactive graph. This is a great tool when designing or discussing your data model.

Here I show you how to embed voyager into your spring boot graphql application. 
Simply add the dependency voyager-spring-boot-starter.
You can disable it with the property voyager.enabled: false

## Resolver

In GraphQL SpringBoot there are two resolver marker interfaces, GraphQLQueryResolver and GraphQLResolver. 

The GraphQLQueryResolver should contain methods that match the original graphql schema queries. If there is a mismatch and a method cannot be matched, the server will fail at runtime startup.

GraphQLResolver can be used to resolve nested fields within the GraphQLQueryResolver's response types.

I recommend that you consider making the field type returned from each GraphQLResolver nullable. If a resolver would throw an exception/fail, then the field would be set to null. A side effect of null on non-nullable field would be that the complete query will fail and return data: null as it failed schema response validation. If it was nullable, the query would as least return the other available fields.

## Exception handler

Spring boot graphql supports Spring web's @ExceptionHandler annotations. 

To enable this we first need to set the graphql property: graphql.servlet.exception-handlers-enabled: true. We can then proceed to create a component with @ExceptionHandler methods, that will transform an exception into a GraphQLError implementation. This is the hook point where you can place your exception handling logic. In this example, I transform the exceptions into a simple ThrowableGraphQLError, but many are available in the library or you have the option to customize your own.

With this approach I recommend you create an "catch all" exception handler. This will ensure any spring, feign, jooq, hibernate etc runtime exception messages are not propagated to the client, but instead are shown a internal server error message. To sum up, if its not defined handled exception type, then show internal server error. Lets not leak info.

By default, graphql will create a DefaultGraphQLErrorHandler bean. This bean will be invoked with a list of all the graphql exceptions/errors that occurred during the query. The bean will then try to find a matching ExceptionHandler for the error's exception. If it finds a matching handler, it will then swap its wrapped error (containing the real exception) with your ErrorHandlers response object.

## DataFetcherResult -  returning data and errors

It is possible to return both data and multiple errors in a graphql resolver by returning a graphql.execution.DataFetcherResult either directly or wrapped in a CompletableFuture instance for asynchronous execution. (async graphql resolvers in next video).

This is a useful when your resolver may need to retrieve data from multiple sources or from another GraphQL resource.

For example, if a single resolver makes network connections to two different APIs. If one API returns successfully and the other does not, you may want to return the partially filled object and an error. If you throw an exception, then the full object will be null. If you return the object without an error, then this may mislead your clients. As the data may be temporarily unavailable, not null.

DataFetcherResult can also be used to pass extra context to lower resolvers without polluting your graphql schema domain objects. This also allows you to pass down new local context objects between parent and child fields.  If you return a local context via setLocalContext(value) then it will be passed down into any child fields via the graphql.schema.DataFetchingEnvironment#getLocalContext().

## Asynchronous resolvers

You can make graphql resolvers execute asynchronously by setting the return type to a CompletableFuture and executing the logic within another thread. This of course does not work if you do your logic as normal then return a CompletableFuture.completedFuture(u)

Async resolvers permit the graphql server to execute multiple resolver methods in parallel. For resolvers to execute in parallel, they must not depend on eachother (parent/child fields).

Executing in parallel is useful if the client requests multiple fields that do not depend on eachother and could take a considerable amount of time to fetch. 
We now have response times of worst-of (resolver A latency, resolver B latency) instead of resolver A latency + resolver B latency.

Take time to size and shape your threadpools, keep an eye on them over time. 

Also, be aware that when you execute resolvers async, then the child resolvers will execute in it's parents async thread (See below: standard CompletableFuture chain).

```
a { 
    # not async - tomcat user thread UT1
    b { 
    # async - Thread 1 - Pool 1
         c {
         # not async - executed in Thread 1 - Pool 1
          } 
    } 
    d {
    # not async - tomcat user thread UT1
    }
}
```

## Mutation

Most discussions of GraphQL focus on data fetching, but any complete data platform needs a way to modify server-side data as well.

In REST, any request might end up causing some side-effects on the server, but by convention it's suggested that one doesn't use GET requests to modify data. GraphQL is similar - technically any query could be implemented to cause a data write. However, it's useful to establish a convention that any operations that cause writes should be sent explicitly via a mutation.

Just like in queries, if the mutation field returns an object type, you can ask for nested fields. This can be useful for fetching the new state of an object after an update. 

## DataFetchingEnvironment

Every resolver is passed a DataFetchingEnvironment object which allows it to know more about what is being fetched and what arguments have been provided. To get access to this class, set it as the last parameter of your resolver method. It will be automatically injected. 

Here are some of my favorite DataFetchingEnvironment use-cases from production. Methods:

getArguments() - this represents the arguments that have been provided on a field and the values of those arguments that have been resolved from passed in variables, literals and default argument values.

getContext() - the context is object is set up when the query is first executed and stays the same over the lifetime of the query. The context can be any value and is typically used to give each data fetcher some calling context needed when trying to get field data. For example the current user authorization context can be created and set at the very start of the query, then all resolvers can access this context and pass it to the downstream services or resources.

ExecutionStepInfo getExecutionStepInfo() - Explains how this resolver was executed. I.e. the steps executed prior. This is available in a nice path format. Have a look

DataFetchingFieldSelectionSet getSelectionSet() - the selection set represents the child fields that have been “selected” under neath the currently executing field. This can be useful to help look ahead to see what sub field information a client wants. I have a dedicated video on this.

ExecutionId getExecutionId() - each query execution is given a unique id. You can use this perhaps on logs to tag each individual query or use a correlation and span id.

DataLoaderRegistry() - This will provide access to the graphql dataloaders, use to solve the n+1 problem.

## SelectionSet

A very powerful feature of graphql spring boot is that you have access to the query's selection set. A selection set is the set of fields the user requested. 

Having direct access to the fields can enable you to make performance optimized queries requesting only the needed data. For example an SQL backed system may be able to use the field sub selection to only retrieve the columns that have been asked for.

If you look ahead in the selection set, you may be able to optimize further and collapse two backend resource calls into one. For example, if you can retrieve the sellingLocations data within the products API call. You can group everything into one API query instead of two.

```graphql
query {
        products {
            # the fields below represent the selection set
            name
            description
            sellingLocations {
                state
            }
        }
    }
```

The selection set (DataFetchingFieldSelectionSet) contains many useful utility methods such as:  contains, containsAnyOf, containsAnyOf. These can be used as the predicate to make your API call selection. 

To get the requested field names you can stream the fields, filter and collect into a set.
- Custom Scalar
- Date Type
- Input validation
- Listener
- Pagination (Edges, Nodes, Cursosr)
- Custom Context
- Data Loader (N+1 problem )
- Instrumentation (Request Logging)
- Request Tracing
- Correlation ID (Thread propagation)
- Integration testing
- JVM Profiling (VisualVM, JMeter)
- DataLoader Key Context
- Spring Security Authorization
- Schema Directive Authorization
- Subscription with reactor
- Subscription Authorization


[Example 1](https://github.com/alejoalvarez/Java-SpringBoot-GraphQL/tree/main/spring-boot-graphql)
This is a basic example using:
- Datafetch
- JPA
- Spring Boot

[Example 2](https://github.com/alejoalvarez/Java-SpringBoot-GraphQL/tree/main/spring-boot-graphql-2)
This is a basic example using:
- GraphQLApi
- graphql.spqr
   
[Example 3](https://github.com/alejoalvarez/Java-SpringBoot-GraphQL/tree/main/spring-boot-graphql-3)
- **This is the most complete example**
