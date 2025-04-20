package com.example.recommendation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table( name = "bx_books")
@Data
public class Book {

    @Id
    private String isbn;
    private String bookTitle;
    private String bookAuthor;
    private Integer yearOfPublication;
    private String publisher;
    private String imageUrlS;
    private String imageUrlM;
    private String imageUrlL;
}
