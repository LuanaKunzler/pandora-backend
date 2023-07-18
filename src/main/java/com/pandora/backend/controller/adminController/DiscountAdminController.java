package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.Discount;
import com.pandora.backend.model.request.admin.discount.DiscountCreationRequest;
import com.pandora.backend.model.request.admin.discount.DiscountUpdateRequest;
import com.pandora.backend.service.admin.DiscountAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class DiscountAdminController extends ApiAdminController {
    private final DiscountAdminService service;

    public DiscountAdminController(DiscountAdminService service) {
        this.service = service;
    }

    @PostMapping(value = "/discount")
    public ResponseEntity<HttpStatus> newDiscount(@RequestBody @Valid DiscountCreationRequest discountCreationRequest) {
        try {
            service.newDiscount(discountCreationRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/discounts")
    public ResponseEntity<Page<Discount>> getDiscounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "discountPercent") String sortField) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Discount> discountsPage = service.getDiscounts(pageable, sortDirection, sortField);
            return new ResponseEntity<>(discountsPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/discount/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        try {
            var discount = service.getDiscountById(id);
            return new ResponseEntity<>(discount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/discount/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id, @RequestBody DiscountUpdateRequest updateRequest) {
        Discount updatedDiscount = service.updateDiscount(id, updateRequest);
        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/discount/{id}")
    public ResponseEntity<HttpStatus> deleteDiscount(@PathVariable Long id) {
        try {
            service.deleteDiscount(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
