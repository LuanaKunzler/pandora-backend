package com.pandora.backend.model.specs;

import com.pandora.backend.model.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecs {

    public static Specification<Book> withCategory(String category) {
        return (root, query, cb) -> {
            if (category == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("bookCategory").get("name"), category);
        };
    }

    public static Specification<Book> withAuthor(String author) {
        return (root, query, cb) -> {
            if (author == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("bookAuthor").get("name"), author);
        };
    }

    public static Specification<Book> maxPrice(Float unitPrice) {
        return (root, query, cb) -> {
            if (unitPrice == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.lessThan(root.get("unitPrice"), unitPrice);
        };
    }

    public static Specification<Book> minPrice(Float unitPrice) {
        return (root, query, cb) -> {
            if (unitPrice == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.greaterThanOrEqualTo(root.get("unitPrice"), unitPrice);
        };
    }

}
