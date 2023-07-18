package com.pandora.backend.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"bookCategory"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "long_desc")
    @Type(type = "text")
    private String longDesc;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BookCategory bookCategory;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private BookAuthor bookAuthor;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "book_edition")
    private String bookEdition;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "language")
    private String language;

    @Lob
    @Column(name = "image_url")
    private byte[] imageUrl;

    @Column(name = "book_url")
    private String bookUrl;

    @Column(name = "unit_price")
    private Float unitPrice;

    @Column(name = "cargo_price")
    private Float cargoPrice;

    @Column(name = "units_in_stock")
    private Integer unitsInStock;

    @Column(name = "sell_count")
    private Integer sellCount;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "date_created")
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "is_active")
    @Type(type = "boolean")
    private Boolean isActive;
}
