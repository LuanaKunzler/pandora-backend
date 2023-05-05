package com.pandora.backend.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book_author")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
