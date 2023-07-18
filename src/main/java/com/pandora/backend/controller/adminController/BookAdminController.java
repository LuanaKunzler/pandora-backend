package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.admin.user.UserCreationRequest;
import com.pandora.backend.model.request.book.BookRequest;
import com.pandora.backend.model.response.book.BookResponse;
import com.pandora.backend.service.admin.BookAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Slf4j
public class BookAdminController extends ApiAdminController {

    private final BookAdminService service;

    public BookAdminController(BookAdminService service) {
        this.service = service;
    }

    @PostMapping(value = "/book")
    public ResponseEntity<Book> newBook(@RequestPart("imageUrl") MultipartFile imageUrl, @Valid BookRequest request) {
        try {
            var book = service.newBook(imageUrl, request);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/books")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "dateCreated") String sortField) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> booksPage = service.getBooks(pageable);
            return new ResponseEntity<>(booksPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/book/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        try {
            var response = service.getBookById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestPart("imageUrl") MultipartFile imageUrl, @Valid BookRequest request) {
        try {
            var book = service.updateBook(id, imageUrl, request);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/book/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        try {
            service.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
