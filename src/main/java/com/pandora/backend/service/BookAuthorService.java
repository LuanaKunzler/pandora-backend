package com.pandora.backend.service;

import com.pandora.backend.model.response.author.BookAuthorResponse;

import java.util.List;

public interface BookAuthorService {
    List<BookAuthorResponse> findAll();
}
