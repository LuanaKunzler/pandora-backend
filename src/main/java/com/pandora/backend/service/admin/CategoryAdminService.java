package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.BookCategory;
import com.pandora.backend.model.request.admin.category.CategoryCreationRequest;
import com.pandora.backend.model.request.admin.category.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryAdminService {
    BookCategory newCategory(CategoryCreationRequest categoryCreationRequest);

    Page<BookCategory> getCategories(Pageable pageable, String sortDirection, String sortField);

    BookCategory getCategoryById(Long id);

    BookCategory updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);

    void deleteCategory(Long id);
}
