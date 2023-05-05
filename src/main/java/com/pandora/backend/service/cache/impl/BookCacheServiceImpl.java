package com.pandora.backend.service.cache.impl;

import com.pandora.backend.repository.BookRepository;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.service.cache.BookCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "book")
public class BookCacheServiceImpl implements BookCacheService {

    private final BookRepository bookRepository;

    @Autowired
    public BookCacheServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Cacheable(key = "#url")
    public Book findByUrl(String url) {
        return bookRepository.findByBookUrl(url).orElse(null);
    }

    @Override
    @Cacheable(key = "#url")
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }


    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<Book> findTop8ByOrderByDateCreatedDesc() {
        return bookRepository.findTop8ByOrderByDateCreatedDesc();
    }

    @Override
    @Cacheable(key = "{#bookCategory.name,#id}", unless = "#result.size()==0")
    public List<Book> getRelatedBooks(BookCategory bookCategory, Long id) {
        List<Book> bookList = bookRepository.findTop8ByBookCategoryAndIdIsNot(bookCategory, id);
        if (bookList.size() < 8) {
            bookList.addAll(bookRepository.findAllByBookCategoryIsNot(bookCategory, PageRequest.of(0, 8 - bookList.size())));
        }
        return bookList;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<Book> findTop8ByOrderBySellCountDesc() {
        return bookRepository.findTop8ByOrderBySellCountDesc();
    }

}
