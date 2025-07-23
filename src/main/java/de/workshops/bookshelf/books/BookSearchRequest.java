package de.workshops.bookshelf.books;

import jakarta.validation.constraints.Size;

public record BookSearchRequest(String author, @Size(min=10, max=14) String isbn) {
}
