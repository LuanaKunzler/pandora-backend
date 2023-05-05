package com.pandora.backend.service.cache.impl;

import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.repository.BookCategoryRepository;
import com.pandora.backend.service.cache.BookCategoryCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "book_category")
public class BookCategoryCacheServiceImpl implements BookCategoryCacheService {

    private final BookCategoryRepository bookCategoryRepository;

    @Autowired
    public BookCategoryCacheServiceImpl(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<BookCategory> findAllByOrderByName() {
        return bookCategoryRepository.findAllByOrderByName();
    }

}
