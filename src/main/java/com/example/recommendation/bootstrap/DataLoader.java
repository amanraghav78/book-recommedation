package com.example.recommendation.bootstrap;

import com.example.recommendation.service.BookService;
import com.example.recommendation.service.RatingService;
import com.example.recommendation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final BookService bookService;
    private final RatingService ratingService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading data...");
        userService.loadUsers();
        bookService.loadBooks();
        ratingService.loadRatings();
        System.out.println("Data loading complete.");
    }
}
