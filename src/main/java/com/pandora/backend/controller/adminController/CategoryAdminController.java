package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.model.request.admin.category.CategoryCreationRequest;
import com.pandora.backend.model.request.admin.category.CategoryUpdateRequest;
import com.pandora.backend.service.admin.CategoryAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class CategoryAdminController extends ApiAdminController {
    private final CategoryAdminService service;

    public CategoryAdminController(CategoryAdminService service) {
        this.service = service;
    }

    @PostMapping(value = "/category")
    public ResponseEntity<HttpStatus> newCategory(@RequestBody @Valid CategoryCreationRequest categoryCreationRequest) {
        try {
            service.newCategory(categoryCreationRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<Page<BookCategory>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "name") String sortField) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BookCategory> categoriesPage = service.getCategories(pageable, sortDirection, sortField);
            return new ResponseEntity<>(categoriesPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/category/{id}")
    public ResponseEntity<BookCategory> getCategoryById(@PathVariable Long id) {
        try {
            var category = service.getCategoryById(id);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<BookCategory> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest updateRequest) {
        BookCategory updatedCategory = service.updateCategory(id, updateRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
        try {
            service.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}