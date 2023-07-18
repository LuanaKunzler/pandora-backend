package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.request.admin.author.AuthorCreationRequest;
import com.pandora.backend.model.request.admin.author.AuthorUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorAdminService {

    BookAuthor newAuthor(AuthorCreationRequest authorCreationRequest);

    Page<BookAuthor> getAuthors(Pageable pageable, String sortDirection, String sortField);

    BookAuthor getAuthorById(Long id);

    BookAuthor updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest);

    void deleteAuthor(Long id);
}
