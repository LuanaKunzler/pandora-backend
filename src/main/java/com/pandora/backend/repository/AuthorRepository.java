package com.pandora.backend.repository;

import com.pandora.backend.model.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<BookAuthor, Long> {
}
