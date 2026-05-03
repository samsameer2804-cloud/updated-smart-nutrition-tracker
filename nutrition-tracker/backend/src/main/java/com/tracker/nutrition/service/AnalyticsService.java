package com.tracker.nutrition.service;

import com.tracker.nutrition.dto.AnalyticsReport;
import com.tracker.nutrition.dto.DailyMacroSummary;
import com.tracker.nutrition.model.FoodEntry;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.FoodEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final FoodEntryRepository repository;

    public AnalyticsService(FoodEntryRepository repository) {
        this.repository = repository;
    }

    public AnalyticsReport getWeeklyAnalytics(User user) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // Last 7 days including today

        List<FoodEntry> weeklyEntries = repository.findByUserAndDateLoggedBetweenOrderByDateLoggedAsc(user, startDate, endDate);

        // Group by date
        Map<LocalDate, List<FoodEntry>> entriesByDate = weeklyEntries.stream()
                .collect(Collectors.groupingBy(FoodEntry::getDateLogged));

        List<DailyMacroSummary> dailyTrends = new ArrayList<>();
        int totalWeeklyCalories = 0;

        // Calculate days with data to prevent dividing by zero for averages
        long daysWithData = entriesByDate.keySet().stream()
                .filter(date -> !entriesByDate.get(date).isEmpty())
                .count();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        // Ensure all 7 days are represented, even if 0
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            List<FoodEntry> dayEntries = entriesByDate.getOrDefault(date, Collections.emptyList());
            
            int dayCalories = dayEntries.stream().mapToInt(FoodEntry::getCalories).sum();
            double dayProtein = dayEntries.stream().mapToDouble(FoodEntry::getProtein).sum();
            double dayCarbs = dayEntries.stream().mapToDouble(FoodEntry::getCarbs).sum();
            double dayFat = dayEntries.stream().mapToDouble(FoodEntry::getFat).sum();

            totalWeeklyCalories += dayCalories;
            
            dailyTrends.add(new DailyMacroSummary(
                    date.format(formatter),
                    dayCalories,
                    Math.round(dayProtein * 10.0) / 10.0,
                    Math.round(dayCarbs * 10.0) / 10.0,
                    Math.round(dayFat * 10.0) / 10.0
            ));
        }

        int averageDailyCalories = daysWithData > 0 ? (int) (totalWeeklyCalories / daysWithData) : 0;

        Optional<FoodEntry> highestCalorieFoodOpt = repository.findFirstByUserOrderByCaloriesDesc(user);
        String highestFoodName = "None";
        int highestFoodCals = 0;
        if (highestCalorieFoodOpt.isPresent()) {
            highestFoodName = highestCalorieFoodOpt.get().getName();
            highestFoodCals = highestCalorieFoodOpt.get().getCalories();
        }

        return new AnalyticsReport(
                totalWeeklyCalories,
                averageDailyCalories,
                highestFoodName,
                highestFoodCals,
                dailyTrends
        );
    }
}
