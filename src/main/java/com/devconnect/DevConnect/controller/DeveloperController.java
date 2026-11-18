package com.devconnect.DevConnect.controller;

import com.devconnect.DevConnect.model.Developer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {
    private final Map<Long, Developer> developerStore = new ConcurrentHashMap<>();
    private final Map<String, Long> developerIdByEmail = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/search")
    public List<Developer> getDevelopersBySkillAndLocation(
            @RequestParam String skill,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "10") int count) {
        if (count < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "count must be zero or greater");
        }

        return developerStore.values().stream()
                .filter(developer -> developer.getSkills().stream().anyMatch(value -> matches(value, skill)))
                .filter(developer -> city == null || matches(developer.getCity(), city))
                .limit(count)
                .toList();
    }

    @GetMapping("/{skill1}/{skill2}")
    public List<Developer> getDevelopersBySkills(
            @PathVariable String skill1,
            @PathVariable String skill2) {
        return developerStore.values().stream()
                .filter(developer -> developer.getSkills().stream().anyMatch(value -> matches(value, skill1)))
                .filter(developer -> developer.getSkills().stream().anyMatch(value -> matches(value, skill2)))
                .toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable Long id) {
        Developer developer = developerStore.get(id);
        if (developer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Developer not found");
        }
        return developer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Developer createDeveloper(@RequestBody Developer developer) {
        if (developer.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Do not provide an id when creating a developer");
        }
        String email = developer.getEmail();
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        developer.setEmail(email.trim());
        long id = idCounter.getAndIncrement();
        Long existingId = developerIdByEmail.putIfAbsent(normalizedEmail, id);
        if (existingId != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A developer with this email already exists");
        }

        developer.setId(id);
        developerStore.put(id, developer);
        return developer;
    }

    @PutMapping("/{id}")
    public synchronized Developer updateDeveloper(
            @PathVariable Long id,
            @RequestBody Developer developer) {
        Developer existingDeveloper = developerStore.get(id);
        if (existingDeveloper == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Developer not found");
        }
        if (developer.getId() != null && !developer.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body id must match the path id");
        }

        String email = developer.getEmail();
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String oldNormalizedEmail = existingDeveloper.getEmail().trim().toLowerCase(Locale.ROOT);
        Long existingOwner = developerIdByEmail.get(normalizedEmail);
        if (existingOwner != null && !existingOwner.equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A developer with this email already exists");
        }

        developer.setId(id);
        developer.setEmail(email.trim());
        if (!normalizedEmail.equals(oldNormalizedEmail)) {
            developerIdByEmail.put(normalizedEmail, id);
            developerIdByEmail.remove(oldNormalizedEmail, id);
        }
        developerStore.put(id, developer);
        return developer;
    }

    private boolean matches(String value, String query) {
        return value != null && query != null && value.equalsIgnoreCase(query);
    }
}
