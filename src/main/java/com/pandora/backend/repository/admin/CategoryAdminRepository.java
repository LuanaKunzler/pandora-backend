package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.BookCategory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAdminRepository extends PagingAndSortingRepository<BookCategory, Long> {
    Boolean existsByName(String name);
}