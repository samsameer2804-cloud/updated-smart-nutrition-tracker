package com.tracker.nutrition.controller;

import com.tracker.nutrition.model.FoodEntry;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.UserRepository;
import com.tracker.nutrition.service.NutritionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodEntryController {

    private static final Logger log = LoggerFactory.getLogger(FoodEntryController.class);
    private final NutritionService service;
    private final UserRepository userRepository;

    @Autowired
    public FoodEntryController(NutritionService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName()).orElseThrow();
    }

    @GetMapping
    public List<FoodEntry> getTodayEntries(Authentication authentication) {
        log.info("Received request to get today's food entries");
        return service.getEntriesForToday(getCurrentUser(authentication));
    }

    @GetMapping("/all")
    public List<FoodEntry> getAllEntries(Authentication authentication) {
        log.info("Received request to get all food entries");
        return service.getAllEntries(getCurrentUser(authentication));
    }

    @PostMapping
    public FoodEntry addEntry(@Valid @RequestBody FoodEntry entry, Authentication authentication) {
        log.info("Received request to add a new food entry: {}", entry.getName());
        return service.addEntry(entry, getCurrentUser(authentication));
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable String id, Authentication authentication) {
        log.info("Received request to delete food entry with id: {}", id);
        service.deleteEntry(id, getCurrentUser(authentication));
    }
}
