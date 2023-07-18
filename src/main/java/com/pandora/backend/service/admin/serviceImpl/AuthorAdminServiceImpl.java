package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.request.admin.author.AuthorCreationRequest;
import com.pandora.backend.model.request.admin.author.AuthorUpdateRequest;
import com.pandora.backend.repository.admin.AuthorAdminRepository;
import com.pandora.backend.service.admin.AuthorAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorAdminServiceImpl implements AuthorAdminService {

    private final AuthorAdminRepository repository;

    public AuthorAdminServiceImpl(AuthorAdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookAuthor newAuthor(AuthorCreationRequest authorCreationRequest) {
        if (Boolean.TRUE.equals(repository.existsByName(authorCreationRequest.getName()))) {
            throw new InvalidArgumentException("Já existe um autor com este nome");
        }

        BookAuthor author = new BookAuthor();
        author.setName(authorCreationRequest.getName());

        return repository.save(author);
    }

    @Override
    public Page<BookAuthor> getAuthors(Pageable pageable, String sortDirection, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAll(pageable);
    }

    @Override
    public BookAuthor getAuthorById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public BookAuthor updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest) {
        BookAuthor existingAuthor = repository.findById(id).orElse(null);
        if (existingAuthor == null) {
            throw new IllegalArgumentException("Author not found");
        }

        existingAuthor.setName(authorUpdateRequest.getName());

        return repository.save(existingAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        repository.deleteById(id);
    }
}
