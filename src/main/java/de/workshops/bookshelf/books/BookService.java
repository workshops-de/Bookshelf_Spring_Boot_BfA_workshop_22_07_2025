package de.workshops.bookshelf.books;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
  private final BookJdbcRepository bookRepository;

  public BookService(BookJdbcRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAllBooks();
  }

  Book getBookByIsbn(String anIsbn) {
    return bookRepository.findAllBooks().stream()
        .filter(b -> b.getIsbn().equals(anIsbn))
        .findFirst()
        .orElseThrow(() -> new BookException("Sorry, no book with this ISBN"));
  }

  List<Book> getBooksByAuthor(String anAuthor) {
    return bookRepository.findAllBooks().stream()
        .filter(b -> b.getAuthor().contains(anAuthor))
        .toList();
  }

  List<Book> searchBooks(BookSearchRequest request) {
    return bookRepository.findAllBooks().stream()
        .filter(b -> b.getIsbn().equals(request.isbn()) || b.getAuthor().contains(request.author()))
        .toList();
  }

  public Book createBook(Book book) {
    return bookRepository.saveBook(book);
  }
}
