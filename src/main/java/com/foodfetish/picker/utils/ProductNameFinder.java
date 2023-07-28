package com.foodfetish.picker.utils;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProductNameFinder {
    private FoodProductRepository foodProductRepository;

    public ProductNameFinder(FoodProductRepository repo){
        this.foodProductRepository = repo;
    }

    public List<FoodProduct> find(String substring){
        Iterable<FoodProduct> repo = foodProductRepository.findAll();

        List<FoodProduct> products = StreamSupport.stream(repo.spliterator(), false)
                .filter(product -> product.getName().toLowerCase().contains(substring.toLowerCase()))
                .limit(15)
                .collect(Collectors.toList());

        return products;
    }
}
