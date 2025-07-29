package de.workshops.bookshelf.books;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser
class BookRestControllerMockMvcTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void getAllBooks() throws Exception {
    var mvcResult = mockMvc.perform(get("/book"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andReturn();

    var responseBody = mvcResult.getResponse().getContentAsString();

    List<Book> books = objectMapper.readValue(responseBody, new TypeReference<>() {});
    assertThat(books)
        .isNotEmpty()
        .hasSize(3);
  }

  @Test
  void getBookByIsbn() throws Exception {
    var mvcResult = mockMvc.perform(get("/book/978-0201633610"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Design Patterns")))
        .andReturn();

    var responseBody = mvcResult.getResponse().getContentAsString();

    Book book = objectMapper.readValue(responseBody, new TypeReference<>() {});

    assertThat(book).isNotNull()
        .hasFieldOrPropertyWithValue("isbn", "978-0201633610");
  }

  @Test
  void getBookByIsbn_nonExistingIsbn() throws Exception {
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