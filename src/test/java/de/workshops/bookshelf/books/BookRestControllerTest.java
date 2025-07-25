package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookRestControllerTest {

  @Autowired
  private BookRestController underTest;

  @Test
  void getAllBooks() {
    assertThat(underTest.getAllBooks())
        .isNotEmpty()
        .hasSize(3);

    assertEquals(3, underTest.getAllBooks().size());
  }

}