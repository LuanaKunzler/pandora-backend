package com.pandora.backend.service.serviceImpl;

import com.pandora.backend.converter.book.BookDetailsResponseConverter;
import com.pandora.backend.converter.book.BookResponseConverter;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.response.book.BookDetailsResponse;
import com.pandora.backend.model.response.book.BookResponse;
import com.pandora.backend.model.specs.BookSpecs;
import com.pandora.backend.repository.BookRepository;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.service.BookService;
import com.pandora.backend.service.cache.BookCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookCacheService bookCacheService;
    private final BookRepository bookRepository;
    private final BookResponseConverter bookResponseConverter;
    private final BookDetailsResponseConverter bookDetailsResponseConverter;

    @Autowired
    public BookServiceImpl(BookCacheService bookCacheService,
                           BookRepository bookRepository,
                           BookResponseConverter bookResponseConverter,
                           BookDetailsResponseConverter bookDetailsResponseConverter) {
        this.bookCacheService = bookCacheService;
        this.bookRepository = bookRepository;
        this.bookResponseConverter = bookResponseConverter;
        this.bookDetailsResponseConverter = bookDetailsResponseConverter;
    }


    @Override
    public BookDetailsResponse findByUrl(String url) {
        Book book = bookCacheService.findByUrl(url);
        if (Objects.isNull(book)) {
            throw new ResourceNotFoundException(String.format("Book not found with the url %s", url));
        }
        return bookDetailsResponseConverter.apply(book);
    }

    @Override
    public List<BookResponse> getAll(Integer page, Integer size, String sort, String category, Float minPrice,
                                     Float maxPrice, String author) {
        PageRequest pageRequest;
        if (Objects.nonNull(sort) && !sort.isBlank()) {
            Sort sortRequest = getSort(sort);
            if (Objects.isNull(sortRequest)) {
                throw new InvalidArgumentException("Invalid sort parameter");
            }
            pageRequest = PageRequest.of(page, size, sortRequest);
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        Specification<Book> combinations =
                Objects.requireNonNull(Specification.where(BookSpecs.withAuthor(author)))
                        .and(BookSpecs.withCategory(category))
                        .and(BookSpecs.minPrice(minPrice))
                        .and(BookSpecs.maxPrice(maxPrice));

        return bookRepository.findAll(combinations, pageRequest)
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAllCount(String category, Float minPrice, Float maxPrice, String author) {
        Specification<Book> combinations =
                Objects.requireNonNull(Specification.where(BookSpecs.withAuthor(author)))
                        .and(BookSpecs.withCategory(category))
                        .and(BookSpecs.minPrice(minPrice))
                        .and(BookSpecs.maxPrice(maxPrice));

        return bookRepository.count(combinations);
    }

    @Override
    public Book findBookById(Long id) {
        Book book = bookCacheService.findById(id);
        if (Objects.isNull(book)) {
            throw new ResourceNotFoundException(String.format("Could not find any book with the id %d", id));
        }
        return book;
    }

    @Override
    public List<BookResponse> getRelatedBooks(String url) {
        Book book = bookCacheService.findByUrl(url);
        if (Objects.isNull(book)) {
            throw new ResourceNotFoundException("Related books not found");
        }
        List<Book> books = bookCacheService.getRelatedBooks(book.getBookCategory(), book.getId());
        return books
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getNewlyAddedBooks() {
        List<Book> books = bookCacheService.findTop8ByOrderByDateCreatedDesc();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("Newly added books not found");
        }
        return books
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getMostSelling() {
        List<Book> books = bookCacheService.findTop8ByOrderBySellCountDesc();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("Most selling books not found");
        }

        return books
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getInterested() {
        List<Book> books = bookCacheService.findTop8ByOrderByDateCreatedDesc();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("Interested books not found");
        }
        return books
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> searchBookDisplay(String keyword, Integer page, Integer size) {
        if (Objects.isNull(page) || Objects.isNull(size)) {
            throw new InvalidArgumentException("Page and size are required");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Book> books = bookRepository.findAllByTitleContainingIgnoreCase(keyword, pageRequest);
        return books
                .stream()
                .map(bookResponseConverter)
                .collect(Collectors.toList());
    }

    private Sort getSort(String sort) {
        switch (sort) {
            case "lowest":
                return Sort.by(Sort.Direction.ASC, "unitPrice");
            case "highest":
                return Sort.by(Sort.Direction.DESC, "unitPrice");
            default:
                return null;
        }
    }
}
