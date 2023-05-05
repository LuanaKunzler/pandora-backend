package com.pandora.backend.service.cache;

import com.pandora.backend.model.entity.BookAuthor;

import java.util.List;

public interface BookAuthorCacheService {
    List<BookAuthor> findAll();
}
