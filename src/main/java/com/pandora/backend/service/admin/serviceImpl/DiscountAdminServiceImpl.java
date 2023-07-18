package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.request.admin.discount.DiscountCreationRequest;
import com.pandora.backend.model.request.admin.discount.DiscountUpdateRequest;
import com.pandora.backend.repository.admin.DiscountAdminRepository;
import com.pandora.backend.service.admin.DiscountAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DiscountAdminServiceImpl implements DiscountAdminService {

    private final DiscountAdminRepository discountRepository;

    public DiscountAdminServiceImpl(DiscountAdminRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount newDiscount(DiscountCreationRequest discountCreationRequest) {
        if (Boolean.TRUE.equals(discountRepository.existsByCode(discountCreationRequest.getCode()))) {
            throw new InvalidArgumentException("Já existe um desconto com este código");
        }

        Discount discount = new Discount();
        discount.setCode(discountCreationRequest.getCode());
        discount.setDiscountPercent(discountCreationRequest.getDiscountPercent());
        discount.setStatus(discountCreationRequest.getStatus());

        return discountRepository.save(discount);
    }

    @Override
    public Page<Discount> getDiscounts(Pageable pageable, String sortDirection, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return discountRepository.findAll(pageable);
    }

    @Override
    public Discount getDiscountById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public Discount updateDiscount(Long id, DiscountUpdateRequest discountUpdate) {
        Discount existingDiscount = discountRepository.findById(id).orElse(null);
        if (existingDiscount == null) {
            throw new IllegalArgumentException("Discount not found");
        }

        existingDiscount.setCode(discountUpdate.getCode());
        existingDiscount.setDiscountPercent(discountUpdate.getDiscountPercent());
        existingDiscount.setStatus(discountUpdate.getStatus());

        return discountRepository.save(existingDiscount);
    }

    @Override
    public void deleteDiscount(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        discountRepository.deleteById(id);
    }
}
