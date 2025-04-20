package com.example.recommendation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "bx_book_ratings")
@IdClass(Rating.RatingId.class)
public class Rating {

    @Id
    private Integer userId;
    @Id
    private String isbn;
    private Integer bookRating;

    public static class RatingId implements Serializable{
        private Integer userId;
        private String isbn;
    }
}
