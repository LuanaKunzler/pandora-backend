package com.pandora.backend.model.response.book;

import com.pandora.backend.model.dto.AuthorDTO;
import lombok.Data;

@Data
public class BookResponse {

    private String title;
    private String imageUrl;
    private AuthorDTO author;
    private String bookUrl;

}
