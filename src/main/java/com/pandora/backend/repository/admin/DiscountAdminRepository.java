package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.Discount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DiscountAdminRepository extends PagingAndSortingRepository<Discount, Long> {
    Boolean existsByCode(String code);
}
