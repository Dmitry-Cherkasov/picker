package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipesListController {
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/recipes")
    public String getRecipes(Model model) {

        if (recipeRepository.count() > 0) {
            Iterable<Recipe> recipes = recipeRepository.findAll();
            model.addAttribute("recipes", recipes);

        } else {
            List<Recipe> recipes = new ArrayList<>();
            Recipe test = new Recipe("Вода з цукром");
            test.setCalories(150);
            test.addComponent(new FoodProduct("Цукор", 0.1, 0.1, 99.7), 10);
            test.addComponent(new FoodProduct("Вода", 0.01, 0.01, 0.01), 90);
            recipes.add(test);
            Iterable<Recipe> recipes1 = recipes;
            model.addAttribute("recipes", recipes1);
        }

        return "recipes";
    }
}
