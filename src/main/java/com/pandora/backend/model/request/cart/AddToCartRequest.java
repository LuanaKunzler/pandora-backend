package com.pandora.backend.model.request.cart;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AddToCartRequest {

    @NotNull
    @Min(1)
    private Long bookId;

    @NotNull
    @Min(1)
    private Integer amount;
}
