package com.pandora.backend.controller;

import com.pandora.backend.model.response.author.BookAuthorResponse;
import com.pandora.backend.service.BookAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController extends PublicApiController {

    private final BookAuthorService bookAuthorService;

    @Autowired
    public AuthorController(BookAuthorService bookAuthorService) {
        this.bookAuthorService = bookAuthorService;
    }


    @GetMapping(value = "/authors")
    public ResponseEntity<List<BookAuthorResponse>> getAllAuthors() {
        List<BookAuthorResponse> bookAuthors = bookAuthorService.findAll();
        return new ResponseEntity<>(bookAuthors, HttpStatus.OK);
    }
}
