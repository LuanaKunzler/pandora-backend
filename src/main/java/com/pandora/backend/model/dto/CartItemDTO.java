package com.pandora.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;

    private String bookUrl;

    private String imageUrl;

    private String title;

    private Float price;

    private Integer amount;

    private Integer unitsInStock;
}
