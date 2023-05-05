package com.pandora.backend.controller;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.response.book.BookDetailsResponse;
import com.pandora.backend.model.response.book.BookResponse;
import com.pandora.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class BookController extends PublicApiController {
    private final BookService bookService;


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping(value = "/book")
    public ResponseEntity<List<BookResponse>> getAll(@RequestParam("page") Integer page,
                                                     @RequestParam("size") Integer pageSize,
                                                     @RequestParam(value = "sort", required = false) String sort,
                                                     @RequestParam(value = "category", required = false) String category,
                                                     @RequestParam(value = "minPrice", required = false) Float minPrice,
                                                     @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                                     @RequestParam(value = "author", required = false) String author) {
        if (Objects.isNull(page) || page < 0) {
            throw new InvalidArgumentException("Invalid page");
        }
        if (Objects.isNull(pageSize) || pageSize < 0) {
            throw new InvalidArgumentException("Invalid pageSize");
        }
        List<BookResponse> books = bookService.getAll(page, pageSize, sort, category, minPrice, maxPrice, author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/book/count")
    public ResponseEntity<Long> getAllCount(@RequestParam(value = "category", required = false) String category,
                                            @RequestParam(value = "minPrice", required = false) Float minPrice,
                                            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                            @RequestParam(value = "author", required = false) String author) {
        Long bookCount = bookService.getAllCount(category, minPrice, maxPrice, author);
        return new ResponseEntity<>(bookCount, HttpStatus.OK);
    }

    @GetMapping(value = "/book/{url}")
    public ResponseEntity<BookDetailsResponse> getByUrl(@PathVariable("url") String url) {
        if (url.isBlank()) {
            throw new InvalidArgumentException("Invalid url params");
        }
        BookDetailsResponse bookDetailsResponse = bookService.findByUrl(url);
        return new ResponseEntity<>(bookDetailsResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/book/related/{url}")
    public ResponseEntity<List<BookResponse>> getByRelated(@PathVariable("url") String url) {
        if (url.isBlank()) {
            throw new InvalidArgumentException("Invalid url params");
        }
        List<BookResponse> books = bookService.getRelatedBooks(url);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/book/recent")
    public ResponseEntity<List<BookResponse>> getByNewlyAdded() {
        List<BookResponse> books = bookService.getNewlyAddedBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/book/mostselling")
    public ResponseEntity<List<BookResponse>> getByMostSelling() {
        List<BookResponse> books = bookService.getMostSelling();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/book/interested")
    public ResponseEntity<List<BookResponse>> getByInterested() {
        List<BookResponse> books = bookService.getInterested();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/book/search")
    public ResponseEntity<List<BookResponse>> searchBook(@RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer size,
                                                               @RequestParam("keyword") String keyword) {
        List<BookResponse> books = bookService.searchBookDisplay(keyword, page, size);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
