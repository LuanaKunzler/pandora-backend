package com.pandora.backend.service.serviceImpl;

import com.pandora.backend.converter.category.BookCategoryResponseConverter;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.model.response.category.BookCategoryResponse;
import com.pandora.backend.service.BookCategoryService;
import com.pandora.backend.service.cache.BookCategoryCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    private final BookCategoryCacheService bookCategoryCacheService;
    private final BookCategoryResponseConverter bookCategoryResponseConverter;

    @Autowired
    public BookCategoryServiceImpl(BookCategoryCacheService bookCategoryCacheService,
                                   BookCategoryResponseConverter bookCategoryResponseConverter) {
        this.bookCategoryCacheService = bookCategoryCacheService;
        this.bookCategoryResponseConverter = bookCategoryResponseConverter;
    }


    @Override
    public List<BookCategoryResponse> findAllByOrderByName() {
        List<BookCategory> bookCategories = bookCategoryCacheService.findAllByOrderByName();
        if (bookCategories.isEmpty()) {
            throw new ResourceNotFoundException("Could not find book categories");
        }
        return bookCategories
                .stream()
                .map(bookCategoryResponseConverter)
                .collect(Collectors.toList());
    }

}
