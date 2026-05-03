package com.tracker.nutrition.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Document(collection = "food_entries")
public class FoodEntry {

    @Id
    private String id;
    
    @NotBlank(message = "Food name is required")
    private String name;
    
    @NotNull(message = "Calories must be provided")
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;
    
    @NotNull(message = "Protein must be provided")
    @Min(value = 0, message = "Protein cannot be negative")
    private Double protein;
    
    @NotNull(message = "Carbs must be provided")
    @Min(value = 0, message = "Carbs cannot be negative")
    private Double carbs;
    
    @NotNull(message = "Fat must be provided")
    @Min(value = 0, message = "Fat cannot be negative")
    private Double fat;
    
    private LocalDate dateLogged;

    @DBRef
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    public FoodEntry() {
        this.dateLogged = LocalDate.now();
    }

    public FoodEntry(String name, Integer calories, Double protein, Double carbs, Double fat) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.dateLogged = LocalDate.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
    
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    
    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }
    
    public LocalDate getDateLogged() { return dateLogged; }
    public void setDateLogged(LocalDate dateLogged) { this.dateLogged = dateLogged; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
