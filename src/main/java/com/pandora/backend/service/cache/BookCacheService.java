package com.pandora.backend.service.cache;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.entity.BookCategory;

import java.util.List;

public interface BookCacheService {

    Book findByUrl(String url);

    Book findById(Long id);

    List<Book> findTop8ByOrderByDateCreatedDesc();

    List<Book> getRelatedBooks(BookCategory bookCategory, Long id);

    List<Book> findTop8ByOrderBySellCountDesc();

}
