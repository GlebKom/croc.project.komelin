package ru.komelin.crocprojectkomelin.exception.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.SpringBootStorageException;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class StorageExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = FileEmptyException.class)
    protected ResponseEntity<Object> handleFileEmptyException(FileEmptyException ex,
                                                              WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = FileDownloadException.class)
    protected ResponseEntity<Object> handleFileDownloadException(FileDownloadException ex,
                                                                 WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(value
            = { SpringBootStorageException.class})
    protected ResponseEntity<Object> handleConflict(
            SpringBootStorageException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Handle exception that occur when the call was transmitted successfully, but Amazon S3 couldn't process
    // it, so it returned an error response.

    @ExceptionHandler(value
            = { AmazonServiceException.class})
    protected ResponseEntity<Object> handleAmazonServiceException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // Handle exceptions that occur when Amazon S3 couldn't be contacted for a response, or the client
    // couldn't parse the response from Amazon S3.

    @ExceptionHandler(value = SdkClientException.class)
    protected ResponseEntity<Object> handleSdkClientException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value
            = {IOException.class, MultipartException.class})
    protected ResponseEntity<Object> handleIOException(
            Exception ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
