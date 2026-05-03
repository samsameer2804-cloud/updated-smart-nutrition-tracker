package com.tracker.nutrition.service;

import com.tracker.nutrition.dto.Recommendation;
import com.tracker.nutrition.model.FoodEntry;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.FoodEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final FoodEntryRepository repository;

    public RecommendationService(FoodEntryRepository repository) {
        this.repository = repository;
    }

    public List<Recommendation> getRecommendations(User user) {
        List<FoodEntry> allEntries = repository.findByUser(user);
        List<FoodEntry> todayEntries = repository.findByUserAndDateLogged(user, LocalDate.now());

        if (allEntries.isEmpty()) {
            return Collections.emptyList();
        }

        // Calculate today's totals
        double todayProtein = todayEntries.stream().mapToDouble(FoodEntry::getProtein).sum();
        int todayCalories = todayEntries.stream().mapToInt(FoodEntry::getCalories).sum();

        // Get unique historical foods (use latest entry of the same name)
        Map<String, FoodEntry> uniqueFoods = new HashMap<>();
        for (FoodEntry entry : allEntries) {
            uniqueFoods.put(entry.getName().toLowerCase(), entry);
        }

        List<FoodEntry> historicalFoods = new ArrayList<>(uniqueFoods.values());
        List<Recommendation> recommendations = new ArrayList<>();

        // Rule 1: High Protein (If lacking protein today)
        if (todayProtein < 50) {
            historicalFoods.stream()
                .max(Comparator.comparing(FoodEntry::getProtein))
                .ifPresent(food -> {
                    if (food.getProtein() >= 10) {
                        recommendations.add(createRec(food, "Boost your protein intake!"));
                    }
                });
        }

        // Rule 2: Low Calorie (If nearing calorie limit)
        if (todayCalories > 1500) {
            historicalFoods.stream()
                .min(Comparator.comparing(FoodEntry::getCalories))
                .ifPresent(food -> {
                    if (food.getCalories() <= 300) {
                        recommendations.add(createRec(food, "A lighter choice to stay under goal."));
                    }
                });
        }

        // Rule 3: General Healthy (High protein/low fat ratio)
        historicalFoods.stream()
            .filter(f -> f.getProtein() > f.getFat() && f.getCalories() > 50)
            .max(Comparator.comparing(f -> f.getProtein() / (f.getFat() + 0.1))) // avoid division by zero
            .ifPresent(food -> recommendations.add(createRec(food, "A balanced, healthy choice from your history.")));

        // Fallback: If no recommendations were generated, just recommend their most logged food
        if (recommendations.isEmpty() && !historicalFoods.isEmpty()) {
            recommendations.add(createRec(historicalFoods.get(0), "A favorite from your history."));
        }

        // Return distinct list
        return recommendations.stream().distinct().limit(2).collect(Collectors.toList());
    }

    private Recommendation createRec(FoodEntry food, String reason) {
        return new Recommendation(food.getName(), food.getCalories(), food.getProtein(), food.getCarbs(), food.getFat(), reason);
    }
}
