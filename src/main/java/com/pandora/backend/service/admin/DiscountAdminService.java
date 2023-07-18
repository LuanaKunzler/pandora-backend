package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.request.admin.discount.DiscountCreationRequest;
import com.pandora.backend.model.request.admin.discount.DiscountUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountAdminService {

    Discount newDiscount(DiscountCreationRequest discountCreationRequest);

    Page<Discount> getDiscounts(Pageable pageable, String sortDirection, String sortField);

    Discount getDiscountById(Long id);

    Discount updateDiscount(Long id, DiscountUpdateRequest discountUpdate);

    void deleteDiscount(Long id);
}
