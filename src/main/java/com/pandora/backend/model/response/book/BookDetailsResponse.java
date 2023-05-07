package com.pandora.backend.model.response.book;

import com.pandora.backend.model.dto.AuthorDTO;
import com.pandora.backend.model.dto.CategoryDTO;
import lombok.Data;

@Data
public class BookDetailsResponse {

    private Long id;
    private String title;
    private String longDesc;
    private CategoryDTO bookCategory;
    private AuthorDTO author;
    private Integer isbn;
    private String bookEdition;
    private Integer numberOfPages;
    private String language;
    private String imageUrl;
    private String bookUrl;
    private Float unitPrice;
    private Float cargoPrice;
    private Integer unitsInStock;

}
