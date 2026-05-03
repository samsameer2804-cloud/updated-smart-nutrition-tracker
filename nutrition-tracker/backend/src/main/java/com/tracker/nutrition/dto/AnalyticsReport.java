package com.tracker.nutrition.dto;

import java.util.List;

public class AnalyticsReport {
    private int totalWeeklyCalories;
    private int averageDailyCalories;
    private String highestCalorieFoodName;
    private int highestCalorieFoodAmount;
    private List<DailyMacroSummary> dailyTrends;

    public AnalyticsReport() {}

    public AnalyticsReport(int totalWeeklyCalories, int averageDailyCalories, String highestCalorieFoodName, int highestCalorieFoodAmount, List<DailyMacroSummary> dailyTrends) {
        this.totalWeeklyCalories = totalWeeklyCalories;
        this.averageDailyCalories = averageDailyCalories;
        this.highestCalorieFoodName = highestCalorieFoodName;
        this.highestCalorieFoodAmount = highestCalorieFoodAmount;
        this.dailyTrends = dailyTrends;
    }

    public int getTotalWeeklyCalories() { return totalWeeklyCalories; }
    public void setTotalWeeklyCalories(int totalWeeklyCalories) { this.totalWeeklyCalories = totalWeeklyCalories; }
    public int getAverageDailyCalories() { return averageDailyCalories; }
    public void setAverageDailyCalories(int averageDailyCalories) { this.averageDailyCalories = averageDailyCalories; }
    public String getHighestCalorieFoodName() { return highestCalorieFoodName; }
    public void setHighestCalorieFoodName(String highestCalorieFoodName) { this.highestCalorieFoodName = highestCalorieFoodName; }
    public int getHighestCalorieFoodAmount() { return highestCalorieFoodAmount; }
    public void setHighestCalorieFoodAmount(int highestCalorieFoodAmount) { this.highestCalorieFoodAmount = highestCalorieFoodAmount; }
    public List<DailyMacroSummary> getDailyTrends() { return dailyTrends; }
    public void setDailyTrends(List<DailyMacroSummary> dailyTrends) { this.dailyTrends = dailyTrends; }
}
