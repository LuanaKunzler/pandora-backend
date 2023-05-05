package com.pandora.backend.converter.book;

import com.pandora.backend.model.dto.AuthorDTO;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.response.book.BookResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookResponseConverter implements Function<Book, BookResponse> {

    @Override
    public BookResponse apply(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle(book.getTitle());
        bookResponse.setImageUrl(book.getImageUrl());
        bookResponse.setAuthor(AuthorDTO.builder().name(book.getBookAuthor().getName()).build());
        bookResponse.setBookUrl(book.getBookUrl());

        return bookResponse;
    }

}
