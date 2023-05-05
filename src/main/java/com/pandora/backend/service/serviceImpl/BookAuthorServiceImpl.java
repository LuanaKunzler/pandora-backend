package com.pandora.backend.service.serviceImpl;

import com.pandora.backend.converter.author.BookAuthorResponseConverter;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.response.author.BookAuthorResponse;
import com.pandora.backend.service.BookAuthorService;
import com.pandora.backend.service.cache.BookAuthorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

    private final BookAuthorCacheService bookAuthorCacheService;
    private final BookAuthorResponseConverter bookAuthorResponseConverter;

    @Autowired
    public BookAuthorServiceImpl(BookAuthorCacheService bookAuthorCacheService,
                                 BookAuthorResponseConverter bookAuthorResponseConverter) {
        this.bookAuthorCacheService = bookAuthorCacheService;
        this.bookAuthorResponseConverter = bookAuthorResponseConverter;
    }


    @Override
    public List<BookAuthorResponse> findAll() {
        List<BookAuthor> bookAuthors = bookAuthorCacheService.findAll();
        if (bookAuthors.isEmpty()) {
            throw new ResourceNotFoundException("Could not find book Authors");
        }
        return bookAuthors
                .stream()
                .map(bookAuthorResponseConverter)
                .collect(Collectors.toList());
    }

}
