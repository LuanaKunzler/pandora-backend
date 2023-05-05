package com.pandora.backend.service;

import com.pandora.backend.model.response.category.BookCategoryResponse;

import java.util.List;

public interface BookCategoryService {
    List<BookCategoryResponse> findAllByOrderByName();
}
