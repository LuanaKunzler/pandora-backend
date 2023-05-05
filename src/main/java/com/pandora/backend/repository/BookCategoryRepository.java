package com.pandora.backend.repository;

import com.pandora.backend.model.entity.BookCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends CrudRepository<BookCategory, Long> {
    List<BookCategory> findAllByOrderByName();
}
