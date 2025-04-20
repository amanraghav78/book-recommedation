package com.example.recommendation.service;

import com.example.recommendation.domain.Rating;
import com.example.recommendation.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ResourceLoader resourceLoader;

    public RatingService(RatingRepository ratingRepository, ResourceLoader resourceLoader) {
        this.ratingRepository = ratingRepository;
        this.resourceLoader = resourceLoader;
    }

    @Value("${data.location}")
    private String dataLocation;

    @Transactional
    public void loadRatings() throws IOException{
        Resource resource = resourceLoader.getResource("classpath:data/BX-Book-Ratings.csv"); // Corrected file name
        List<Rating> ratings = Files.lines(Paths.get(resource.getURI()), StandardCharsets.ISO_8859_1)
                .skip(1) // Skip header row
                .map(line -> line.split(";"))
                .map(parts -> {
                    Rating rating = new Rating();
                    rating.setUserId(Integer.parseInt(parts[0].replaceAll("\"", "")));
                    rating.setIsbn(parts[1].replaceAll("\"", ""));
                    rating.setBookRating(Integer.parseInt(parts[2].replaceAll("\"", "")));
                    return rating;
                })
                .collect(Collectors.toList());
        ratingRepository.saveAll(ratings);
    }
}
