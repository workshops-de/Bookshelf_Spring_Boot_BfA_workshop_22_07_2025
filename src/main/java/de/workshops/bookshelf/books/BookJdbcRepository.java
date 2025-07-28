package de.workshops.bookshelf.books;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookJdbcRepository {
  private final JdbcTemplate jdbcTemplate;

  public BookJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  List<Book> findAllBooks() {
    var sql = "SELECT * FROM book";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
  }

  Book saveBook(Book book) {
    var sql = "INSERT INTO book (isbn, title, author, description) VALUES (?, ?, ?, ?)";
    int updated = jdbcTemplate.update(sql,
        book.getIsbn(), book.getTitle(), book.getAuthor(), book.getDescription());
    return book;
  }
}
