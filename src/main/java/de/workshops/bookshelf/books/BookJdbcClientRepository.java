package de.workshops.bookshelf.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookJdbcClientRepository {
  private final Logger LOGGER = LoggerFactory.getLogger(BookJdbcClientRepository.class);
  private final JdbcClient jdbcClient;

  public BookJdbcClientRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  List<Book> findAllBooks() {
    var sql = "SELECT * FROM book";
    return jdbcClient.sql(sql)
        .query(new BeanPropertyRowMapper<>(Book.class))
        .list();
  }

  Book saveBook(Book book) {
    var sql = "INSERT INTO book (isbn, title, author, description) VALUES (:isbn, :title, :author, :description)";
    var generatedKeyHolder = new GeneratedKeyHolder();
    jdbcClient.sql(sql)
        .param("isbn", book.getIsbn())
        .param("title", book.getTitle())
        .param("author", book.getAuthor())
        .param("description", book.getDescription())
        .update(generatedKeyHolder);

    var key = generatedKeyHolder.getKey();
    LOGGER.info("Generated key: {}", key);

    return book;
  }

}
