package com.tracker.nutrition.repository;

import com.tracker.nutrition.model.FoodEntry;
import com.tracker.nutrition.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodEntryRepository extends MongoRepository<FoodEntry, String> {
    List<FoodEntry> findByUserAndDateLogged(User user, LocalDate dateLogged);
    List<FoodEntry> findByUserAndDateLoggedBetweenOrderByDateLoggedAsc(User user, LocalDate startDate, LocalDate endDate);
    List<FoodEntry> findByUser(User user);
    Optional<FoodEntry> findByIdAndUser(String id, User user);
    boolean existsByIdAndUser(String id, User user);
    Optional<FoodEntry> findFirstByUserOrderByCaloriesDesc(User user);
}
