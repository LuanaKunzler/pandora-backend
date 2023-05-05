package com.pandora.backend.converter.book;

import com.pandora.backend.model.dto.AuthorDTO;
import com.pandora.backend.model.dto.CategoryDTO;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.response.book.BookDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookDetailsResponseConverter implements Function<Book, BookDetailsResponse> {
    @Override
    public BookDetailsResponse apply(Book book) {
        BookDetailsResponse bookDetailsResponse = new BookDetailsResponse();
        bookDetailsResponse.setTitle(book.getTitle());
        bookDetailsResponse.setLongDesc(book.getLongDesc());
        bookDetailsResponse.setBookCategory(CategoryDTO.builder().name(book.getBookCategory().getName()).build());
        bookDetailsResponse.setAuthor(AuthorDTO.builder().name(book.getBookAuthor().getName()).build());
        bookDetailsResponse.setIsbn(book.getIsbn());
        bookDetailsResponse.setBookEdition(book.getBookEdition());
        bookDetailsResponse.setNumberOfPages(book.getNumberOfPages());
        bookDetailsResponse.setLanguage(book.getLanguage());
        bookDetailsResponse.setImageUrl(book.getImageUrl());
        bookDetailsResponse.setBookUrl(book.getBookUrl());


        return bookDetailsResponse;
    }
}
