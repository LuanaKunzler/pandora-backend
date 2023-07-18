package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.request.book.BookRequest;
import com.pandora.backend.model.response.book.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookAdminService {

    Page<Book> getBooks(Pageable pageable);

    Book newBook(MultipartFile imageUrl, BookRequest bookRequest) throws IOException;

    Book updateBook(Long id, MultipartFile imageUrl, BookRequest bookRequest) throws IOException;

    BookResponse getBookById(Long id);

    void deleteBook(Long id);
}
