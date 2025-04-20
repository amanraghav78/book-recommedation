package com.example.recommendation.service;

import com.example.recommendation.domain.Book;
import com.example.recommendation.repository.BookRepository;
import jakarta.transaction.Transactional;
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
public class BookService {

    private final BookRepository bookRepository;
    private final ResourceLoader resourceLoader;

    public BookService(BookRepository bookRepository, ResourceLoader resourceLoader) {
        this.bookRepository = bookRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void loadBooks() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:data/BX-Books.csv");
        List<Book> books = Files.lines(Paths.get(resource.getURI()), StandardCharsets.ISO_8859_1)
                .skip(1) // Skip header row
                .map(line -> line.split(";"))
                .map(parts -> {
                    Book book = new Book();
                    book.setIsbn(parts[0].replaceAll("\"", ""));
                    book.setBookTitle(parts[1].replaceAll("\"", ""));
                    book.setBookAuthor(parts[2].replaceAll("\"", ""));
                    if (parts.length > 3) {
                        String yearStr = parts[3].replaceAll("\"", "");
                        try {
                            book.setYearOfPublication(Integer.parseInt(yearStr));
                        } catch (NumberFormatException e) {
                            System.err.println("Could not parse year: " + yearStr + " for ISBN: " + book.getIsbn());
                            book.setYearOfPublication(null); // Set to null or a default value
                        }
                    }
                    if (parts.length > 4) {
                        book.setPublisher(parts[4].replaceAll("\"", ""));
                    }
                    if (parts.length > 5) {
                        book.setImageUrlS(parts[5].replaceAll("\"", ""));
                    }
                    if (parts.length > 6) {
                        book.setImageUrlM(parts[6].replaceAll("\"", ""));
                    }
                    if (parts.length > 7) {
                        book.setImageUrlL(parts[7].replaceAll("\"", ""));
                    }
                    return book;
                })
                .collect(Collectors.toList());
        bookRepository.saveAll(books);
    }
}