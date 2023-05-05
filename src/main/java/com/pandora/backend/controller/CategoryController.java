package com.pandora.backend.controller;

import com.pandora.backend.model.response.category.BookCategoryResponse;
import com.pandora.backend.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController extends PublicApiController {
    private final BookCategoryService bookCategoryService;

    @Autowired
    public CategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    @GetMapping(value = "/category")
    public ResponseEntity<List<BookCategoryResponse>> getAllCategories() {
        List<BookCategoryResponse> bookCategories = bookCategoryService.findAllByOrderByName();
        return new ResponseEntity<>(bookCategories, HttpStatus.OK);
    }
}
