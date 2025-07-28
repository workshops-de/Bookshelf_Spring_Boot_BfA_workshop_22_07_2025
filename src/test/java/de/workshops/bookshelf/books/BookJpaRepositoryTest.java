package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookJpaRepositoryTest {

  @Autowired
  BookJpaRepository underTest;

  @Test
  void findByIsbn() {
    var book = underTest.findByIsbn("978-3826655487");
    assertThat(book).isNotNull()
        .hasFieldOrPropertyWithValue("isbn", "978-3826655487");
  }

  // This test should show, that using JPA named query methods deliver the same result as methods using @Query
  @Test
  void searchByAuthorContainingOrIsbn() {
    var searchResult = underTest.searchByAuthorContainingOrIsbn("Rob", "978-0201633610");
    assertThat(searchResult).hasSize(2);

    var search = underTest.search("978-0201633610", "Rob");
    assertThat(search).hasSize(2);

    assertThat(search).isEqualTo(searchResult);
  }
}