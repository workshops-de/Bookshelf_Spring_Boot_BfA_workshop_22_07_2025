package de.workshops.bookshelf.books;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
public class BookRestController {

  private final BookService bookService;

  public BookRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public List<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{isbn}")
  public ResponseEntity<Book> getBookByIsbn(@PathVariable(name = "isbn") @Size(min = 10, max = 14) String anIsbn) {
    var book = bookService.getBookByIsbn(anIsbn);
    return ResponseEntity.ok(book);
  }

  @GetMapping(params = "author")
  public List<Book> getBooksByAuthor(@RequestParam(name = "author", required = false) String anAuthor) {
    return bookService.getBooksByAuthor(anAuthor);
  }

  @PostMapping("/search")
  List<Book> search(@RequestBody @Valid BookSearchRequest request) {
    return bookService.searchBooks(request);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  Book createBook(@RequestBody @Valid Book book) {
    return bookService.createBook(book);
  }

  @ExceptionHandler(BookException.class)
  ResponseEntity<String> handleBookException(BookException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
