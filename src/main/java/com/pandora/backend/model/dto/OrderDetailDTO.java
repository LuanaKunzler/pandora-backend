package com.pandora.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private String bookUrl;

    private String imageUrl;

    private String title;

    private Float price;

    private Float cargoPrice;

    private Integer amount;

    private CategoryDTO category;
}
