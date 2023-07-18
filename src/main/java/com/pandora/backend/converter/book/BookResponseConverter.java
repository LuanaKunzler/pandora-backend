package com.pandora.backend.converter.book;

import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.response.book.BookResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.function.Function;

@Component
public class BookResponseConverter implements Function<Book, BookResponse> {

    @Override
    public BookResponse apply(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setLongDesc(book.getLongDesc());
        bookResponse.setBookCategory(book.getBookCategory());
        bookResponse.setBookAuthor(book.getBookAuthor());
        bookResponse.setIsbn(book.getIsbn());
        bookResponse.setBookEdition(book.getBookEdition());
        bookResponse.setNumberOfPages(book.getNumberOfPages());
        bookResponse.setLanguage(book.getLanguage());
        String imageUrl = convertBlobToUrl(book.getImageUrl());
        bookResponse.setImageUrl(imageUrl);
        bookResponse.setBookUrl(book.getBookUrl());
        bookResponse.setUnitPrice(book.getUnitPrice());
        bookResponse.setCargoPrice(book.getCargoPrice());
        bookResponse.setUnitsInStock(book.getUnitsInStock());
        bookResponse.setSellCount(book.getSellCount());
        bookResponse.setDateCreated(book.getDateCreated());
        bookResponse.setLastUpdated(book.getLastUpdated());
        bookResponse.setIsActive(book.getIsActive());
        return bookResponse;
    }

    public String convertBlobToUrl(byte[] blob) {
        String base64Image = Base64.getEncoder().encodeToString(blob);
        String imageUrl = "data:image/jpeg;base64," + base64Image;
        return imageUrl;
    }

}
