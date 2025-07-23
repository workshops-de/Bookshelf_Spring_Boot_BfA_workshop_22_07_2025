package de.workshops.bookshelf;

import de.workshops.bookshelf.books.BookException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BookException.class)
  public ProblemDetail handleException(BookException ex) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.I_AM_A_TEAPOT, ex.getMessage());
    problemDetail.setTitle("Book not found");
    return problemDetail;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle("Constraint violation");
    return problemDetail;
  }
}
