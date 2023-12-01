package ru.komelin.crocprojectkomelin.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.request.RequestPerSecondExceededException;

@ControllerAdvice
public class RequestExceptionHandler extends RepositoryExceptionHandler{
    @ExceptionHandler(value = DownloadLimitExceededException.class)
    protected ResponseEntity<Object> handleDownloadLimitExceededException(DownloadLimitExceededException ex,
                                                                          WebRequest request) {

        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = DownloadDateExceededException.class)
    protected ResponseEntity<Object> handleDownloadDateExceededException(DownloadDateExceededException ex,
                                                                         WebRequest request) {

        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = RequestPerSecondExceededException.class)
    protected ResponseEntity<Object> handleRequestPerSecondExceededException(RequestPerSecondExceededException ex,
                                                                             WebRequest request) {

        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.TOO_MANY_REQUESTS,
                request);
    }
}
