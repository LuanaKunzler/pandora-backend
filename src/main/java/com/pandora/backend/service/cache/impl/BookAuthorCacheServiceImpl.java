package com.pandora.backend.service.cache.impl;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.repository.AuthorRepository;
import com.pandora.backend.service.cache.BookAuthorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "book_author")
public class BookAuthorCacheServiceImpl implements BookAuthorCacheService {

    private final AuthorRepository authorRepository;

    @Autowired
    public BookAuthorCacheServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<BookAuthor> findAll() {
        return authorRepository.findAll();
    }
}
