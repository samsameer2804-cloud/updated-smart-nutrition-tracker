package com.tracker.nutrition.dto;

public class DailyMacroSummary {
    private String date;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;

    public DailyMacroSummary() {}

    public DailyMacroSummary(String date, int calories, double protein, double carbs, double fat) {
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
}
