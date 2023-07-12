package com.foodfetish.picker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;


@Entity
public class FoodProduct implements Comparable<FoodProduct>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int calories;
    private Double prots;
    private Double fats;
    private Double carbs;

    public FoodProduct() {
    }

    public FoodProduct(String name, Double prots, Double fats, Double carbs) {
        this.name = name;
        this.prots = prots;
        this.fats = fats;
        this.carbs = carbs;
        this.calories = (int) (prots * 4.1 + fats * 9.3 + carbs * 4.1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodProduct that = (FoodProduct) o;
        return calories == that.calories && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(prots, that.prots) && Objects.equals(fats, that.fats) && Objects.equals(carbs, that.carbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories, prots, fats, carbs);
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void countCalories(Double prots, Double fats, Double carbs) {
        this.calories = (int) (prots * 4.1 + fats * 9.3 + carbs * 4.1);
    }

    public Double getProts() {
        return prots;
    }

    public void setProts(Double prots) {
        this.prots = prots;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    @Override
    public int compareTo(FoodProduct o) {
        return name.compareTo(o.getName());
    }
}
