package com.foodfetish.picker.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ProductLister {
    private final List<String> list;

    public ProductLister() {
        this.list = scanFile();
    }

    private List<String> scanFile() {
        List<String> output = new ArrayList<>();
        try (FileReader reader = new FileReader("resources/FoodContentTableUkr.txt");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.lines().forEach(output::add);
        } catch (FileNotFoundException e) {
           output.add("Something went wrong!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.sort(Comparator.naturalOrder());
        return output;
    }

    public List<String> getList() {
        return list;
    }
}
