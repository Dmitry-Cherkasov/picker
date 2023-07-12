package com.foodfetish.picker.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecipeWriter {

    public RecipeWriter(String recipe) {
        writeToFile(recipe);
    }

    private void writeToFile(String recipe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/output.txt"))) {
            writer.write(recipe);
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }

    }
}