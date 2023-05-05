package com.pandora.backend.service;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.response.book.BookDetailsResponse;
import com.pandora.backend.model.response.book.BookResponse;

import java.util.List;

public interface BookService {

    BookDetailsResponse findByUrl(String url);

    List<BookResponse> getAll(Integer page, Integer size, String sort, String category, Float minPrice,
                              Float maxPrice, String author);

    Long getAllCount(String category, Float minPrice, Float maxPrice, String author);

    Book findBookById(Long id);

    List<BookResponse> getRelatedBooks(String url);

    List<BookResponse> getNewlyAddedBooks();

    List<BookResponse> getMostSelling();

    List<BookResponse> getInterested();

    List<BookResponse> searchBookDisplay(String keyword, Integer page, Integer size);

}
