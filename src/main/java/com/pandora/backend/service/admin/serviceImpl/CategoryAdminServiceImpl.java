package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.model.request.admin.category.CategoryCreationRequest;
import com.pandora.backend.model.request.admin.category.CategoryUpdateRequest;
import com.pandora.backend.repository.admin.CategoryAdminRepository;
import com.pandora.backend.service.admin.CategoryAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryAdminRepository repository;

    public CategoryAdminServiceImpl(CategoryAdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookCategory newCategory(CategoryCreationRequest categoryCreationRequest) {
        if (Boolean.TRUE.equals(repository.existsByName(categoryCreationRequest.getName()))) {
            throw new InvalidArgumentException("Já existe uma categoria com este nome");
        }

        BookCategory category = new BookCategory();
        category.setName(categoryCreationRequest.getName());

        return repository.save(category);
    }

    @Override
    public Page<BookCategory> getCategories(Pageable pageable, String sortDirection, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAll(pageable);
    }

    @Override
    public BookCategory getCategoryById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public BookCategory updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        BookCategory existingCategory = repository.findById(id).orElse(null);
        if (existingCategory == null) {
            throw new IllegalArgumentException("Category not found");
        }

        existingCategory.setName(categoryUpdateRequest.getName());

        return repository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        repository.deleteById(id);
    }
}
