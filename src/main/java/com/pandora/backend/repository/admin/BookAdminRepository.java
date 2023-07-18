package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAdminRepository extends PagingAndSortingRepository<Book, Long> {
    Boolean existsByIsbn(String isbn);
}
