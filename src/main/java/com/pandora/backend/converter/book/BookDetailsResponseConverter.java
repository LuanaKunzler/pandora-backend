package com.pandora.backend.converter.book;

import com.pandora.backend.model.dto.AuthorDTO;
import com.pandora.backend.model.dto.CategoryDTO;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.response.book.BookDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.function.Function;

@Component
public class BookDetailsResponseConverter implements Function<Book, BookDetailsResponse> {
    @Override
    public BookDetailsResponse apply(Book book) {
        BookDetailsResponse bookDetailsResponse = new BookDetailsResponse();
        bookDetailsResponse.setId(book.getId());
        bookDetailsResponse.setTitle(book.getTitle());
        bookDetailsResponse.setLongDesc(book.getLongDesc());
        bookDetailsResponse.setBookCategory(CategoryDTO.builder().name(book.getBookCategory().getName()).build());
        bookDetailsResponse.setAuthor(AuthorDTO.builder().name(book.getBookAuthor().getName()).build());
        bookDetailsResponse.setIsbn(book.getIsbn());
        bookDetailsResponse.setBookEdition(book.getBookEdition());
        bookDetailsResponse.setNumberOfPages(book.getNumberOfPages());
        bookDetailsResponse.setLanguage(book.getLanguage());
        String imageUrl = convertBlobToUrl(book.getImageUrl());
        bookDetailsResponse.setImageUrl(imageUrl);
        bookDetailsResponse.setBookUrl(book.getBookUrl());
        bookDetailsResponse.setUnitPrice(book.getUnitPrice());
        bookDetailsResponse.setCargoPrice(book.getCargoPrice());
        bookDetailsResponse.setUnitsInStock(book.getUnitsInStock());

        return bookDetailsResponse;
    }

    public String convertBlobToUrl(byte[] blob) {
        String base64Image = Base64.getEncoder().encodeToString(blob);
        String imageUrl = "data:image/jpeg;base64," + base64Image;
        return imageUrl;
    }
}
