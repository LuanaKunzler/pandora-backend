package com.pandora.backend.controller;

import com.pandora.backend.model.request.discount.ApplyDiscountRequest;
import com.pandora.backend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DiscountControllerUser extends ApiUserController {

    private final DiscountService discountService;

    @Autowired
    public DiscountControllerUser(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping(value = "/cart/discount")
    public ResponseEntity<HttpStatus> applyDiscount(@RequestBody @Valid ApplyDiscountRequest applyDiscountRequest) {
        discountService.applyDiscount(applyDiscountRequest.getCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
