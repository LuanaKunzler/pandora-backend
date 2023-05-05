package com.pandora.backend.service.cache;

import com.pandora.backend.model.entity.BookCategory;

import java.util.List;

public interface BookCategoryCacheService {
    List<BookCategory> findAllByOrderByName();
}
