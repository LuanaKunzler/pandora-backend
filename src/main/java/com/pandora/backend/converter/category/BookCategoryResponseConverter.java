package com.pandora.backend.converter.category;

import com.pandora.backend.model.dto.CategoryDTO;
import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.model.response.category.BookCategoryResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookCategoryResponseConverter implements Function<BookCategory, BookCategoryResponse> {
    @Override
    public BookCategoryResponse apply(BookCategory bookCategory) {
        BookCategoryResponse bookCategoryResponse = new BookCategoryResponse();
        bookCategoryResponse.setCategory(CategoryDTO.builder().name(bookCategory.getName()).build());
        return bookCategoryResponse;
    }
}
