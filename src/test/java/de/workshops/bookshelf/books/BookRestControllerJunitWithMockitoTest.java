package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRestControllerJunitWithMockitoTest {

  @Mock
  BookService bookServiceMock;

  @InjectMocks
  BookRestController underTest;

  @Test
  void getAllBooks() {
    when(bookServiceMock.getAllBooks()).thenReturn(List.of(new Book(), new Book()));

    var allBooks = underTest.getAllBooks();
    assertNotNull(allBooks);
    assertEquals(2, allBooks.size());
  }
}