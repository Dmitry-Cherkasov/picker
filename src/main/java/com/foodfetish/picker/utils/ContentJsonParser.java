package com.foodfetish.picker.utils;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.map.LinkedMap;

public class ContentJsonParser {

    private FoodProductRepository foodProductRepository;

    public ContentJsonParser (FoodProductRepository repository){
        this.foodProductRepository = repository;
    }

    public LinkedMap<FoodProduct,Integer> parse(String json){
        Gson gson = new Gson();
        LinkedMap<Long, Integer> contentIdMap = gson.fromJson(json, new TypeToken<LinkedMap<Long, Integer>>() {}.getType());
        LinkedMap<FoodProduct, Integer> ingredientsList = new LinkedMap<>();
        contentIdMap.forEach((id, weight) -> {
            if(foodProductRepository.existsById(id)){
            FoodProduct product = foodProductRepository.findById(id).get();
            ingredientsList.put(product,weight);
            }
            else {
                throw new UnsupportedOperationException("Unknown product");
            }
        });
        return ingredientsList;

    }

}
