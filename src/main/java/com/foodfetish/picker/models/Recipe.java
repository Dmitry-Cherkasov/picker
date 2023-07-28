package com.foodfetish.picker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.function.Function;

@Entity
public class Recipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String anons;
    private String contentMap;
    private int totalWeight=0;
    private int calories;
    private double prots;
    private double fats;
    private double carbs;

    public Recipe(){
    }

    public Recipe(String name){
        this.name = name;
    }

    public Recipe(String name, String description ){
        this.name = name;
        this.anons = description;
    }


    public void addComponent(FoodProduct item, Integer weight){
        Function<Double,Double> round = aDouble -> Math.round(aDouble*100.0)/100.0;
        totalWeight += weight;
        prots +=round.apply(item.getProts()*weight*0.01);
        fats +=round.apply(item.getFats()*weight*0.01);
        carbs +=round.apply(item.getCarbs()*weight*0.01);

    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getAnons() {
        return anons;
    }


    public void setId(long id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public int getCalories() {
        return calories;
    }

    public int countCalories() {
        return (int) (prots*4.1+fats*9.3+carbs*4.1);
    }


    public String getContentMap() {
        return contentMap;
    }

    public void setContentMap(String contentMap) {
        this.contentMap = contentMap;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProts() {
        return prots;
    }

    public void setProts(double prots) {
        this.prots = prots;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getWeight() {
        return totalWeight;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", prots=" + prots +
                ", fats=" + fats +
                ", carbs=" + carbs +
                '}';
    }
}
