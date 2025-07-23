package de.workshops.bookshelf.books;

import lombok.Data;

import java.util.Objects;

public class Book {
  private String title;
  private String description;
  private String author;
  private String isbn;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(author, book.author) && Objects.equals(isbn, book.isbn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, author, isbn);
  }
}
