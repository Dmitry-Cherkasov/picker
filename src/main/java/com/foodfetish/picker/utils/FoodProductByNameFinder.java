package com.foodfetish.picker.utils;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;

import java.util.stream.StreamSupport;

public class FoodProductByNameFinder {
    private FoodProductRepository foodProductRepository;

    public FoodProductByNameFinder(FoodProductRepository repo){
        if(repo == null){
            throw new UnsupportedOperationException();
        }
        this.foodProductRepository = repo;
    }

    public FoodProduct find(String substring){
        Iterable<FoodProduct> products = foodProductRepository.findAll();

        return StreamSupport.stream(products.spliterator(), true).
                filter(product ->  product.getName().equalsIgnoreCase(substring)).findFirst().get();
    }
}
