package ru.komelin.crocprojectkomelin.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> error() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
