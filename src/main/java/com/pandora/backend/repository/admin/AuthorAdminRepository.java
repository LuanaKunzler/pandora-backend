package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.BookAuthor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorAdminRepository extends PagingAndSortingRepository<BookAuthor, Long> {
    Boolean existsByName(String name);
}