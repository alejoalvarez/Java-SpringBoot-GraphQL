package com.alejo.graphql.springbootgraphql.repository;

import com.alejo.graphql.springbootgraphql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {


}
