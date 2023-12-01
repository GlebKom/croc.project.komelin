package ru.komelin.crocprojectkomelin.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;

@ControllerAdvice
public class RepositoryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = LinkNotFoundException.class)
    protected ResponseEntity<Object> handleLinkNotFoundException(LinkNotFoundException ex,
                                                                 WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }
}
