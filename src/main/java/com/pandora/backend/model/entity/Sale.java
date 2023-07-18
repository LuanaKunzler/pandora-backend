package com.pandora.backend.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Sale {
    @Id
    private Long bookId;
    private String title;
    private String isbn;
    private Integer quantitySold;
    private Double unitPrice;
    private Double totalPrice;
}
