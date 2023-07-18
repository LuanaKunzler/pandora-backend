package com.pandora.backend.model.response.book;

import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.entity.BookCategory;
import lombok.Data;
import java.util.Date;

@Data
public class BookResponse {

    private Long id;
    private String title;
    private String longDesc;
    private BookCategory bookCategory;
    private BookAuthor bookAuthor;
    private String isbn;
    private String bookEdition;
    private Integer numberOfPages;
    private String language;
    private String imageUrl;
    private String bookUrl;
    private Float unitPrice;
    private Float cargoPrice;
    private Integer unitsInStock;
    private Integer sellCount;
    private Date dateCreated;
    private Date lastUpdated;
    private Boolean isActive;
}
