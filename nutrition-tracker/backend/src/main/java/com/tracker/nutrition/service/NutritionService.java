package com.tracker.nutrition.service;

import com.tracker.nutrition.exception.FoodNotFoundException;
import com.tracker.nutrition.model.FoodEntry;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.FoodEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NutritionService {

    private static final Logger log = LoggerFactory.getLogger(NutritionService.class);
    private final FoodEntryRepository repository;

    @Autowired
    public NutritionService(FoodEntryRepository repository) {
        this.repository = repository;
    }

    public List<FoodEntry> getAllEntries(User user) {
        log.info("Fetching all food entries for user: {}", user.getUsername());
        return repository.findByUser(user);
    }

    public List<FoodEntry> getEntriesForToday(User user) {
        log.info("Fetching food entries for today: {} for user: {}", LocalDate.now(), user.getUsername());
        return repository.findByUserAndDateLogged(user, LocalDate.now());
    }

    public FoodEntry addEntry(FoodEntry entry, User user) {
        if (entry.getDateLogged() == null) {
            entry.setDateLogged(LocalDate.now());
        }
        entry.setUser(user);
        log.info("Adding new food entry: {} for user: {}", entry.getName(), user.getUsername());
        return repository.save(entry);
    }

    public void deleteEntry(String id, User user) {
        if (!repository.existsByIdAndUser(id, user)) {
            log.warn("Attempted to delete non-existent food entry with id: {} for user: {}", id, user.getUsername());
            throw new FoodNotFoundException("Food entry with id " + id + " not found or does not belong to you.");
        }
        log.info("Deleting food entry with id: {} for user: {}", id, user.getUsername());
        FoodEntry entry = repository.findByIdAndUser(id, user).get();
        repository.delete(entry);
    }
}
