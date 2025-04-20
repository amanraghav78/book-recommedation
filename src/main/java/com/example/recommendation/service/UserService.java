package com.example.recommendation.service;

import com.example.recommendation.domain.User;
import com.example.recommendation.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;

    public UserService(UserRepository userRepository, ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void loadUsers() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:data/BX-Users.csv");
        Set<Integer> seenUserIds = new HashSet<>();
        List<User> usersToSave = Files.lines(Paths.get(resource.getURI()), StandardCharsets.ISO_8859_1)
                .skip(1) // Skip header row
                .map(line -> line.split(";"))
                .map(parts -> {
                    if (parts.length > 0 && !parts[0].replaceAll("\"", "").isEmpty()) {
                        try {
                            int userId = Integer.parseInt(parts[0].replaceAll("\"", ""));
                            if (!seenUserIds.contains(userId)) {
                                seenUserIds.add(userId);
                                User user = new User();
                                user.setUserId(userId);
                                if (parts.length > 1) {
                                    user.setLocation(parts[1].replaceAll("\"", ""));
                                    if (parts.length > 2) {
                                        String ageStr = parts[2].replaceAll("\"", "");
                                        user.setAge(ageStr.equalsIgnoreCase("NULL") ? null : parseAge(ageStr, userId));
                                    }
                                }
                                return user;
                            } else {
                                System.err.println("Duplicate User-ID found: " + userId + " - Skipping.");
                                return null;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Could not parse User-ID: " + parts[0] + " - Skipping user.");
                            return null;
                        }
                    } else {
                        System.err.println("Missing or invalid User-ID - Skipping user.");
                        return null;
                    }
                })
                .filter(user -> user != null)
                .collect(Collectors.toList());
        userRepository.saveAll(usersToSave);
    }

    private Integer parseAge(String ageStr, int userId) {
        if (ageStr.equalsIgnoreCase("NULL")) {
            return null;
        }
        try {
            return Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            System.err.println("Could not parse age: " + ageStr + " for user ID: " + userId);
            return null;
        }
    }
}