package com.pandora.backend.model.response.author;

import com.pandora.backend.model.dto.AuthorDTO;
import lombok.Data;

@Data
public class BookAuthorResponse {
    private AuthorDTO author;
}
