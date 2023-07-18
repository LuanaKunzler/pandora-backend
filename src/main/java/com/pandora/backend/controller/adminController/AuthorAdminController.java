package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.request.admin.author.AuthorCreationRequest;
import com.pandora.backend.model.request.admin.author.AuthorUpdateRequest;
import com.pandora.backend.model.request.admin.discount.DiscountCreationRequest;
import com.pandora.backend.model.request.admin.discount.DiscountUpdateRequest;
import com.pandora.backend.service.admin.AuthorAdminService;
import com.pandora.backend.service.admin.DiscountAdminService;
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
public class AuthorAdminController extends ApiAdminController {
    private final AuthorAdminService service;

    public AuthorAdminController(AuthorAdminService service) {
        this.service = service;
    }

    @PostMapping(value = "/author")
    public ResponseEntity<HttpStatus> newAuthor(@RequestBody @Valid AuthorCreationRequest authorCreationRequest) {
        try {
            service.newAuthor(authorCreationRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<Page<BookAuthor>> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "name") String sortField) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BookAuthor> authorsPage = service.getAuthors(pageable, sortDirection, sortField);
            return new ResponseEntity<>(authorsPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/author/{id}")
    public ResponseEntity<BookAuthor> getAuthorById(@PathVariable Long id) {
        try {
            var author = service.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<BookAuthor> updateAuthor(@PathVariable Long id, @RequestBody AuthorUpdateRequest updateRequest) {
        BookAuthor updatedAuthor = service.updateAuthor(id, updateRequest);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<HttpStatus> deleteAuthor(@PathVariable Long id) {
        try {
            service.deleteAuthor(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
