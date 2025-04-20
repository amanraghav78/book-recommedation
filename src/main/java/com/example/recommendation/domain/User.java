package com.example.recommendation.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name= "bx_users")
@Data
public class User {

    @Id
    private Integer userId;
    private String location;
    private Integer age;
}
