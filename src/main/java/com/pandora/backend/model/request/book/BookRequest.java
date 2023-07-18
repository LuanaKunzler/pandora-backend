package com.pandora.backend.model.request.book;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.BookCategory;
import lombok.Data;

@Data
public class BookRequest {

    private String title;
    private String longDesc;
    private BookCategory bookCategory;
    private BookAuthor bookAuthor;
    private String isbn;
    private String bookEdition;
    private Integer numberOfPages;
    private String language;
    private String bookUrl;
    private Float unitPrice;
    private Integer unitsInStock;
    private Boolean isActive;
}
