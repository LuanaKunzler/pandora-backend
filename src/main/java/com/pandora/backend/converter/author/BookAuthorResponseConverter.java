package com.pandora.backend.converter.author;

import com.pandora.backend.model.dto.AuthorDTO;
import com.pandora.backend.model.entity.BookAuthor;
import com.pandora.backend.model.response.author.BookAuthorResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookAuthorResponseConverter implements Function<BookAuthor, BookAuthorResponse> {

    @Override
    public BookAuthorResponse apply(BookAuthor bookAuthor) {
        BookAuthorResponse bookAuthorResponse = new BookAuthorResponse();
        bookAuthorResponse.setName(bookAuthor.getName());
        return bookAuthorResponse;
    }

}
