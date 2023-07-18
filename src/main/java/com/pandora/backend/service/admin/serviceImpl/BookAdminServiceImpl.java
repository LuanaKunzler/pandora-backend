package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.converter.book.BookResponseConverter;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.entity.Book;
import com.pandora.backend.model.request.book.BookRequest;
import com.pandora.backend.model.response.book.BookResponse;
import com.pandora.backend.repository.admin.BookAdminRepository;
import com.pandora.backend.service.admin.BookAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookAdminServiceImpl implements BookAdminService {

    private final BookAdminRepository repository;
    private final BookResponseConverter converter;

    public BookAdminServiceImpl(BookAdminRepository repository,
                                BookResponseConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public Page<Book> getBooks(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreated");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAll(pageable);
    }

    @Override
    public Book newBook(MultipartFile imageUrl, BookRequest bookRequest) throws IOException {

        if (Boolean.TRUE.equals(repository.existsByIsbn(bookRequest.getIsbn()))) {
            throw new InvalidArgumentException("Já existe um livro com esse ISBN");
        }

        byte[] imageData = imageUrl.getBytes();

        Book newBook = new Book();
        newBook.setTitle(bookRequest.getTitle());
        newBook.setLongDesc(bookRequest.getLongDesc());
        newBook.setBookCategory(bookRequest.getBookCategory());
        newBook.setBookAuthor(bookRequest.getBookAuthor());
        newBook.setIsbn(bookRequest.getIsbn());
        newBook.setBookEdition(bookRequest.getBookEdition());
        newBook.setNumberOfPages(bookRequest.getNumberOfPages());
        newBook.setLanguage(bookRequest.getLanguage());
        newBook.setImageUrl(imageData);
        newBook.setBookUrl(bookRequest.getBookUrl());
        newBook.setUnitPrice(bookRequest.getUnitPrice());
        newBook.setCargoPrice(12.99f);
        newBook.setUnitsInStock(bookRequest.getUnitsInStock());
        newBook.setIsActive(bookRequest.getIsActive());

        return repository.save(newBook);
    }

    @Override
    public Book updateBook(Long id, MultipartFile imageUrl, BookRequest bookRequest) throws IOException {
        Book existingBook = repository.findById(id)
                .orElseThrow(() -> new InvalidArgumentException("Livro não encontrado"));

        if (imageUrl != null && !imageUrl.isEmpty()) {
            existingBook.setImageUrl(imageUrl.getBytes());
        }

        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setLongDesc(bookRequest.getLongDesc());
        existingBook.setBookCategory(bookRequest.getBookCategory());
        existingBook.setBookAuthor(bookRequest.getBookAuthor());
        existingBook.setIsbn(bookRequest.getIsbn());
        existingBook.setBookEdition(bookRequest.getBookEdition());
        existingBook.setNumberOfPages(bookRequest.getNumberOfPages());
        existingBook.setLanguage(bookRequest.getLanguage());
        existingBook.setBookUrl(bookRequest.getBookUrl());
        existingBook.setUnitPrice(bookRequest.getUnitPrice());
        existingBook.setUnitsInStock(bookRequest.getUnitsInStock());
        existingBook.setIsActive(bookRequest.getIsActive());
        existingBook.setImageUrl(imageUrl.getBytes());

        return repository.save(existingBook);
    }


    @Override
    public BookResponse getBookById(Long id) {
        Optional<Book> bookOptional = repository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Book not found with the id %d", id));
        }
        Book book = bookOptional.get();
        return converter.apply(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        repository.deleteById(id);
    }
}
