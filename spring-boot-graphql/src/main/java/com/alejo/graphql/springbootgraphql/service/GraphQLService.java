package com.alejo.graphql.springbootgraphql.service;

import com.alejo.graphql.springbootgraphql.model.Book;
import com.alejo.graphql.springbootgraphql.repository.BookRepository;
import com.alejo.graphql.springbootgraphql.service.datafetcher.AllBooksDataFetcher;
import com.alejo.graphql.springbootgraphql.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    @Autowired
    BookRepository bookRepository;

    @Value("classpath:books.graphql")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    private BookDataFetcher bookDataFetcher;
    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    // load schema at application start up
    @PostConstruct
    private void loadSchema() throws IOException{
        // load books into the Book Repository
        loadDataIntoHSQL();
        // get schema
        File schemaFile = resource.getFile();
        //parse schema
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private void loadDataIntoHSQL() {

        Stream.of(
                new Book("111","Test1", "publisherAlejo1", new String[]{"Alejo1, Alejo11"}, "11/05/2021"),
                new Book("222","Test2", "publisherAlejo2", new String[]{"Alejo2, Alejo22"}, "22/05/2021"),
                new Book("333","Test3", "publisherAlejo3", new String[]{"Alejo3, Alejo33"}, "33/05/2021"),
                new Book("444","Test4", "publisherAlejo4", new String[]{"Alejo4, Alejo44"}, "44/05/2021")
        ).forEach(book -> {
                bookRepository.save(book);
        });
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allBooks", allBooksDataFetcher)
                        .dataFetcher("book", bookDataFetcher))
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
