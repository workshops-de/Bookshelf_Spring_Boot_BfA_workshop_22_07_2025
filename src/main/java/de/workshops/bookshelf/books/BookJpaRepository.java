package de.workshops.bookshelf.books;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookJpaRepository extends ListCrudRepository<Book, Long> {
  Book findByIsbn(String isbn);

  List<Book> searchByAuthorContainingOrIsbn(String author, String isbn);

  @Query("SELECT b FROM Book b WHERE b.isbn = ?1 OR b.author LIKE %?2%")
  List<Book> search(String isbn, String author);
}
