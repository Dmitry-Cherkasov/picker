package com.foodfetish.picker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.TreeMap;

@Entity
public class Recipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String anons;
    private String content;
    private int totalWeight=0;
    private int calories;
    private double prots;
    private double fats;
    private double carbs;

    public Recipe(){
    }

    public Recipe(String name){
        this.name = name;
        this.anons = "Currently empty";
    }

    public Recipe(String name, String description ){
        this.name = name;
        this.anons = description;
    }


    public void addComponent(FoodProduct item, Integer weight){
        totalWeight += weight;
        prots +=item.getProts()*weight/100;
        fats +=item.getFats()*weight/100;
        carbs +=item.getCarbs()*weight/100;
        calories = (int) (prots * 4.1 + fats * 9.3 + carbs * 4.1);

        if (content == null){
            content = item.getName() + " : " + weight + "\n";
        }
        else {
            content = String.join(", ", content, item.getName() + " : " + weight + "\n");
        }
    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getAnons() {
        return anons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getContent(){
        return  "Content: " + content;
    }


    public int getCalories() {
        return calories;
    }

    public int countCalories() {
        return (int) (prots*4.1+fats*9.3+carbs*9.3);
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

    public String toString(){
        StringBuilder output = new StringBuilder();

        return output.toString();
    }
}
