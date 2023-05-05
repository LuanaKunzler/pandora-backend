package com.pandora.backend.repository;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.entity.BookCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByBookUrl(String bookUrl);

    List<Book> findAllByBookCategory(Pageable pageable, BookCategory bookCategory);

    List<Book> findTop8ByOrderByDateCreatedDesc();

    List<Book> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Book> findTop8ByBookCategoryAndIdIsNot(BookCategory bookCategory, Long id);

    List<Book> findAllByBookCategoryIsNot(BookCategory bookCategory, Pageable pageable);

    List<Book> findTop8ByOrderBySellCountDesc();

}
