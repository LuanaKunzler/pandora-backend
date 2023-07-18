package com.pandora.backend.model.request.admin.discount;

import lombok.Data;

@Data
public class DiscountCreationRequest {
    private String code;
    private Integer discountPercent;
    private Integer status;
}
