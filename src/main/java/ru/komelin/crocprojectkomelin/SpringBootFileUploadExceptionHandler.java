package ru.komelin.crocprojectkomelin;

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
import ru.komelin.crocprojectkomelin.exception.repository.PhotoNotFoundException;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.SpringBootFileUploadException;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class SpringBootFileUploadExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle repository exceptions

    @ExceptionHandler(value = PhotoNotFoundException.class)
    protected ResponseEntity<Object> handlePhotoNotFoundException(PhotoNotFoundException ex,
                                                                  WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }


    // Handle storage exceptions

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
            = { SpringBootFileUploadException.class})
    protected ResponseEntity<Object> handleConflict(
            SpringBootFileUploadException ex, WebRequest request) {
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

    @ExceptionHandler(value
            = { SdkClientException.class})
    protected ResponseEntity<Object> handleSdkClientException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value
            = {IOException.class, FileNotFoundException.class, MultipartException.class})
    protected ResponseEntity<Object> handleIOException(
            Exception ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
