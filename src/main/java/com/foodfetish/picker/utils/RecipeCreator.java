package com.foodfetish.picker.utils;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import org.apache.commons.collections4.map.LinkedMap;
import org.json.JSONObject;

import java.util.function.Function;

public class RecipeCreator {

    public RecipeCreator(){
          }

    public Recipe createRecipe(String name, String anons, LinkedMap<FoodProduct, Integer> content){
        Recipe recipe = new Recipe(name);
        recipe.setAnons(anons);

        Function<Double,Double> round = aDouble -> Math.round(aDouble*100.0)/100.0;
        Function<Double,Double> recalculate = aDouble -> aDouble*100/recipe.getWeight();

        content.forEach(recipe::addComponent);
        recipe.setProts(recalculate.andThen(round).apply(recipe.getProts()));
        recipe.setFats(round.apply(recalculate.apply(recipe.getFats())));
        recipe.setCarbs(round.apply(recalculate.apply(recipe.getCarbs())));
        recipe.setCalories(recipe.countCalories());

        LinkedMap<Long, Integer> recipeIdMap = new LinkedMap<>();
        content.forEach(((product, weight) -> recipeIdMap.put(product.getId(), weight)));

        JSONObject json = new JSONObject(recipeIdMap);
        recipe.setContentMap(json.toString());

        return recipe;
    }

}


