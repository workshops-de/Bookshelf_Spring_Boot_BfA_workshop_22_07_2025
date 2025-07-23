package de.workshops.bookshelf.books;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
public class BookRestController {

  private final ObjectMapper mapper;
  private final ResourceLoader resourceLoader;

  private List<Book> books;

  public BookRestController(ObjectMapper mapper, ResourceLoader resourceLoader) {
    this.mapper = mapper;
    this.resourceLoader = resourceLoader;
  }

  @PostConstruct
  public void init() throws IOException {
    final var resource = resourceLoader.getResource("classpath:books.json");
    this.books = mapper.readValue(resource.getInputStream(), new TypeReference<>() {
    });
  }

  @GetMapping
  public List<Book> getAllBooks() {
    return books;
  }

  @GetMapping("/{isbn}")
  public ResponseEntity<Book> getBookByIsbn(@PathVariable(name = "isbn") @Size(min = 10, max = 14) String anIsbn) {
    var book = books.stream()
        .filter(b -> b.getIsbn().equals(anIsbn))
        .findFirst()
        .orElseThrow(() -> new BookException("Sorry, no book with this ISBN"));
    return ResponseEntity.ok(book);
  }

  @GetMapping(params = "author")
  public List<Book> getBooksByAuthor(@RequestParam(name = "author") @NotBlank String anAuthor) {
    return books.stream()
        .filter(b -> b.getAuthor().contains(anAuthor))
        .toList();
  }

  @PostMapping("/search")
  List<Book> search(@RequestBody @Valid BookSearchRequest request) {
    return books.stream()
        .filter(b -> b.getAuthor().contains(request.author())
            || b.getIsbn().equals(request.isbn()))
        .toList();
  }

//  @ExceptionHandler(BookException.class)
  ResponseEntity<String> handleBookException(BookException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
