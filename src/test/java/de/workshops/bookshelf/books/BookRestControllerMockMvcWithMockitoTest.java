package de.workshops.bookshelf.books;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookRestController.class)
class BookRestControllerMockMvcWithMockitoTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  BookService bookServiceMock;

  @Captor
  ArgumentCaptor<String> isbnCaptor;

  @Test
  void getAllBooks() throws Exception {
    // given
    when(bookServiceMock.getAllBooks()).thenReturn(List.of(new Book(), new Book(), new Book()));

    // when
    var mvcResult = mockMvc.perform(get("/book"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    // then
    var responseBody = mvcResult.getResponse().getContentAsString();

    List<Book> books = objectMapper.readValue(responseBody, new TypeReference<>() {});
    assertThat(books)
        .isNotEmpty()
        .hasSize(3);
  }

  @Test
  void getBookByIsbn() throws Exception {
    var expectedBook = new Book();
    expectedBook.setIsbn("978-0201633610");
    expectedBook.setTitle("Design Patterns");

    when(bookServiceMock.getBookByIsbn(isbnCaptor.capture())).thenReturn(expectedBook);

    var mvcResult = mockMvc.perform(get("/book/1111111111"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Design Patterns")))
        .andReturn();

    var responseBody = mvcResult.getResponse().getContentAsString();

    Book book = objectMapper.readValue(responseBody, new TypeReference<>() {});

    assertThat(book).isNotNull()
        .hasFieldOrPropertyWithValue("isbn", "978-0201633610");

    var isbnCaptorValue = isbnCaptor.getValue();
    assertThat(isbnCaptorValue).isEqualTo("1111111111");
  }

  @Test
  void getBookByIsbn_nonExistingIsbn() throws Exception {
    when(bookServiceMock.getBookByIsbn(anyString()))
        .thenThrow(new BookException("Sorry, no book with this ISBN"));

    mockMvc.perform(get("/book/978-0201633611"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getBookByIsbn_isbnTooLong() throws Exception {
    var mvcResult = mockMvc.perform(get("/book/978-02016336111111111"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andReturn();

    var responseBody = mvcResult.getResponse().getContentAsString();
    ProblemDetail problemDetail = objectMapper.readValue(responseBody, ProblemDetail.class);
    assertThat(problemDetail.getTitle()).isEqualTo("Constraint Violation");
  }
}