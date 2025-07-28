package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookJdbcRepository.class)
class BookJdbcRepositoryTest {
  @Autowired
  BookJdbcRepository underTest;

  @Test
  void findAllBooks() {
    var allBooks = underTest.findAllBooks();
    assertThat(allBooks).hasSizeGreaterThanOrEqualTo(3);
  }

  @Test
  void saveBook() {
    var book = new Book();
    book.setIsbn("1111111111");
    book.setTitle("My first book");
    book.setAuthor("Birgit Kratz");
    book.setDescription("A best selling story and must read.");

    var savedBook = underTest.saveBook(book);

    assertThat(underTest.findAllBooks()).hasSize(4);
  }
}